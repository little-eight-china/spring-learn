package bdbk.springframework.test;

import bdbk.springframework.beans.PropertyValue;
import bdbk.springframework.beans.PropertyValues;
import bdbk.springframework.beans.factory.config.BeanDefinition;
import bdbk.springframework.beans.factory.support.DefaultListableBeanFactory;
import bdbk.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import bdbk.springframework.test.bean.UserService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 *
 * @author little8
 * @since 2022-03-20
 */
public class ApiTest {

    /**
     * 测试单例填充属性
     */
    @Test
    public void test_readXmlFile(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. 读取配置文件&注册Bean
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        // 3. 获取Bean对象调用方法
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
    }
}
