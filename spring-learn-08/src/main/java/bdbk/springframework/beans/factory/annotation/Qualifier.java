package bdbk.springframework.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * 指定注解注入的bean的名词
 * @author little8
 * @since 2022-06-22
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Qualifier {

    String value() default "";

}
