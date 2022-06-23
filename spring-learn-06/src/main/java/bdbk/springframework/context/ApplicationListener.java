package bdbk.springframework.context;

import java.util.EventListener;

/**
 * 监听器接口
 * @author little8
 * @since 2022-06-14
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * 处理监听到事件后的逻辑
     */
    void onApplicationEvent(E event);

}
