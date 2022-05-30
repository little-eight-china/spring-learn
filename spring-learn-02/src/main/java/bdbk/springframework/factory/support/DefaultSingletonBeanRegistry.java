package bdbk.springframework.factory.support;

import java.util.HashMap;
import java.util.Map;


/**
 * 默认单例实现
 * @author little8
 * @since 2022-03-25
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }

}
