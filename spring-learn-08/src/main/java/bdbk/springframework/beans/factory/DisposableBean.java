package bdbk.springframework.beans.factory;

/**
 * 销毁bean接口
 * @author little8
 * @since 2022-06-14
 */
public interface DisposableBean {

    void destroy() throws Exception;

}
