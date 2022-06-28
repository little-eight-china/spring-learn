package bdbk.springframework.aop.framework.autoproxy;

import bdbk.springframework.beans.factory.BeanNameAware;

/**
 * 默认的自动创建通知器的代理对象的实现类
 * @author little8
 * @since 2022-06-26
 */
public class DefaultAdvisorAutoProxyCreator extends AbstractAdvisorAutoProxyCreator implements BeanNameAware {

    public static final String SEPARATOR = ".";
    private boolean usePrefix = false;
    private String advisorBeanNamePrefix;

    public DefaultAdvisorAutoProxyCreator() {
    }

    public void setUsePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix;
    }

    public boolean isUsePrefix() {
        return this.usePrefix;
    }

    public void setAdvisorBeanNamePrefix(String advisorBeanNamePrefix) {
        this.advisorBeanNamePrefix = advisorBeanNamePrefix;
    }

    public String getAdvisorBeanNamePrefix() {
        return this.advisorBeanNamePrefix;
    }

    public void setBeanName(String name) {
        if (this.advisorBeanNamePrefix == null) {
            this.advisorBeanNamePrefix = name + ".";
        }

    }

    protected boolean isEligibleAdvisorBean(String beanName) {
        return !this.isUsePrefix() || beanName.startsWith(this.getAdvisorBeanNamePrefix());
    }
}
