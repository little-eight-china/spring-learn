package bdbk.springframework.factory;

import bdbk.springframework.exception.BeansException;

/**
 * bean工厂接口
 * @author little8
 * @since 2022-03-20
 */
public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;

}
