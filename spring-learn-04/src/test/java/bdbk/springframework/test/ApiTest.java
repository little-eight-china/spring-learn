package bdbk.springframework.test;

import bdbk.springframework.beans.PropertyValue;
import bdbk.springframework.beans.PropertyValues;
import bdbk.springframework.beans.factory.config.BeanDefinition;
import bdbk.springframework.beans.factory.support.DefaultListableBeanFactory;
import bdbk.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import bdbk.springframework.test.bean.UserService;
import bdbk.springframework.test.support.MyBeanPostProcessor;
import bdbk.springframework.test.support.MyInstantiationAwareBeanPostProcessor;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ApiTest {

    /**
     * 测试扩展类的输出日志,在这期间我改了name的名字为tony
     */
    @Test
    public void test_support(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. 读取配置文件&注册Bean
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        // 3、扩展类的注册
        MyInstantiationAwareBeanPostProcessor myInstantiationAwareBeanPostProcessor = new MyInstantiationAwareBeanPostProcessor();
        MyBeanPostProcessor beanPostProcessor = new MyBeanPostProcessor();
        beanFactory.addBeanPostProcessor(myInstantiationAwareBeanPostProcessor);
        beanFactory.addBeanPostProcessor(beanPostProcessor);

        // 3. 获取Bean对象调用方法
        UserService userService = (UserService) beanFactory.getBean("userService", UserService.class);
        userService.queryUserInfo();
        Assert.assertEquals("tony", userService.getName());
    }
}
