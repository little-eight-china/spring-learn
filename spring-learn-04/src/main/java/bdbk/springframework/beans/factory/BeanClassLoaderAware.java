package bdbk.springframework.beans.factory;

/**
 * Callback that allows a bean to be aware of the bean
 * @author little8
 * @since 2022-06-12
 */
public interface BeanClassLoaderAware extends Aware{

    void setBeanClassLoader(ClassLoader classLoader);

}


