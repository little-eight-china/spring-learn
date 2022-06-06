# spring-learn-02

第一阶段我们做了一个简易的spring ioc了，就像麻雀一样，五脏俱全了，所以后面的步骤，先慢慢优化这只”麻雀“先。

第二阶段我期望优化实例化bean这一块。

目前只是支持默认的无参构造函数进行实例化，那下面就做个在实例化时填充成员变量的bean。

定义beanDefinition的作用域：单例Singleton与原型Prototype。

```java

public class BeanDefinition {

    private boolean isSingleton;

    private boolean isPrototype;

    // ...
}

```

在获取bean的时候，会去判断作用域来决定怎么获取bean
```java
public Object doGetBean(String name, Object[] args) throws BeansException {
		BeanDefinition beanDefinition = getBeanDefinition(name);
		if (beanDefinition.isSingleton()) {
			Object bean = getSingleton(name);
			if (bean == null) {
				return createBean(name, beanDefinition, args);
			}
			return bean;
		} else {
			return createBean(name, beanDefinition, args);
		}

	}

```


新建PropertyValue，标识每一位成员变量，再新建PropertyValues作为一个bean的全部成员变量,propertyValues作为BeanDefinition里成员变量。

```java
public class BeanDefinition {

    private PropertyValues propertyValues;

    // ...
}


```

在doCreateBean时，给bean填充进去

```java
	protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Object bean;
		try {
			bean = createBeanInstance(beanDefinition, beanName, args);
			// 给 Bean 填充属性
			applyPropertyValues(beanName, bean, beanDefinition);
		} catch (Exception e) {
			throw new BeansException("Instantiation of bean failed", e);
		}

		addSingleton(beanName, bean);
		return bean;
	}

```

```java

protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
		try {
			PropertyValues propertyValues = beanDefinition.getPropertyValues();
			for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {

				String name = propertyValue.getName();
				Object value = propertyValue.getValue();

				if (value instanceof BeanReference) {
					// A 依赖 B，获取 B 的实例化
					BeanReference beanReference = (BeanReference) value;
					value = getBean(beanReference.getBeanName());
				}
				// 属性填充
				BeanUtil.setFieldValue(bean, name, value);
			}
		} catch (Exception e) {
			throw new BeansException("Error setting property values：" + beanName);
		}
	}

```

这样的话，我们就可以通过**DefaultListableBeanFactory**去拿到我们期望的bean对象。基本步骤如下：
- 先new一个工厂，DefaultListableBeanFactory
- 新建一个期望的对象，然后记录下这个对象的基本信息，还把对象的成员变量也填充进去。
- 往工厂里注册这个对象的基本信息，或者说配方。
- 客户端通过工厂的getBean方法，获取到该对象。
