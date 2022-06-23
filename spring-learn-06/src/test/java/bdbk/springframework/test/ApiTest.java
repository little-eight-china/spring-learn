package bdbk.springframework.test;
import bdbk.springframework.context.support.ClassPathXmlApplicationContext;
import bdbk.springframework.test.bean.UserDao;
import bdbk.springframework.test.bean.UserService;
import org.junit.Assert;
import org.junit.Test;

public class ApiTest {

    /**
     * 测试注解注册bean,在这期间我读取properties文件修改name的值为tony
     */
    @Test
    public void test_annotation(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        // 获取userService、userDao
        UserService userService = applicationContext.getBean("userService", UserService.class);
        UserDao userDao = applicationContext.getBean("userDao", UserDao.class);
        userService.queryUserInfo();
        // 作用域是prototype，所以应该不相等
        Assert.assertNotEquals(userService, applicationContext.getBean("userService", UserService.class));
        // 作用域是默认的singleton，所以应该相等
        Assert.assertEquals(userDao, applicationContext.getBean("userDao", UserDao.class));
    }
}
