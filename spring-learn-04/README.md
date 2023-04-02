# spring-learn-04

第一阶段我们做了一个简易的spring ioc了，就像麻雀一样，五脏俱全了，所以后面的步骤，先慢慢优化这只”麻雀“先。

第二阶段我期望优化实例化bean这一块。

第三阶段，通过配置文件来实例化bean。

做完了三个阶段，我们可以通过这个做好的框架使用上比较完整的spring ioc功能（注册、获取bean）了，但缺乏了对bean对象的额外扩展功能，所以第四阶段将去做bean对象的扩展。

先梳理下现在的获取bean的步骤：
- 实例化bean
- 属性注入
- 返回bean


现在加点扩展的东西：
- 实例化bean前，处理InstantiationAwareBeanPostProcessor接口(postProcessBeforeInstantiation)
- 实例化bean后，处理InstantiationAwareBeanPostProcessor接口(postProcessAfterInstantiation)
- 属性注入前，处理InstantiationAwareBeanPostProcessor接口(postProcessPropertyValues)
- 属性注入
- 处理Aware接口
- 处理BeanPostProcessor后置处理器的before方法postProcessBeforeInitialization
- 处理InitializingBean接口（afterPropertiesSet）
- 处理init-method方法
- 处理BeanPostProcessor后置处理器的after方法postProcessAfterInitialization
- 返回bean
- 处理FactoryBean对象

从上面的步骤可以看到，从实例化bean跟属性填充的注入的前后，都做了一个比较全面的扩展。

** 实例化前 **
判断是否有继承InstantiationAwareBeanPostProcessor的BeanPostProcessor，有的话，就执行相应方法返回bean
```java
protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
		for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
			if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
				Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
				if (null != result) return result;
			}
		}
		return null;
	}
```

当上面的方法不返回null时，才会去处理BeanPostProcessor的postProcessAfterInitialization
```java
	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
		Object result = existingBean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessAfterInitialization(result, beanName);
			if (null == current) return result;
			result = current;
		}
		return result;
	}

```

** 实例化后 **
判断是否有实现InstantiationAwareBeanPostProcessor的postProcessAfterInstantiation，如果返回的是false，则会终止后面的步骤，在这里直接返回已经实例化的bean

```java
protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        // .....
        // 实例化后判断
        boolean continueWithPropertyPopulation = applyBeanPostProcessorsAfterInstantiation(beanName, bean);
        if (!continueWithPropertyPopulation) {
            return bean;
        }
        // .....
	}
```

```java
private boolean applyBeanPostProcessorsAfterInstantiation(String beanName, Object bean) {
		boolean continueWithPropertyPopulation = true;
		for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
			if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
				InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor = (InstantiationAwareBeanPostProcessor) beanPostProcessor;
				if (!instantiationAwareBeanPostProcessor.postProcessAfterInstantiation(bean, beanName)) {
					continueWithPropertyPopulation = false;
					break;
				}
			}
		}
		return continueWithPropertyPopulation;
	}

```

** 属性注入前 **
在设置 Bean 属性之前，允许 BeanPostProcessor 修改beanDefinition的属性值
通过相关接口InstantiationAwareBeanPostProcessor的getPropertyValues，来修beanDefinition

```java

protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        // .....
        // 给 Bean 填充属性
        populateBean(beanName, beanDefinition, bean);
        // 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
        bean = InitializeBean(beanName, bean, beanDefinition);
        // .....
	}
```

```java

protected void populateBean(String beanName, BeanDefinition beanDefinition, Object bean) {
		// 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
		applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
		applyPropertyValues(beanName, beanDefinition, bean);
	}

```

```java

	/**
	 * 在设置属性之前，允许 BeanPostProcessor 修改属性值
	 */
	protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
		for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
			if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
				PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
				if (null != pvs) {
					for (PropertyValue propertyValue : pvs.getPropertyValues()) {
						beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
					}
				}
			}
		}
	}

```

```java

protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        // .....
        // 实例化后判断
        boolean continueWithPropertyPopulation = applyBeanPostProcessorsAfterInstantiation(beanName, bean);
        if (!continueWithPropertyPopulation) {
            return bean;
        }
        // .....
	}
```

** 处理Aware接口 **
这里给出BeanFactoryAwar、BeanClassLoaderAware、BeanNameAware，通过接口的方法可以获取到对应信息，自己做处理

```java

protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        // .....
        // 给 Bean 填充属性
        populateBean(beanName, beanDefinition, bean);
        // 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
        bean = InitializeBean(beanName, bean, beanDefinition);
        // .....
	}
```

```java

private Object InitializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
		// invokeAwareMethods
		if (bean instanceof Aware) {
			if (bean instanceof BeanFactoryAware) {
				((BeanFactoryAware) bean).setBeanFactory(this);
			}
			if (bean instanceof BeanClassLoaderAware) {
				((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
			}
			if (bean instanceof BeanNameAware) {
				((BeanNameAware) bean).setBeanName(beanName);
			}
		}

		//...
	}
```

** 处理BeanPostProcessor后置处理器的before方法 **
判断是否有实现BeanPostProcessor的postProcessBeforeInitialization，有的话直接返回期望的bean

```java

private Object InitializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        //...    
    
    // 2. 执行 BeanPostProcessor Before 处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);


        //...
	}
```
```java
	public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
		Object result = existingBean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessBeforeInitialization(result, beanName);
			if (null == current) return result;
			result = current;
		}
		return result;
	}

```


** 处理InitializingBean接口 **
判断当前bean是否实现InitializingBean接口的，是的话就执行afterPropertiesSet方法


```java

private Object InitializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        //...    

        // 3、执行 Bean 对象的初始化方法
        try {
        invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
        throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", e);
        }

        //...
	}
```

```java

private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
		boolean isInitializingBean = bean instanceof InitializingBean;
		// 1. 实现接口 InitializingBean
		if (isInitializingBean) {
			((InitializingBean) bean).afterPropertiesSet();
		}

		// 2. 注解配置 init-method {判断是为了避免二次执行销毁}
		if (beanDefinition != null) {
			String initMethodName = beanDefinition.getInitMethodName();
			if (StrUtil.isNotEmpty(initMethodName)) {
				Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
				if (null == initMethod) {
					throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
				}
				initMethod.invoke(bean);
			}
		}

	}
```

** 处理init-method方法 **
查看当前beanDefinition是否配置了initMethodName方法，有的话就去执行initMethodName对应的方法


```java

private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        // 1. 实现接口 InitializingBean
        if (bean instanceof InitializingBean) {
        ((InitializingBean) bean).afterPropertiesSet();
        }

		// 2. 注解配置 init-method {判断是为了避免二次执行销毁}
		if (beanDefinition != null) {
			String initMethodName = beanDefinition.getInitMethodName();
			if (StrUtil.isNotEmpty(initMethodName)) {
				Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
				if (null == initMethod) {
					throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
				}
				initMethod.invoke(bean);
			}
		}

	}
```

** 处理BeanPostProcessor后置处理器的after方法 **
判断是否有实现BeanPostProcessor的postProcessAfterInitialization，有的话直接返回期望的bean



```java

private Object InitializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        //...    

        // 4. 执行 BeanPostProcessor After 处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

        //...
	}
```

```java
	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
		Object result = existingBean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessAfterInitialization(result, beanName);
			if (null == current) return result;
			result = current;
		}
		return result;
	}

```

** 处理FactoryBean对象 **
在完全实例化bean以及填充bean后，还有一次机会是查看该bean是否为FactoryBean来处理

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

FactoryBean有自己的map进行维护
```java
private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
		if (!(beanInstance instanceof FactoryBean)) {
			return beanInstance;
		}

		Object object = getCachedObjectForFactoryBean(beanName);

		if (object == null) {
			FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
			object = getObjectFromFactoryBean(factoryBean, beanName);
		}

		return object;
	}

```

实现这些的话，我们便很方便在获取bean的过程中，去动态对bean进行处理。
