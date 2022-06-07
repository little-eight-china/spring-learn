package bdbk.springframework.beans;

/**
 * bean属性类
 * @author little8
 * @since 2022-06-05
 */
public class PropertyValue {

    private final String name;

    private final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

}
