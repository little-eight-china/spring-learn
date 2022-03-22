package bdbk.springframework.factory;

import bdbk.springframework.exception.BeansException;

/**
 * bean工厂
 * @author little8
 * @since 2022-03-20
 */
public interface BeanFactory {

    Object getBean(String name) throws RuntimeException, InstantiationException, IllegalAccessException;

}
