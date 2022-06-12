package bdbk.springframework.beans.factory.support;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.PropertyValue;
import bdbk.springframework.beans.PropertyValues;
import bdbk.springframework.beans.factory.config.BeanDefinition;
import bdbk.springframework.beans.factory.config.BeanReference;
import cn.hutool.core.bean.BeanUtil;

import java.lang.reflect.Constructor;

/**
 * 具备实例化bean能力的抽象工厂
 * @author little8
 * @since 2022-03-21
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

	private final InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		return doCreateBean(beanName, beanDefinition, args);
	}

	protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Object bean;
		try {
			bean = createBeanInstance(beanDefinition, beanName, args);
			// 给 Bean 填充属性
			populateBean(beanName, beanDefinition, bean);
		} catch (Exception e) {
			throw new BeansException("Instantiation of bean failed", e);
		}

		if (beanDefinition.isSingleton()) {
			addSingleton(beanName, bean);
		}
		return bean;
	}

	protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
		Constructor constructorToUse = null;
		Class<?> beanClass = beanDefinition.getBeanClass();
		Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
		for (Constructor ctor : declaredConstructors) {
			if (null != args && ctor.getParameterTypes().length == args.length) {
				constructorToUse = ctor;
				break;
			}
		}

		return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructorToUse, args);
	}

	protected void populateBean(String beanName, BeanDefinition beanDefinition, Object bean) {
		this.applyPropertyValues(beanName, beanDefinition, bean);
	}

	/**
	 * Bean 属性填充
	 */
	protected void applyPropertyValues(String beanName, BeanDefinition beanDefinition, Object bean) {
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

	public InstantiationStrategy getInstantiationStrategy() {
		return instantiationStrategy;
	}
}
