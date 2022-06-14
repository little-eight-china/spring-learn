package bdbk.springframework.context.event;


import bdbk.springframework.beans.factory.BeanFactory;
import bdbk.springframework.context.ApplicationEvent;
import bdbk.springframework.context.ApplicationListener;

/**
 * xml文件应用上下文，这是用户使用的一个门户起点
 * @author little8
 * @since 2022-06-14
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void multicastEvent(final ApplicationEvent event) {
        for (final ApplicationListener listener : getApplicationListeners(event)) {
            listener.onApplicationEvent(event);
        }
    }

}
