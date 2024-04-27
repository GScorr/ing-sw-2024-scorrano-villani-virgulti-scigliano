package it.polimi.ingsw.SOCKET_FINAL.Message;

import java.io.Serializable;

public class MyMessageFinal implements Serializable {
    private final String content;

    public MyMessageFinal(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

