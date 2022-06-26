package bdbk.springframework.beans.factory;

/**
 * 实现此接口，能感知到所属的 bean
 * @author little8
 * @since 2022-06-12
 */
public interface BeanNameAware extends Aware {

    void setBeanName(String name);

}

