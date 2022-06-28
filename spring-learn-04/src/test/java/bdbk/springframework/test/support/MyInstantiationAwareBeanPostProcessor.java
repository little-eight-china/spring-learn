package bdbk.springframework.test.support;

import bdbk.springframework.beans.PropertyValues;
import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {


        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("1、InstantiationAwareBeanPostProcessor -- postProcessBeforeInstantiation");
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println("3、InstantiationAwareBeanPostProcessor -- postProcessAfterInstantiation");
        //默认返回true，什么也不做，继续下一步 初始化
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        System.out.println("4、InstantiationAwareBeanPostProcessor -- postProcessPropertyValues");
        return null;
    }
}

