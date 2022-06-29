package bdbk.springframework.beans.factory;

import bdbk.springframework.beans.exception.BeansException;

/**
 * 实例工厂
 * @author little8
 * @since 2022-06-28
 */
public interface ObjectFactory<T> {

    T getObject() throws BeansException;

}
