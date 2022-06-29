package bdbk.springframework.beans.factory.config;

import bdbk.springframework.beans.exception.BeansException;

/**
 * bean实例化前后的智能扩展类
 * @author little_eight
 * @since 2022/6/29
 */
public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {

    Object getEarlyBeanReference(Object var1, String var2) throws BeansException;
}
