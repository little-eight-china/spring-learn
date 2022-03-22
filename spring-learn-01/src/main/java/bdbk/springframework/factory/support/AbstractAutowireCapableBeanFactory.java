package bdbk.springframework.factory.support;

import bdbk.springframework.exception.BeansException;
import bdbk.springframework.factory.config.BeanDefinition;

/**
 * 具备创建bean能力的工厂
 * @author little8
 * @since 2022-03-21
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
		return doCreateBean(beanName, beanDefinition);
	}

	protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
		Class beanClass = beanDefinition.getBeanClass();
		Object bean = null;
		try {
			bean = beanClass.newInstance();
		} catch (Exception e) {
			throw new BeansException("实例化bean失败", e);
		}
		return bean;
	}
}
