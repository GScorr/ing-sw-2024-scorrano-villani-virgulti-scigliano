package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class implements the controller for a chat window in the game's GUI.
 * It manages displaying chat messages, sending new messages, and updating the chat in real-time.
 */
public class ChatController extends GenericSceneController {

    private int decision;

    private volatile boolean running; // add a flag to control the thread

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

    /**
     * the followers method are used to manage chat message
     *
     */
    public void addMessage(String message) {
        chatMessagesListView.getItems().add(message);
    }

    public String getMessageInput() {
        return messageInputField.getText();
    }

    public void clearMessageInput() {
        messageInputField.clear();
    }

    /**
     * Handles closing the chat window.
     * Stops the chat updater thread and sets the chat open flag in the header to false.
     */
    @FXML
    private void handleClose() {
        running = false;
        stage.close();
        header.setChatOpen(false);
    }

    /**
     * Initializes the chat window by clearing the message list and populating it with existing messages.
     * Starts the chat updater thread that periodically checks for new messages.
     * Sets up an action listener for the message input field to handle sending messages.
     *
     * @throws IOException If there's an error during communication with the client.
     */
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

    /**
     * Adds a new chat message to the chat window.
     *
     * @param chatMessage The ChatMessage object containing message details.
     */
    public void addMessageToChat(ChatMessage chatMessage) {
        addMessage(chatMessage.player.getName() + ": " + chatMessage.message);
    }

    /**
     * Handles sending a new chat message.
     * Retrieves the message from the input field, checks if it's empty, and sends it to the client using the `ChatChoice` method.
     * Clears the input field after sending the message.
     *
     * @throws IOException If there's an error during communication with the client.
     */
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

    /**
     * Starts a background thread to periodically update the chat with new messages.
     * The thread fetches new messages, resets the unread message count for this chat window, and sleeps for a second before repeating.
     */
    public void startChatUpdater() {
        running = true;
        Thread chatUpdater = new Thread(() -> {
            while (running) {
                try {
                    Platform.runLater(this::updateChat);
                    client.getMiniModel().getNot_read().set(idx, 0);
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

    /**
     * Updates the chat window by clearing the message list and refetching messages from the client.
     */
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
