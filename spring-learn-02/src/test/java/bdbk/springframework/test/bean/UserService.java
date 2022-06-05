package bdbk.springframework.test.bean;

/**
 *
 * @author little8
 * @since 2022-03-20
 */
public class UserService {


    private int age;
    private String name;

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

    public void queryUserInfo(){
        System.out.println(String.format("查询用户信息：年龄: [%d], 名字: [%s]", getAge(), getName()));
    }

}
