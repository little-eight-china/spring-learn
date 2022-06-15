package bdbk.springframework.test.bean;

import bdbk.springframework.beans.exception.BeansException;
import bdbk.springframework.beans.factory.*;

/**
 *
 * @author little8
 * @since 2022-03-20
 */
public class UserService implements BeanFactoryAware, BeanClassLoaderAware, BeanNameAware, InitializingBean {


    private int age;
    private String name;
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
            System.out.printf("查询用户信息：年龄: [%d], 名字: [%s]，dao：[%s]%n", getAge(), getName(), getUserDao().getName());
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("[userService]6、BeanFactoryAware -- setBeanFactory");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("[userService]6、BeanClassLoaderAware-- setBeanClassLoader ");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("[userService]6、BeanNameAware-- setBeanName ");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("[userService]8、InitializingBean-- afterPropertiesSet ");
    }

    public void initDataMethod() {
        System.out.println("9、init-method -- initDataMethod ");
    }

    public void destroyMethod() {
        System.out.println("[userService] 11、destroy-method -- destroyMethod ");
    }
}
