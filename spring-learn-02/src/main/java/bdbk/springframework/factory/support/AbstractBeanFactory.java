package bdbk.springframework.factory.support;

import bdbk.springframework.exception.BeansException;
import bdbk.springframework.factory.BeanFactory;
import bdbk.springframework.factory.config.BeanDefinition;


/**
 * bean工厂的抽象类，提供获取bean，并根据模板模式开放获取bean对象定义、创建bean的功能
 * @author little8
 * @since 2022-03-21
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

	@Override
	public Object getBean(String name) throws BeansException {
		return doGetBean(name, null);
	}

	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		return doGetBean(name, args);
	}

	public Object doGetBean(String name, Object[] args) throws BeansException {
		Object bean = getSingleton(name);
		if (bean != null) {
			return bean;
		}
		BeanDefinition beanDefinition = getBeanDefinition(name);
		return createBean(name, beanDefinition, args);
	}


	protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

	protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;
}
