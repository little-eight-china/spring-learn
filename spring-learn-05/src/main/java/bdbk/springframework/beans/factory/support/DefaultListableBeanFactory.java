package bdbk.springframework.beans.factory.support;

import java.util.HashMap;
import java.util.Map;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.config.BeanDefinition;

/**
 * 携带bean定义集合的实现工厂，可根据此维护的集合来操作已经注册过的bean定义对象
 * @author little8
 * @since 2022-03-21
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

	private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

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
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Class beanClass = beanDefinition.getBeanClass();
            if (type.isAssignableFrom(beanClass)) {
                result.put(beanName, (T) getBean(beanName));
            }
        });
        return result;
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        beanDefinitionMap.keySet()
                .stream().filter(beanName -> beanDefinitionMap.get(beanName).isLazyInit())
                .forEach(this::getBean);
    }
}
