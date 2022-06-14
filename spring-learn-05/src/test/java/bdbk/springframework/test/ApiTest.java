package bdbk.springframework.test;
import bdbk.springframework.context.support.ClassPathXmlApplicationContext;
import bdbk.springframework.test.event.CustomEvent;
import org.junit.Test;

public class ApiTest {

    /**
     * 测试扩展类的输出日志,在这期间我改了name的名字为tony
     */
    @Test
    public void test_support(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, "监听成功了！"));

        // 销毁方法
        applicationContext.registerShutdownHook();
    }
}
