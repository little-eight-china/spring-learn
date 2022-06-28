package bdbk.springframework.aop;

import java.lang.reflect.Method;

/**
 * 切入点匹配的方法的接口
 * @author little8
 * @since 2022-06-26
 */
public interface MethodMatcher {

    boolean matches(Method method, Class<?> targetClass);

}
