package bdbk.springframework.test;

import bdbk.springframework.beans.PropertyValue;
import bdbk.springframework.beans.PropertyValues;
import bdbk.springframework.beans.factory.config.BeanDefinition;
import bdbk.springframework.beans.factory.config.BeanReference;
import bdbk.springframework.beans.factory.support.DefaultListableBeanFactory;
import bdbk.springframework.test.bean.UserDao;
import bdbk.springframework.test.bean.UserService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class ApiTest {

    /**
     * 测试通过原生Constructor方法填充属性
     */

    @Test
    public void test_Constructor(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. UserService 注入bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 3.携带参数的去获取bean
        UserService userService = (UserService) beanFactory.getBean("userService", 12, "tony", new UserDao("userDao"));
        userService.queryUserInfo();
    }

    /**
     * 测试单例通过非原生Constructor方法填充属性
     */
    @Test
    public void test_NotConstructor(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. UserDao设置属性[name]、 注册
        PropertyValues daoPropertyValues = new PropertyValues();
        daoPropertyValues.addPropertyValue(new PropertyValue("name", "userDao"));
        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class, daoPropertyValues));

        // 3.UserService设置属性[age, name]、注册
        PropertyValues servicePropertyValues = new PropertyValues();
        servicePropertyValues.addPropertyValue(new PropertyValue("age", 18));
        servicePropertyValues.addPropertyValue(new PropertyValue("name", "mike"));
        servicePropertyValues.addPropertyValue(new PropertyValue("userDao",new BeanReference("userDao")));
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, servicePropertyValues);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 4.获取UserService的bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
    }


    /**
     * 测试单例
     */
    @Test
    public void test_SingletonBean(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2.设置属性[age, name]
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("age", 18));
        propertyValues.addPropertyValue(new PropertyValue("name", "mike"));

        // 3. UserService 注入bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 4.获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
        assertEquals(userService, beanFactory.getBean("userService"));
    }

    /**
     * 测试原型
     */
    @Test
    public void test_PrototypeBean(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2.设置属性[age, name]
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("age", 18));
        propertyValues.addPropertyValue(new PropertyValue("name", "mike"));

        // 3. UserService 注入bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanDefinition.setScope("prototype");
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 4.获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
        assertNotEquals(userService, beanFactory.getBean("userService"));
    }

}
