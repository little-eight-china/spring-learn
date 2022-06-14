package bdbk.springframework.test;

import bdbk.springframework.beans.factory.support.DefaultListableBeanFactory;
import bdbk.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import bdbk.springframework.test.bean.UserService;
import org.junit.Test;

public class ApiTest {

    /**
     * 测试单例填充属性
     */
    @Test
    public void test_readXmlFile() {
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
