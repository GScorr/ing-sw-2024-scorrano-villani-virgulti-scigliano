package it.polimi.ingsw;

import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chat implements Serializable {
    List<Player> chatters = new ArrayList<>();
    List<ChatMessage> chat = new ArrayList<>();

    public void addMessage (ChatMessage message){
        chat.add(message);
    }

    public List<Player> getChatters() {
        return chatters;
    }

    public List<ChatMessage> getChat() {
        return chat;
    }

    public void setChatters(List<Player> chatters) {
        this.chatters = chatters;
    }

    public void setChat(List<ChatMessage> chat) {
        this.chat = chat;
    }
}
