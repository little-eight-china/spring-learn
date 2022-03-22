package bdbk.springframework.test;

import bdbk.springframework.factory.config.BeanDefinition;
import bdbk.springframework.factory.support.DefaultListableBeanFactory;
import bdbk.springframework.test.bean.UserService;
import org.junit.Test;

/**
 * 
 * @author little8
 * @since 2022-03-20
 */
public class ApiTest {

    @Test
    public void test_BeanFactory(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2.注册bean
        BeanDefinition beanDefinition = new BeanDefinition(new UserService().getClass());
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 3.获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
    }

}
