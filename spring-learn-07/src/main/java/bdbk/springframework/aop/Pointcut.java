package bdbk.springframework.aop;

/**
 * 切入点接口
 * @author little8
 * @since 2022-06-26
 */
public interface Pointcut {

    /**
     * 返回当前切入点匹配的类
     */
    ClassFilter getClassFilter();

    /**
     * 返回当前切入点匹配的方法
     */
    MethodMatcher getMethodMatcher();

}
