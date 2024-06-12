package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for adding a chat message.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * specifically for adding a chat message to the game client's model.
 *
 */
public class addChatResponse extends ResponseMessage {
    int idx;
    ChatMessage chat_message;

    public addChatResponse(int idx, ChatMessage chat_message) {
        this.idx = idx;
        this.chat_message = chat_message;
    }

    /**
     * Updates the client-side model with the received chat message.
     *
     * This method delegates the chat message addition to the client's miniModel object.
     */
    @Override
    public void action() {
        super.client.miniModel.addChat(idx, chat_message);;
    }

}
