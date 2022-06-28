package bdbk.springframework.beans.factory.config;

/**
 * 单例bean注册接口
 * @author little8
 * @since 2022-06-14
 */
public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);

    void registerSingleton(String beanName, Object singletonObject);

}
