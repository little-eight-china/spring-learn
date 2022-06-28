package bdbk.springframework.test.aop;

import bdbk.springframework.aop.MethodBeforeAdvice;
import bdbk.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class UserServiceBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("拦截方法：" + method.getName());
    }

}
