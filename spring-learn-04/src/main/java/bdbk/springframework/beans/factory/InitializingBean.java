package bdbk.springframework.beans.factory;

/**
 * bean 处理了属性填充后的扩展接口
 * @author little8
 * @since 2022-06-12
 */
public interface InitializingBean {

    /**
     * bean 处理了属性填充后调用
     */
    void afterPropertiesSet() throws Exception;

}
