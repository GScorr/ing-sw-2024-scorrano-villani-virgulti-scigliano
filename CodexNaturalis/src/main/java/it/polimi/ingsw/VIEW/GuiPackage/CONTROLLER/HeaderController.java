package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.RMI_FINAL.ChatIndexManager;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
    ColorCoordinatesHelper helper;

    private Stage scoreboardStage;

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

    @FXML
    private void showScoreboard() throws IOException {
        // Check if the scoreboard popup is already open
        if (scoreboardStage != null && scoreboardStage.isShowing()) {
            return;
        }

        scoreboardStage = new Stage();
        scoreboardStage.initModality(Modality.NONE); // Make the window non-blocking
        scoreboardStage.setTitle("Scoreboard");
        scoreboardStage.setResizable(false); // Make the window non-resizable

        // Set the owner of the popup stage to the main stage
        scoreboardStage.initOwner(scene_controller.getActiveScene().getWindow()); // Adjust according to how you access the main stage

        File file = new File("src/resources/imgMirk/scoreTable.png");
        Image image = new Image(file.toURI().toString());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(315); // Max width
        imageView.setFitHeight(630); // Max height
        imageView.setPreserveRatio(true);

        ColorCoordinatesHelper helper = new ColorCoordinatesHelper();
        List<CircleData> circles = new ArrayList<>();
        for (int i = 0; i < the_client.getMiniModel().getNum_players(); i++) {
            GameField g = the_client.getMiniModel().getGame_fields().get(i);
            System.out.println(helper.fromEnumtoColor(g.getPlayer().getColor()) + " " + helper.fromPointstoX(g.getPlayer(), g.getPlayer().getColor()) + " " + helper.fromPointstoY(g.getPlayer(), g.getPlayer().getColor()));
            circles.add(new CircleData(
                    helper.fromPointstoX(g.getPlayer(), g.getPlayer().getColor()),
                    helper.fromPointstoY(g.getPlayer(), g.getPlayer().getColor()),
                    10,
                    helper.fromEnumtoColor(g.getPlayer().getColor())
            ));
        }

        Pane circlesPane = new Pane();
        for (CircleData data : circles) {
            Circle circle = new Circle(data.x, data.y, data.radius);
            circle.setFill(data.color);
            circlesPane.getChildren().add(circle);
        }

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(imageView, circlesPane);

        VBox vbox = new VBox(stackPane);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Scene scene = new Scene(vbox, 315, 630); // Set the scene size to match the max dimensions of the image
        scoreboardStage.setScene(scene);
        scoreboardStage.show();

        // Set the event handler to nullify the stage reference when the popup is closed
        scoreboardStage.setOnHidden(event -> scoreboardStage = null);
    }

    public void handleDisconnect(ActionEvent actionEvent) {
       // Platform.exit();
    }


    private static class CircleData {
        double x, y, radius;
        Color color;

        CircleData(double x, double y, double radius, Color color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }
    }
}
