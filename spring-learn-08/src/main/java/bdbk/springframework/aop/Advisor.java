package bdbk.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * 增强的接口
 * @author little8
 * @since 2022-06-26
 */
public interface Advisor {

    Advice getAdvice();

}
