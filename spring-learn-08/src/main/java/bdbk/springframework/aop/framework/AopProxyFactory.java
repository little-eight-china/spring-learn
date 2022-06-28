package bdbk.springframework.aop.framework;

import bdbk.springframework.aop.AdvisedSupport;

/**
 * 创建代理对象工厂
 * @author little_eight
 * @since 2022/6/27
 */
public interface AopProxyFactory {
    AopProxy createAopProxy(AdvisedSupport advisedSupport);
}
