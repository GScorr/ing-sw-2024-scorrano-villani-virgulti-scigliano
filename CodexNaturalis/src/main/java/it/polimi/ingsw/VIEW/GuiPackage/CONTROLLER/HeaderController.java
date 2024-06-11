package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.RMI_FINAL.ChatIndexManager;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class HeaderController extends GenericSceneController {

    @FXML
    private VBox chatBox;
    @FXML
    private Menu chatsMenu;
    private ChatIndexManager chat_manager = new ChatIndexManager();
    private VirtualViewF the_client;
    GenericSceneController upper_controller ;

    public void setUpper_controller(GenericSceneController upper_controller) {
        this.upper_controller = upper_controller;
    }

    public void setThe_client(VirtualViewF the_client) {
        this.the_client = the_client;
    }

    public void  startInitializeHeader() throws IOException {

        int i=1;
        if(this.the_client.getMiniModel().getNum_players() > 2){
            for( i = 1; i<=this.the_client.getMiniModel().getNum_players(); ++i){
                if( i!=this.the_client.getMiniModel().getMy_index() ) addChatItem("-CHAT WITH PLAYER " + this.the_client.getMiniModel().getNum_to_player().get(i) + " - New messages (" + this.the_client.getMiniModel().getNot_read().get(chat_manager.getChatIndex(this.the_client.getMiniModel().getMy_index(),i)) + ")", i);}
            addChatItem("PUBLIC CHAT" + " - New messages (" + this.the_client.getMiniModel().getNot_read().get(6) + ")", i);
        }else{ addChatItem("PUBLIC CHAT" + " - New messages (" + this.the_client.getMiniModel().getNot_read().get(6) + ")", i);
        }
    }

    private void addMessageToChatBox(String message) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        chatBox.getChildren().add(messageLabel);
    }


    // Aggiunge un nuovo chat item al menu "Chats"
    public void addChatItem(String chatName, int chatId) {
        MenuItem chatItem = new MenuItem(chatName);
        chatItem.setUserData(chatId);
        chatItem.setOnAction(event -> {
            try {
                showChat(chatName, chatId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        chatsMenu.getItems().add(chatItem);
    }

    // Mostra un popup quando un chat item è premuto
    private void showChat(String chatName, int chatId) throws IOException {

        chatBox.getChildren().clear();  //
        chatBox.setVisible(true);
        System.out.println("chat is now visible: " + chatBox.isVisible());

        if (this.the_client.getMiniModel().getNum_players() != 2) {
            if (chatId == this.the_client.getMiniModel().getNum_players() + 1) {
                this.the_client.getMiniModel().getNot_read().set(6, 0);
                updateUnreadTotal();
                scene_controller.showChat("Chat", 6, this.the_client, chatId);
            } else {
                this.the_client.getMiniModel().getNot_read().set(chat_manager.getChatIndex(this.the_client.getMiniModel().getMy_index(), chatId), 0);
                updateUnreadTotal();
                scene_controller.showChat("Chat", chat_manager.getChatIndex(this.the_client.getMiniModel().getMy_index(), chatId), this.the_client, chatId);
            }
        } else {
            this.the_client.getMiniModel().getNot_read().set(6, 0);
            updateUnreadTotal();
            scene_controller.showChat("Chat", 6, this.the_client, chatId);

        }
    }

    private void updateUnreadTotal() throws IOException {
        this.the_client.getMiniModel().setUnread_total(0);
        for (Integer i : this.the_client.getMiniModel().getNot_read()) {
            this.the_client.getMiniModel().setUnread_total(this.the_client.getMiniModel().getUnread_total() + i);
        }
    }
}
