package bdbk.springframework.aop;

/**
 * 切入点匹配的类的接口
 * @author little8
 * @since 2022-06-26
 */
public interface ClassFilter {

    boolean matches(Class<?> clazz);

}
