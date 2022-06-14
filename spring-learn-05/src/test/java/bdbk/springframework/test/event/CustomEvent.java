package bdbk.springframework.test.event;

import bdbk.springframework.context.event.ApplicationContextEvent;

public class CustomEvent extends ApplicationContextEvent {

    private String message;

    public CustomEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
