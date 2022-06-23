package bdbk.springframework.util;

/**
 * 占位符解释、SpEL计算等等的字符串处理接口
 * @author little8
 * @since 2022-06-22
 */
public interface StringValueResolver {

    String resolveStringValue(String strVal);

}
