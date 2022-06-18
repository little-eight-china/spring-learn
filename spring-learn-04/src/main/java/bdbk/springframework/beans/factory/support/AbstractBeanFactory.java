package bdbk.springframework.beans.factory.support;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.BeanFactory;
import bdbk.springframework.beans.factory.FactoryBean;
import bdbk.springframework.beans.factory.config.BeanDefinition;
import bdbk.springframework.beans.factory.config.BeanPostProcessor;
import bdbk.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * bean工厂的抽象类，提供获取bean，并根据模板模式开放获取bean对象定义、创建bean的功能
 * @author little8
 * @since 2022-03-21
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements BeanFactory {

	/**
	 * ClassLoader
	 */
	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

	/**
	 * BeanPostProcessor列表
	 */
	private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

	@Override
	public Object getBean(String name) throws BeansException {
		return doGetBean(name, null);
	}

	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		return doGetBean(name, args);
	}

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

	/**
	 * 处理普通Bean与FactoryBean的获取
	 */
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


	protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

	protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

	/**
	 * 添加扩展
	 */
	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
		this.beanPostProcessors.remove(beanPostProcessor);
		this.beanPostProcessors.add(beanPostProcessor);
	}

	public int getBeanPostProcessorCount() {
		return this.beanPostProcessors.size();
	}

	/**
	 * 获取所有的扩展类
	 */
	public List<BeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}

	public ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}
}
