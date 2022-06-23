package bdbk.springframework.beans.factory.config;

import bdbk.springframework.beans.PropertyValues;
import bdbk.springframework.beans.exception.BeansException;

import java.beans.PropertyDescriptor;

/**
 * bean实例化
 * @author little8
 * @since 2022-06-12
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 在 Bean 对象执行初始化方法之前，执行此方法
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    /**
     * 在 Bean 对象执行初始化方法之后，执行此方法
     */
    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;


    /**
     * 在 Bean 对象实例化完成后，设置属性操作之前执行此方法
     */
    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;
}
