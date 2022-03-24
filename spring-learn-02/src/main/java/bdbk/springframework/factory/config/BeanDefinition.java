package bdbk.springframework.factory.config;

/**
 * 保存 bean 的相关信息
 * @author little8
 * @since 2022-03-20
 */

@SuppressWarnings({"rawtypes"})
public class BeanDefinition {

    private Class beanClass;

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
