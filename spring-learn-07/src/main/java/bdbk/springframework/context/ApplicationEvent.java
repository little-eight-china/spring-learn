package bdbk.springframework.context;

import java.util.EventObject;

/**
 * 事件抽象类
 * @author little8
 * @since 2022-06-14
 */
public abstract class ApplicationEvent extends EventObject {

    /**
     * 构造事件
     */
    public ApplicationEvent(Object source) {
        super(source);
    }

}
