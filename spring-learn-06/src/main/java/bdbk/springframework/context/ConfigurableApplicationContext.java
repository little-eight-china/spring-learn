package bdbk.springframework.context;


import bdbk.springframework.beans.exception.BeansException;

/**
 * 可配置化的应用上下文接口
 * @author little8
 * @since 2022-06-13
 */
public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 刷新容器
     */
    void refresh() throws BeansException;

    void registerShutdownHook();

    void close();

}
