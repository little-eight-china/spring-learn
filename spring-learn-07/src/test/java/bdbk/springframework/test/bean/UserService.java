package bdbk.springframework.test.bean;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.*;
import bdbk.springframework.beans.factory.annotation.Autowired;
import bdbk.springframework.beans.factory.annotation.Value;
import bdbk.springframework.context.annotation.Scope;
import bdbk.springframework.stereotype.Component;

/**
 *
 * @author little8
 * @since 2022-03-20
 */
@Component
@Scope(value = "prototype")
public class UserService {

    @Value("${age}")
    private int age;
    @Value("${name}")
    private String name;
    @Autowired
    private UserDao userDao;

    public UserService() {
    }

    public UserService(int age, String name, UserDao userDao) {
        this.age = age;
        this.name = name;
        this.userDao = userDao;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void queryUserInfo(){
        if (getUserDao() == null) {
            System.out.printf("查询用户信息：年龄: [%d], 名字: [%s]", getAge(), getName());
        } else {
            System.out.printf("查询用户信息：年龄: [%d], 名字: [%s]，dao：[%s]%n", getAge(), getName(), getUserDao().toString());
        }
    }
}
