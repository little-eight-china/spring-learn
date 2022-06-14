package bdbk.springframework.context.support;

import bdbk.springframework.beans.exception.BeansException;

/**
 * xml文件应用上下文，这是用户使用的一个门户起点
 * @author little8
 * @since 2022-06-13
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private String[] configLocations;

    public ClassPathXmlApplicationContext() {
    }

    /**
     * 从 xml 中装载 beanDefinition，并刷新上下文
     */
    public ClassPathXmlApplicationContext(String configLocations) throws BeansException {
        this(new String[]{configLocations});
    }

    /**
     * 从 xml 中装载 beanDefinition，并刷新上下文
     */
    public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }
}
