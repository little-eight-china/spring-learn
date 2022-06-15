package bdbk.springframework.test.support;

import bdbk.springframework.beans.PropertyValues;
import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import bdbk.springframework.test.bean.UserDao;
import bdbk.springframework.test.bean.UserService;

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
        System.out.println(String.format("[%s]1、InstantiationAwareBeanPostProcessor -- postProcessBeforeInstantiation", beanName));
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println(String.format("[%s]3、InstantiationAwareBeanPostProcessor -- postProcessAfterInstantiation", beanName));
        //默认返回true，什么也不做，继续下一步 初始化
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        System.out.println(String.format("[%s]4、InstantiationAwareBeanPostProcessor -- postProcessPropertyValues", beanName));
        return null;
    }
}

