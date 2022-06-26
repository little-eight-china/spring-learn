package bdbk.springframework.beans.factory.config;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.support.ConfigurableListableBeanFactory;

/**
 * 实例化bean前对bean信息的扩展接口
 * @author little8
 * @since 2022-06-22
 */
public interface BeanFactoryPostProcessor {

    /**
     * 在所有的 BeanDefinition 加载完成后，实例化 Bean 对象之前，提供修改 BeanDefinition 属性的机制
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
