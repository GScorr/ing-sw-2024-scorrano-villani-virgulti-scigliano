package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.RMI_FINAL.ChatIndexManager;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
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

    private boolean chatOpen = false;
    @FXML
    private Menu chatsMenu;
    private ChatIndexManager chat_manager = new ChatIndexManager();
    private VirtualViewF the_client;
    GenericSceneController upper_controller ;
    SceneController scene_controller;

    public void setUpper_controller(GenericSceneController upper_controller) {
        this.upper_controller = upper_controller;
    }

    public void setThe_client(VirtualViewF the_client) {
        this.the_client = the_client;
    }

    public void startInitializeHeader() throws IOException {
        Thread menuUpdater = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    chatsMenu.getItems().clear();
                    int i = 1;
                    try {
                        if (the_client.getMiniModel().getNum_players() > 2) {
                            for (i = 1; i <= the_client.getMiniModel().getNum_players(); ++i) {
                                if (i != the_client.getMiniModel().getMy_index()) {
                                    addChatItem("-CHAT WITH PLAYER " + the_client.getMiniModel().getNum_to_player().get(i) + " - New messages (" + the_client.getMiniModel().getNot_read().get(chat_manager.getChatIndex(the_client.getMiniModel().getMy_index(), i)) + ")", i);
                                }
                            }
                            addChatItem("PUBLIC CHAT" + " - New messages (" + the_client.getMiniModel().getNot_read().get(6) + ")", i);
                        } else {
                            addChatItem("PUBLIC CHAT" + " - New messages (" + the_client.getMiniModel().getNot_read().get(6) + ")", i);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        menuUpdater.start();
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
        if(!isChatOpen()){
            chatOpen=true;
            if (this.the_client.getMiniModel().getNum_players() != 2) {
                if (chatId == this.the_client.getMiniModel().getNum_players() + 1) {
                    this.the_client.getMiniModel().getNot_read().set(6, 0);
                    updateUnreadTotal();
                    scene_controller.showChat("Chat", 6, this.the_client, chatId, this);
                } else {
                    this.the_client.getMiniModel().getNot_read().set(chat_manager.getChatIndex(this.the_client.getMiniModel().getMy_index(), chatId), 0);
                    updateUnreadTotal();
                    scene_controller.showChat("Chat", chat_manager.getChatIndex(this.the_client.getMiniModel().getMy_index(), chatId), this.the_client, chatId, this);
                }
            } else {
                this.the_client.getMiniModel().getNot_read().set(6, 0);
                updateUnreadTotal();
                scene_controller.showChat("Chat", 6, this.the_client, chatId, this);

            }
        }
        /*chatBox.getChildren().clear();  //
        chatBox.setVisible(true);
        System.out.println("chat is now visible: " + chatBox.isVisible());*/


    }

    private void updateUnreadTotal() throws IOException {
        this.the_client.getMiniModel().setUnread_total(0);
        for (Integer i : this.the_client.getMiniModel().getNot_read()) {
            this.the_client.getMiniModel().setUnread_total(this.the_client.getMiniModel().getUnread_total() + i);
        }
    }

    public void setScene(SceneController sceneController) {
        this.scene_controller=sceneController;
    }

    public void setChatOpen(boolean open) {
        this.chatOpen = open;
    }

    // Aggiorna il flag e restituisce il suo stato
    public boolean isChatOpen() {
        return chatOpen;
    }
}
