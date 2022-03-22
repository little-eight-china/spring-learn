package bdbk.springframework.factory.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bdbk.springframework.exception.BeansException;
import bdbk.springframework.factory.BeanFactory;
import bdbk.springframework.factory.config.BeanDefinition;

public class DefaultBeanFactory implements BeanFactory {
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    @Override
    public Object getBean(String name) throws RuntimeException, InstantiationException, IllegalAccessException{
        return beanDefinitionMap.get(name).getBeanClass().newInstance();
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
    }

}
