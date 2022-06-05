package bdbk.springframework.factory.support;


import bdbk.springframework.exception.BeansException;
import bdbk.springframework.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 实例化策略接口
 * @author little8
 * @since 2022-06-05
 */
public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException;

}
