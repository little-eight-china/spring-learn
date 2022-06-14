package bdbk.springframework.context;

/**
 * 事件发布者接口
 * @author little8
 * @since 2022-06-14
 */
public interface ApplicationEventPublisher {

    /**
     * 发布事件，通知相关监听器
     */
    void publishEvent(ApplicationEvent event);

}
