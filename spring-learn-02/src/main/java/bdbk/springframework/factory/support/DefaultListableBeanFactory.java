package bdbk.springframework.factory.support;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import bdbk.springframework.exception.BeansException;
import bdbk.springframework.factory.config.BeanDefinition;

/**
 * 携带bean定义集合的实现工厂，可根据此维护的集合来操作已经注册过的bean定义对象
 * @author little8
 * @since 2022-03-21
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ListableBeanFactory {

	private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		beanDefinitionMap.put(beanName, beanDefinition);
	}

	@Override
    protected BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        if (!containsBeanDefinition(beanName)) {
			throw new BeansException(String.format("No bean named %s is defined", beanName));
		}
		return beanDefinitionMap.get(beanName);
	}

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }
}
