package bdbk.springframework.aop.framework.autoproxy;

import bdbk.springframework.aop.*;
import bdbk.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import bdbk.springframework.aop.framework.ProxyFactory;
import bdbk.springframework.beans.PropertyValues;
import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.BeanFactory;
import bdbk.springframework.beans.factory.BeanFactoryAware;
import bdbk.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import bdbk.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;

/**
 * 自动创建的代理对象的抽象类
 * @author little_eight
 * @since 2022/6/26
 */
public abstract class AbstractAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

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

        return bean;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
    }
}
