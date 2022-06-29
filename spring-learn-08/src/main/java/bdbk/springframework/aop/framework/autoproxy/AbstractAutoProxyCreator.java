package bdbk.springframework.aop.framework.autoproxy;

import bdbk.springframework.aop.*;
import bdbk.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import bdbk.springframework.aop.framework.ProxyFactory;
import bdbk.springframework.beans.PropertyValues;
import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.BeanFactory;
import bdbk.springframework.beans.factory.BeanFactoryAware;
import bdbk.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import bdbk.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 自动创建的代理对象的抽象类
 * @author little_eight
 * @since 2022/6/26
 */
public abstract class AbstractAutoProxyCreator implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    private final Set<Object> earlyProxyReferences = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }


    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

        if (isInfrastructureClass(beanClass)) return null;

        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (!classFilter.matches(beanClass)) continue;

            ProxyFactory proxyFactory = new ProxyFactory();

            TargetSource targetSource = null;
            try {
                targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            proxyFactory.setTargetSource(targetSource);
            proxyFactory.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            proxyFactory.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            proxyFactory.setProxyTargetClass(false);

            return proxyFactory.getProxy();

        }

        return null;
    }

    public boolean postProcessAfterInstantiation(Object bean, String beanName) {
        return true;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }


    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!earlyProxyReferences.contains(beanName)) {
            return wrapIfNecessary(bean);
        }
        return bean;
    }

    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        if (!earlyProxyReferences.contains(beanName)) {
            earlyProxyReferences.add(beanName);
        }
        return wrapIfNecessary(bean);
    }

    protected Object wrapIfNecessary(Object bean) {
        if (isInfrastructureClass(bean.getClass())) return bean;

        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            // 过滤匹配类
            if (!classFilter.matches(bean.getClass())) continue;

            ProxyFactory proxyFactory = new ProxyFactory();

            TargetSource targetSource = new TargetSource(bean);
            proxyFactory.setTargetSource(targetSource);
            proxyFactory.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            proxyFactory.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            proxyFactory.setProxyTargetClass(true);

            // 返回代理对象
            return proxyFactory.getProxy();
        }

        return bean;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
    }
}
