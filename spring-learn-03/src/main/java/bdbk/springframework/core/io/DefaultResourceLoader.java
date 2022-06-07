package bdbk.springframework.core.io;

import cn.hutool.core.lang.Assert;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 资源接口
 * @author little8
 * @since 2022-06-06
 */
public class DefaultResourceLoader implements ResourceLoader {

    @Override
    public Resource getResource(String location) {
        Assert.notNull(location, "Location must not be null");
        Assert.isTrue(location.startsWith(CLASSPATH_URL_PREFIX), String.format("Location must start with %s", CLASSPATH_URL_PREFIX));
        return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
    }

}
