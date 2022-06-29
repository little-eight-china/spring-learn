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
public class UserService2 {
    public String name = "UserService2";
    @Autowired
    public UserService userService;

    public UserService getService() {
        return userService;
    }
}
