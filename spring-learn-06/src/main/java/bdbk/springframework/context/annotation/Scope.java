package bdbk.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * bean注入注解
 * @author little8
 * @since 2022-06-21
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    String value() default "singleton";

}
