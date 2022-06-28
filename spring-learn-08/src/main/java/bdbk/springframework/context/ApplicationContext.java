package bdbk.springframework.context;

import bdbk.springframework.beans.factory.support.ListableBeanFactory;

/**
 * 应用上下文接口
 * @author little8
 * @since 2022-06-13
 */
public interface ApplicationContext extends ListableBeanFactory, ApplicationEventPublisher {
}
