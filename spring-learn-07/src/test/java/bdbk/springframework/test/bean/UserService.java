package bdbk.springframework.test.bean;

import bdbk.springframework.context.annotation.Scope;
import bdbk.springframework.stereotype.Component;

/**
 *
 * @author little8
 * @since 2022-03-20
 */
@Component
@Scope(value = "prototype")
public class UserService implements UserServiceIntf {

    public UserService() {
    }

    @Override
    public void query1(){
        System.out.println("UserService查询1");
    }

    @Override
    public void query2(){
        System.out.println("UserService查询2");
    }
}
