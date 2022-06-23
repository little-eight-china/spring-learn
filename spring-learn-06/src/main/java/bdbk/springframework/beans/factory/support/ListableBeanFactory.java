package bdbk.springframework.beans.factory.support;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.BeanFactory;

import java.util.Map;

/**
 * beanFactory接口的扩展接口，它可以枚举所有的bean实例，而不是客户端通过名称一个一个的查询得出所有的实例
 * @author little8
 * @since 2022-03-21
 */
public interface ListableBeanFactory extends BeanFactory {

    boolean containsBeanDefinition(String beanName);

    /**
     * 按照类型返回 Bean 实例
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    int getBeanDefinitionCount();

    String[] getBeanDefinitionNames();
}
