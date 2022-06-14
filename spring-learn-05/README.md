# spring-learn-03

第一阶段我们做了一个简易的spring ioc了，就像麻雀一样，五脏俱全了，所以后面的步骤，先慢慢优化这只”麻雀“先。

第二阶段我期望优化实例化bean这一块。

第三阶段，通过配置文件来实例化bean。

第四阶段，去做bean对象的扩展。

第五阶段，做应用上下文，更优雅地管理bean的生命周期。

既然有了上下文，那之前没办法做的事情现在都可以干了，比如懒加载，跟销毁bean时实现的方法
```java

public class BeanDefinition {
    private boolean lazyInit;

    private String destroyMethodName;

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public boolean isLazyInit() {
        return this.lazyInit;
    }

}
```
销毁bean时的方法需要定义接口DisposableBean, 最终会跟普通的bean区分开来，重新用一个disposableBeans来作为注册的容器

```java
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String, DisposableBean> disposableBeans = new LinkedHashMap<>();


    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    public void destroySingletons() {
        Set<String> keySet = this.disposableBeans.keySet();
        Object[] disposableBeanNames = keySet.toArray();

        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            Object beanName = disposableBeanNames[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }
    }
}

```
后面在创建bean的时候，去判断是否需要注册销毁bean即可

```java
protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		//...

		// 注册实现了 DisposableBean 接口的 bean 对象，这样后续应用上下文可执行相应的销毁方法
		registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);


		if (beanDefinition.isSingleton()) {
			registerSingleton(beanName, bean);
		}
		return bean;
	}
```

```java
	protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
		// 非 Singleton 类型的 Bean 不执行销毁方法
		if (!beanDefinition.isSingleton()) return;

		if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
			registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
		}
	}
```

还有基于原生的java.util.EventObject、java.util.EventListener来实现应用上下文的监听事件，让用户自己去处理感兴趣的事件。
应用上下文最核心的方法refresh(),从注释便可看出执行了什么
```java
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    @Override
    public void refresh() throws BeansException {
        // 1、刷新前准备
        prepareRefresh();

        // 2、创建bean工厂，并从xml文件中装载beanDefinition后注册到工厂里
        ConfigurableListableBeanFactory beanFactory = this.obtainFreshBeanFactory();

        // 3. 注册beanPostProcessor
        registerBeanPostProcessors(beanFactory);

        // 4. 初始化事件发布者
        initApplicationEventMulticaster();

        // 5. 注册事件监听器
        registerListeners();

        // 6. 提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();

        // 7. 发布容器刷新完成事件
        finishRefresh();
    }
}


```
