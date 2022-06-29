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
public class UserService3 {
    public String name = "UserService3";

    @Autowired
    public UserService4 userService;

    public UserService4 getService() {
        return userService;
    }
}
