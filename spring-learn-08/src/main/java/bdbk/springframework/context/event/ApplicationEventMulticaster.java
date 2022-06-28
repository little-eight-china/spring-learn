package bdbk.springframework.context.event;

import bdbk.springframework.context.ApplicationEvent;
import bdbk.springframework.context.ApplicationListener;

/**
 * 事件广播器接口，注册监听器和发布事件的广播器，提供添加、移除和发布事件方法。
 * @author little8
 * @since 2022-06-14
 */
public interface ApplicationEventMulticaster {

    /**
     * Add a listener to be notified of all events.
     * @param listener the listener to add
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * Remove a listener from the notification list.
     * @param listener the listener to remove
     */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * Multicast the given application event to appropriate listeners.
     * @param event the event to multicast
     */
    void multicastEvent(ApplicationEvent event);

}
