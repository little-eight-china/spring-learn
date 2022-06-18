package bdbk.springframework.test.bean;

import bdbk.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyBeanFactory implements FactoryBean<FactoryBeanInterface> {
    @Override
    public FactoryBeanInterface getObject() {
        InvocationHandler handler = (proxy, method, args) -> "FactoryBeanInterface的getFactoryBeanName被代理了";
        return (FactoryBeanInterface) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(), new Class[]{FactoryBeanInterface.class}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return FactoryBeanInterface.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
