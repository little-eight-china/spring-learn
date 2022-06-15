package bdbk.springframework.test.support;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.config.BeanPostProcessor;
import bdbk.springframework.test.bean.UserService;

public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("userService".equals(beanName)) {
            UserService userService = (UserService) bean;
            userService.setName("tony");
        }

        System.out.println(String.format("[%s]7、BeanPostProcessor -- postProcessBeforeInitialization%n", beanName));
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(String.format("[%s]10、BeanPostProcessor -- postProcessAfterInitialization", beanName));
        return bean;
    }

}
