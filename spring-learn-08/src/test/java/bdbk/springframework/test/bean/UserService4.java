package bdbk.springframework.test.bean;

import bdbk.springframework.beans.factory.annotation.Autowired;
import bdbk.springframework.context.annotation.Scope;
import bdbk.springframework.stereotype.Component;

/**
 *
 * @author little8
 * @since 2022-03-20
 */
@Component
@Scope(value = "prototype")
public class UserService4 {

    public String name = "UserService4";
    @Autowired
    public UserService3 userService;

    public UserService3 getService() {
        return userService;
    }
}
