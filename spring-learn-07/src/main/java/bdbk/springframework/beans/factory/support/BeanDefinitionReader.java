package bdbk.springframework.beans.factory.support;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.core.io.Resource;
import bdbk.springframework.core.io.ResourceLoader;

/**
 * 资源读取接口
 * @author little8
 * @since 2022-06-06
 */
public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws BeansException;

    void loadBeanDefinitions(String location) throws BeansException;

    void loadBeanDefinitions(String... locations) throws BeansException;
}
