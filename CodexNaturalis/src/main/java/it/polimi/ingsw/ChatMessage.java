package it.polimi.ingsw;

import it.polimi.ingsw.MODEL.Player.Player;
import java.io.Serializable;

/**
 * This class represents a single message exchanged in a chat.
 *
 */
public class ChatMessage implements Serializable {

    public String message;
    public Player player;

    public ChatMessage(String message) {
        this.message = message;
    }

    public ChatMessage(String message, Player player) {
        this.message = message;
        this.player = player;
    }

}
