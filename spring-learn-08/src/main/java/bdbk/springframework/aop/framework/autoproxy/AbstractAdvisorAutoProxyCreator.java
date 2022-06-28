package bdbk.springframework.aop.framework.autoproxy;

import bdbk.springframework.beans.factory.BeanFactory;

/**
 * 自动创建通知器的代理对象的抽象类
 * @author little_eight
 * @since 2022/6/26
 */
public abstract class AbstractAdvisorAutoProxyCreator extends AbstractAutoProxyCreator {

    public AbstractAdvisorAutoProxyCreator() {
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
    }
}
