package bdbk.springframework.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * 注入值的注解
 * @author little8
 * @since 2022-06-22
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    /**
     * The actual value expression: e.g. "#{systemProperties.myProp}".
     */
    String value();

}
