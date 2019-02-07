package com.example.psyyf2.parent.Adapter;

import java.util.Date;

/**
 * Created by moiravan on 2018/4/14.
 */

public class ChatMessage {
    String message;
    String name;
    private long time;


    public ChatMessage() {
    }

    public ChatMessage(String name, String message) {
        this.message = message;
        this.name = name;
        time = new Date().getTime();

    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }
}
