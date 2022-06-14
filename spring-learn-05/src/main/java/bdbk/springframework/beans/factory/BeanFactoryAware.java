package bdbk.springframework.beans.factory;


import bdbk.springframework.beans.exception.BeansException;

/**
 * 实现此接口，能感知到所属的 beanFactory
 * @author little8
 * @since 2022-06-12
 */
public interface BeanFactoryAware extends Aware {

   void setBeanFactory(BeanFactory beanFactory) throws BeansException;

}
