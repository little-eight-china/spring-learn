package bdbk.springframework.beans.factory.config;

/**
 * bean属性类群
 * @author little8
 * @since 2022-06-05
 */
public class BeanReference {

    private final String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

}
