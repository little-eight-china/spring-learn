package bdbk.springframework.exception;

/**
 *  bean 异常
 * @author little8
 * @since 2022-03-20
 */
public class BeansException extends RuntimeException {

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
