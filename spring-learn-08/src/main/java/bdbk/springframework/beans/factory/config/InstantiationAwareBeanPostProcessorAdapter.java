package bdbk.springframework.beans.factory.config;

import bdbk.springframework.beans.PropertyValues;
import bdbk.springframework.beans.exception.BeansException;

import java.beans.PropertyDescriptor;

/**
 * bean实例化过程中需要处理的事情，包括实例化bean的前后过程以及bean的属性设置
 * @author little8
 * @since 2022-06-22
 */
public abstract class InstantiationAwareBeanPostProcessorAdapter implements SmartInstantiationAwareBeanPostProcessor {
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
