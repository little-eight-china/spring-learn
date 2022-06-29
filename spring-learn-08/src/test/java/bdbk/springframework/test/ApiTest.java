package bdbk.springframework.test;
import bdbk.springframework.context.support.ClassPathXmlApplicationContext;
import bdbk.springframework.test.bean.UserService;
import bdbk.springframework.test.bean.UserService2;
import bdbk.springframework.test.bean.UserService3;
import org.junit.Assert;
import org.junit.Test;

public class ApiTest {

    /**
     * 测试循环依赖，UserService与UUserService2相互注入依赖，都是单例，所以不会抛异常
     */
    @Test
    public void test_CircularDependency(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        UserService userService = applicationContext.getBean("userService", UserService.class);
        UserService2 userService2 = applicationContext.getBean("userService2", UserService2.class);
        Assert.assertEquals(userService.userService.name, "UserService2");
        Assert.assertEquals(userService2.userService.name, "UserService");
    }

    /**
     * 测试循环依赖，UserService3与UUserService4相互注入依赖，都不是单例，所以会抛异常StackOverflowError
     */
    @Test(expected = StackOverflowError.class)
    public void test_CircularDependency_Error(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.getBean("userService3", UserService3.class);
    }
}
