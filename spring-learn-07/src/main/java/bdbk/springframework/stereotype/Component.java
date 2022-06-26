package bdbk.springframework.stereotype;

import java.lang.annotation.*;

/**
 * bean注入注解
 * @author little8
 * @since 2022-06-21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    String value() default "";

}
