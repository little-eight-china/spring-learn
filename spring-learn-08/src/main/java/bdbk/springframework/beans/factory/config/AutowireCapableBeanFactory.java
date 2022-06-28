package bdbk.springframework.beans.factory.config;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.BeanFactory;

/**
 * 在BeanFactory基础上实现了对存在实例的管理
 * @author little8
 * @since 2022-06-09
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 执行 BeanPostProcessors 接口实现类的 postProcessBeforeInitialization 方法
     *
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    /**
     * 执行 BeanPostProcessors 接口实现类的 postProcessorsAfterInitialization 方法
     *
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;

}
