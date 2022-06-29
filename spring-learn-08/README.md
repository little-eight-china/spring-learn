# spring-learn-08

第一阶段我们做了一个简易的spring ioc了，就像麻雀一样，五脏俱全了，所以后面的步骤，先慢慢优化这只”麻雀“先。

第二阶段我期望优化实例化bean这一块。

第三阶段，通过配置文件来实例化bean。

第四阶段，去做bean对象的扩展。

第五阶段，做应用上下文，更优雅地管理bean的生命周期。

第六阶段，实现注解方式的注入。

第七阶段，引入AOP。

现在spring-learn已经变的很完整了，下面的话就先不写新功能，先解决掉一个spring业界都闻名的bug：循环依赖。

先说说为啥会出现循环依赖，假设bean1依赖bean2，然后bean2又依赖bean1。
在获取bean1时，通过getBean方法调到AbstractBeanFactory的doGetBean

```java
public Object doGetBean(String name, Object[] args) throws BeansException {
		BeanDefinition beanDefinition = getBeanDefinition(name);
		if (beanDefinition.isSingleton()) {
			Object bean = getSingleton(name);
			if (bean == null) {
				bean = createBean(name, beanDefinition, args);
			}
			// 给factoryBean注入机会
			return getObjectForBeanInstance(bean, name);
		} else {
			Object bean = createBean(name, beanDefinition, args);
			// 给factoryBean注入机会
			return getObjectForBeanInstance(bean, name);
		}
	}

```
可以看到最后都会调用到AbstractAutowireCapableBeanFactory的doCreateBean:

```java
protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
            // ...
			// 给 Bean 填充属性
			populateBean(beanName, beanDefinition, bean);
			// ...
		}
```
给bean1填充属性bean2时，下面2个处理无论怎么处理都会再次调到getBean

```java
	protected void populateBean(String beanName, BeanDefinition beanDefinition, Object bean) {
		// 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
		applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
		applyPropertyValues(beanName, beanDefinition, bean);
	}
```

那bean2又通过getBean方法来调到同样的方法来处理依赖的bean1，这样就进入了死循环了。

为了终止这种死循环，那肯定是需要在调用populateBean之前，就把当前的bean存到一个引用缓存中，模仿spring的话就是三级缓存。

首先需要理解的是，** 只有单例模式才能解决循环依赖。 **

我们先设计一个对象工厂，从名字上来理解就是工厂里的一个对象

```java
public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}

```
        

我们设计三个缓存map在单例注册类DefaultSingletonBeanRegistry里
```java
/**
     * 一级缓存，普通对象
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    /**
     * 二级缓存，提前暴漏对象，没有完全实例化的对象
     */
    protected final Map<String, Object> earlySingletonObjects = new HashMap<>();

    /**
     * 三级缓存，存放工厂对象
     */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();
```
当我们通过AbstractAutowireCapableBeanFactory的createBeanInstance拿到实例化后的bean时，本质上它只能算是一个工厂里的对象，那就在没对这个bean做任何处理时就放到singletonFactories里

下面就是通过addSingletonFactory方法，搞到singletonFactories里

```java
protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Object bean;
		try {
			bean = createBeanInstance(beanDefinition, beanName, args);

			// 处理循环依赖，将实例化后的Bean对象提前放入缓存中暴露出来
			if (beanDefinition.isSingleton()) {
				Object finalBean = bean;
				addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean));
			}

			// ...
	}

protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory){
synchronized (this.singletonObjects) {
        if (!this.singletonObjects.containsKey(beanName)) {
        this.singletonFactories.put(beanName, singletonFactory);
        this.earlySingletonObjects.remove(beanName);
        }
        }
        }
```

那什么时候存到earlySingletonObjects呢？我们通过调用链可以知道，AbstractBeanFactory的doGetBean方法里只要getSingleton返回的bean不为空，那他就不会再次进入到createBean->getBean这种死循环了，
所以处理earlySingletonObjects就放到getSingleton里做
```java
	public Object doGetBean(String name, Object[] args) throws BeansException {
		BeanDefinition beanDefinition = getBeanDefinition(name);
		if (beanDefinition.isSingleton()) {
			Object bean = getSingleton(name);
			if (bean == null) {
				bean = createBean(name, beanDefinition, args);
			}
			// 给factoryBean注入机会
			return getObjectForBeanInstance(bean, name);
		} else {
			Object bean = createBean(name, beanDefinition, args);
			// 给factoryBean注入机会
			return getObjectForBeanInstance(bean, name);
		}
	}

```
怎么做呢，就是先判断一级缓存有没有，没有的话就去查二级缓存，二级缓存也没的话，就去三级缓存，三级缓存有的话，就把它移到二级缓存里（新增到二级缓存并删掉三级缓存的相应对象的意思）
```java
 public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);
        if (null == singletonObject) {
            singletonObject = earlySingletonObjects.get(beanName);
            // 判断二级缓存中是否有对象，没有的话就去三级拿
            if (null == singletonObject) {
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    // 把三级缓存中的工厂对象中的真实对象获取出来，放入二级缓存中
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

```

这时候我们注意到，当我们往一个缓存写入时，就必须移除其他2个缓存相同beanName的bean。

到最后成功调用到DefaultSingletonBeanRegistry的registerSingleton时，二级、三级缓存对应的bean就可以功成身退了，直接存到我们的一级缓存，也代表着一个完整的bean存储。
```java
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized(this.singletonObjects) {
            singletonObjects.put(beanName, singletonObject);
            earlySingletonObjects.remove(beanName);
            singletonFactories.remove(beanName);
        }
    }

```

这样的话，如果存在循环依赖的话，我们就可以通过三级缓存提前暴露出未完全实例化好的bean(也就是bean的引用)，先使用着提前曝光的bean引用填充到属性里，
直到大家都实例化完毕。
