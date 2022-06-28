package bdbk.springframework.aop.framework;

import bdbk.springframework.aop.AdvisedSupport;

/**
 * 代理工厂
 * @author little8
 * @since 2022-06-26
 */
public class DefaultAopProxyFactory implements AopProxyFactory {

    public AopProxy createAopProxy(AdvisedSupport advisedSupport) {
        if (advisedSupport.isProxyTargetClass()) {
            return new Cglib2AopProxy(advisedSupport);
        }
        return new JdkDynamicAopProxy(advisedSupport);
    }
}
