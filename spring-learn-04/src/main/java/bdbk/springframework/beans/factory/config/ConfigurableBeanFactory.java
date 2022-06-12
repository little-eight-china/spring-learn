package bdbk.springframework.beans.factory.config;

/**
 * 提供配置BeanFactory的各种方法，目前只有配置可扩展类的方法
 * @author little8
 * @since 2022-06-09
 */
public interface ConfigurableBeanFactory {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    int getBeanPostProcessorCount();
}
