package bdbk.springframework.context.support;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.config.BeanFactoryPostProcessor;
import bdbk.springframework.beans.factory.config.BeanPostProcessor;
import bdbk.springframework.beans.factory.support.ConfigurableListableBeanFactory;
import bdbk.springframework.context.ApplicationContext;
import bdbk.springframework.context.ApplicationEvent;
import bdbk.springframework.context.ApplicationListener;
import bdbk.springframework.context.ConfigurableApplicationContext;
import bdbk.springframework.context.event.ApplicationEventMulticaster;
import bdbk.springframework.context.event.ContextClosedEvent;
import bdbk.springframework.context.event.ContextRefreshedEvent;
import bdbk.springframework.context.event.SimpleApplicationEventMulticaster;
import bdbk.springframework.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 抽象应用上下文
 * @author little8
 * @since 2022-06-13
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

    private long startupDate;

    @Override
    public void refresh() throws BeansException {
        // 1、刷新前准备
        prepareRefresh();

        // 2、创建bean工厂，并从xml文件中装载beanDefinition后注册到工厂里
        ConfigurableListableBeanFactory beanFactory = this.obtainFreshBeanFactory();

        // 3、bean工厂预备前处理
        prepareBeanFactory(beanFactory);

        // 4、在 bean 实例化之前，执行 BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        // 5. 注册beanPostProcessor
        registerBeanPostProcessors(beanFactory);

        // 6. 初始化事件发布者
        initApplicationEventMulticaster();

        // 7. 注册事件监听器
        registerListeners();

        // 8. 提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();

        // 9. 发布容器刷新完成事件
        finishRefresh();
    }

    protected void prepareRefresh() {
        this.startupDate = System.currentTimeMillis();
    }

    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        // 创建bean工厂，并从xml文件中装载beanDefinition后注册到工厂里
        refreshBeanFactory();
        return getBeanFactory();
    }

    protected abstract void refreshBeanFactory() throws BeansException;

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

    }

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }
    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    private void registerListeners() {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }


    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        // 发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));

        // 执行销毁单例bean的销毁方法
        getBeanFactory().destroySingletons();
    }

    public long getStartupDate() {
        return this.startupDate;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("startup date [")
                .append(new Date(this.getStartupDate()))
                .append("]; ");
        return sb.toString();
    }
}
