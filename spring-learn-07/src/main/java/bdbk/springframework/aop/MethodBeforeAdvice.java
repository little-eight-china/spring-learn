package bdbk.springframework.aop;

import java.lang.reflect.Method;

/**
 * 通知的前置的方法处理接口
 * @author little8
 * @since 2022-06-26
 */
public interface MethodBeforeAdvice extends BeforeAdvice {

    /**
     * 方法执行前的处理
     */
    void before(Method method, Object[] args, Object target) throws Throwable;

}
