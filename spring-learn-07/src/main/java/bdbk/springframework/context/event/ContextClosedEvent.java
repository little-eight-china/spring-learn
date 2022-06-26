package bdbk.springframework.context.event;

/**
 * xml文件应用上下文，这是用户使用的一个门户起点
 * @author little8
 * @since 2022-06-14
 */
public class ContextClosedEvent extends ApplicationContextEvent{

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextClosedEvent(Object source) {
        super(source);
    }

}
