package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatController extends GenericSceneController {

    private int decision;

    private volatile boolean running; // Aggiungere un flag per controllare il thread

    @FXML
    private Label titleLabel;

    @FXML
    private ListView<String> chatMessagesListView;

    @FXML
    private TextField messageInputField;

    private Stage stage;
    private HeaderController header;

    private VirtualViewF client;
    private SceneController controller;
    private int idx;

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(event -> handleClose());
    }

    public void setClient(VirtualViewF client) {
        this.client = client;
    }

    public void setController(SceneController sceneController) {
        this.controller = sceneController;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public void addMessage(String message) {
        chatMessagesListView.getItems().add(message);
    }

    public String getMessageInput() {
        return messageInputField.getText();
    }

    public void clearMessageInput() {
        messageInputField.clear();
    }

    @FXML
    private void handleClose() {
        running = false; // Fermare il thread quando la finestra viene chiusa
        stage.close();
        header.setChatOpen(false);
    }

    public void startInitialize() throws IOException {
        chatMessagesListView.getItems().clear(); // Pulisce la lista
        for (ChatMessage chatMessage : client.getMiniModel().getChat().get(idx).getChat()) {
            addMessage(chatMessage.player.getName() + ": " + chatMessage.message);
        }
        startChatUpdater();

        messageInputField.setOnAction(event -> {
            try {
                handleSendMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void addMessageToChat(ChatMessage chatMessage) {
        addMessage(chatMessage.player.getName() + ": " + chatMessage.message);
    }

    @FXML
    private void handleSendMessage() throws IOException {
        String message = getMessageInput();
        if (!message.isEmpty()) {
            client.ChatChoice(message, decision);
            messageInputField.clear();
        }
    }

    public void setDecision(int decision) {
        this.decision = decision;
    }

    public void startChatUpdater() {
        running = true;
        Thread chatUpdater = new Thread(() -> {
            while (running) {
                try {
                    Platform.runLater(this::updateChat);
                    client.getMiniModel().getNot_read().set(idx, 0);
                    System.out.println("resetto");
                    Thread.sleep(1000); // Aggiorna la chat ogni secondo
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        chatUpdater.setDaemon(true); // Imposta il thread come daemon in modo che si chiuda automaticamente quando l'app si chiude
        chatUpdater.start();
    }

    private void updateChat() {
        chatMessagesListView.getItems().clear();
        try {
            for (ChatMessage chatMessage : client.getMiniModel().getChat().get(idx).getChat()) {
                addMessage(chatMessage.player.getName() + ": " + chatMessage.message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHeader(HeaderController header) {
        this.header = header;
    }
}
