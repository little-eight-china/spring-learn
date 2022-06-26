package bdbk.springframework.beans.factory.support;


import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.config.AutowireCapableBeanFactory;
import bdbk.springframework.beans.factory.config.BeanDefinition;
import bdbk.springframework.beans.factory.config.ConfigurableBeanFactory;
import bdbk.springframework.util.StringValueResolver;

/**
 * 可配置化的比较完整的bean工厂接口，可最大程度地管理到bean
 * @author little8
 * @since 2022-06-09
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    void preInstantiateSingletons() throws BeansException;

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void addEmbeddedValueResolver(StringValueResolver var1);

    String resolveEmbeddedValue(String var1);
}
