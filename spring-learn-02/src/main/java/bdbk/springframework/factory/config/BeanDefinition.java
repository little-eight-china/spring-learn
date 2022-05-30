package bdbk.springframework.factory.config;

/**
 * 保存 bean 的相关信息
 * @author little8
 * @since 2022-03-20
 */

@SuppressWarnings({"rawtypes"})
public class BeanDefinition {

    private Class beanClass;

    private boolean isSingleton;

    private boolean isPrototype;

    public boolean isSingleton() {
        return isSingleton;
    }

    public void setSingleton(boolean singleton) {
        isSingleton = singleton;
    }

    public boolean isPrototype() {
        return isPrototype;
    }

    public void setPrototype(boolean prototype) {
        isPrototype = prototype;
    }

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

}
