package bdbk.springframework.context.support;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.support.ConfigurableListableBeanFactory;
import bdbk.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 刷新应用上下文抽象类
 * @author little8
 * @since 2022-06-13
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

}
