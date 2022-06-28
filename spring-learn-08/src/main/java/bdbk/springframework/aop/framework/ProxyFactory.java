package bdbk.springframework.aop.framework;

import bdbk.springframework.aop.AdvisedSupport;

/**
 * 代理工厂
 * @author little8
 * @since 2022-06-26
 */
public class ProxyFactory extends ProxyCreatorSupport {
    public ProxyFactory() {
    }

    public Object getProxy() {
        return createAopProxy().getProxy();
    }

}
