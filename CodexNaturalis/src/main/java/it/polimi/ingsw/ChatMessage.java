package it.polimi.ingsw;

import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    String message;
    Player player;


    public ChatMessage(String message) {
        this.message = message;
    }
}
