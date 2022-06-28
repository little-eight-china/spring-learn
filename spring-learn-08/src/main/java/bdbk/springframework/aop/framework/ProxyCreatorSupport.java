package bdbk.springframework.aop.framework;

import bdbk.springframework.aop.AdvisedSupport;
import cn.hutool.core.lang.Assert;

/**
 *
 * @author little_eight
 * @since 2022/6/27
 */
public class ProxyCreatorSupport extends AdvisedSupport {
    private AopProxyFactory aopProxyFactory;

    public ProxyCreatorSupport() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    public ProxyCreatorSupport(AopProxyFactory aopProxyFactory) {
        Assert.notNull(aopProxyFactory, "AopProxyFactory must not be null");
        this.aopProxyFactory = aopProxyFactory;
    }

    public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
        Assert.notNull(aopProxyFactory, "AopProxyFactory must not be null");
        this.aopProxyFactory = aopProxyFactory;
    }

    public AopProxyFactory getAopProxyFactory() {
        return this.aopProxyFactory;
    }

    protected final synchronized AopProxy createAopProxy() {
        return this.getAopProxyFactory().createAopProxy(this);
    }

}
