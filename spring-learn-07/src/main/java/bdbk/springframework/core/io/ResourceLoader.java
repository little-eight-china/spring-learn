package bdbk.springframework.core.io;

/**
 * 资源接口，提供获取资源流的方法
 * @author little8
 * @since 2022-06-06
 */
public interface ResourceLoader {

    /**
     * Pseudo URL prefix for loading from the class path: "classpath:"
     */
    String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * 装载资源的方法接口
     * @param location 资源的路径
     */
    Resource getResource(String location);

}
