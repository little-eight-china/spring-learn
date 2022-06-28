package bdbk.springframework.aop.aspectj;

import bdbk.springframework.aop.Pointcut;
import bdbk.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

/**
 * 根据表达式获取切入点的通知器
 * @author little8
 * @since 2022-06-26
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    private AspectJExpressionPointcut pointcut;

    private Advice advice;

    private String expression;

    public void setExpression(String expression){
        this.expression = expression;
    }

    @Override
    public Pointcut getPointcut() {
        if (null == pointcut) {
            pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice){
        this.advice = advice;
    }

}
