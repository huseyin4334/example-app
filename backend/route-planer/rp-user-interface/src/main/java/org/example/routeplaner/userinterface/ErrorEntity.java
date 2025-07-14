package org.example.routeplaner.userinterface;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ErrorEntity {
    private List<String> message = new ArrayList<>();

    public void addMessage(String message) {
        this.message.add(message);
    }
}
