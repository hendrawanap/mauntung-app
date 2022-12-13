package com.mauntung.mauntung.domain.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MessageBuilder {
    private final List<String> messages;

    public MessageBuilder() {
        messages = new LinkedList<>();
    }

    public void append(String message) {
        messages.add(message);
    }

    public int length() {
        return messages.size();
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    @Override
    public String toString() {
        Optional<String> messages = this.messages.stream().reduce((message, acc) -> acc + ". " + message);
        if (messages.isEmpty()) return "";
        return messages.get();
    }
}
