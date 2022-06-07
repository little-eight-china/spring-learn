package bdbk.springframework.beans.factory.support;

import bdbk.springframework.beans.factory.config.BeanDefinition;

/**
 * bean定义的注册接口
 * @author little8
 * @since 2022-03-21
 */
public interface BeanDefinitionRegistry {
    /**
	 * 向注册表中注BeanDefinition
	 *
	 * @param beanName 容器名字
	 * @param beanDefinition 容器结构
	 */
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
