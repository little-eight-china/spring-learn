package bdbk.springframework.test;
import bdbk.springframework.context.support.ClassPathXmlApplicationContext;
import bdbk.springframework.test.bean.UserServiceIntf;
import org.junit.Test;

public class ApiTest {

    /**
     * 测试aop, 然后在spring.xml配好，针对userService的所有方法都会进行拦截
     * 如果想测试cglib的代理，可以在bdbk.springframework.aop.AdvisedSupport#setProxyTargetClass(boolean)改成true即可
     */
    @Test
    public void test_aop(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        // 获取userService（jdk代理的话，在spring.xml以及接收都得用接口UserServiceIntf，不然就会有错误）
        UserServiceIntf userService = applicationContext.getBean("userService", UserServiceIntf.class);
        userService.query1();
        userService.query2();
    }
}
