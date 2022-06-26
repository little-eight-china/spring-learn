package bdbk.springframework.test;
import bdbk.springframework.context.support.ClassPathXmlApplicationContext;
import bdbk.springframework.test.bean.UserDao;
import bdbk.springframework.test.bean.UserService;
import org.junit.Assert;
import org.junit.Test;

public class ApiTest {

    /**
     * 测试aop
     */
    @Test
    public void test_aop(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        // 获取userService、userDao
        UserService userService = applicationContext.getBean("userService", UserService.class);
        UserDao userDao = applicationContext.getBean("userDao", UserDao.class);
        userService.queryUserInfo();
    }
}
