package bdbk.springframework.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 实例化策略接口
 * @author little8
 * @since 2022-03-24
 */
public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException;

}