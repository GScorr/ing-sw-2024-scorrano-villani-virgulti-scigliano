package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class addChatResponse extends ResponseMessage {
    int idx;
    ChatMessage chat_message;

    public addChatResponse(int idx, ChatMessage chat_message) {
        this.idx = idx;
        this.chat_message = chat_message;
    }


    @Override
    public void action() {
        super.client.miniModel.addChat(idx, chat_message);;
    }
}
