package com.reviewtrungtam.webapp.general.exception;

import java.util.HashSet;
import java.util.Set;

public class AppException extends RuntimeException {
    private final Set<String> messages;

    public AppException(String message) {
        super(message);
        messages = new HashSet<>();
        messages.add(message);
    }

    public AppException(Set<String> messages) {
        this.messages = messages;
    }

    public Set<String> getMessages() {
        return messages;
    }
}
