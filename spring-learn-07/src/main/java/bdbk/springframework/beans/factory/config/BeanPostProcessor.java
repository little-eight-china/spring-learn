package bdbk.springframework.beans.factory.config;

import bdbk.springframework.beans.exception.BeansException;

/**
 * 对已经实例化的bean进行再次处理和修改，返回新定义的bean
 * @author little8
 * @since 2022-06-09
 */
public interface BeanPostProcessor {

    /**
     * 在 Bean 对象执行初始化方法之前，执行此方法
     *
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * 在 Bean 对象执行初始化方法之后，执行此方法
     *
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
