package bdbk.springframework.beans.factory.support;

import bdbk.springframework.beans.factory.BeanFactory;

/**
 * beanFactory接口的扩展接口，它可以枚举所有的bean实例，而不是客户端通过名称一个一个的查询得出所有的实例
 * @author little8
 * @since 2022-03-21
 */
public interface ListableBeanFactory extends BeanFactory {

    // 对于给定的名字是否含有BeanDefinition
    boolean containsBeanDefinition(String beanName);

    // 返回工厂的BeanDefinition总数
    int getBeanDefinitionCount();
}
