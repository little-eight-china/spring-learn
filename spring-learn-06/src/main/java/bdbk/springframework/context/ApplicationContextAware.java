package bdbk.springframework.context;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.Aware;

/**
 * 处理aware接口
 * @author little8
 * @since 2022-06-22
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
