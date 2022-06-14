package bdbk.springframework.beans.factory.config;

import bdbk.springframework.beans.factory.support.ListableBeanFactory;

/**
 * 提供配置BeanFactory的各种方法，目前只有配置可扩展类的方法
 * @author little8
 * @since 2022-06-09
 */
public interface ConfigurableBeanFactory extends SingletonBeanRegistry {
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    int getBeanPostProcessorCount();

    /**
     * 销毁单例对象
     */
    void destroySingletons();
}
