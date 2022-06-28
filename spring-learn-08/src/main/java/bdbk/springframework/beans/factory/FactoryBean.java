package bdbk.springframework.beans.factory;

/**
 * FactoryBean是动态注入bean，而BeanFactory主要是负责Bean实例化、定位、配置应用程序中的对象及建立这些对象间的依赖。
 * @author little8
 * @since 2022-06-18
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();

}
