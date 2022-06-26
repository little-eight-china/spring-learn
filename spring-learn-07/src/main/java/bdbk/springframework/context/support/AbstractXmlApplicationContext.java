package bdbk.springframework.context.support;


import bdbk.springframework.beans.factory.support.DefaultListableBeanFactory;
import bdbk.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * xml文件的应用上下文抽象类，处理从xml文件中装载beanDefinition
 * @author little8
 * @since 2022-06-13
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if (null != configLocations){
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    protected abstract String[] getConfigLocations();

}
