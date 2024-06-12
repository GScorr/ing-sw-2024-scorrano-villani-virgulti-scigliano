package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class ChooseGoalController extends GenericSceneController {

    @FXML
    private ImageView card1;

    @FXML
    private ImageView card2;

    @FXML
    private Label bottomLabel;

    private Timeline bufferingTimeline;

    @FXML
    private AnchorPane HeaderInclude;

    public void startInitialize() throws IOException, ClassNotFoundException, InterruptedException {

        //header
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/header.fxml"));
        Parent header = loader.load();
        HeaderController headerController = loader.getController();
        headerController.setThe_client(super.client);
        System.out.println("--------------------" + scene_controller);
        headerController.setScene(scene_controller);
        // Aggiungi l'header alla posizione desiderata nel layout principale
        // Ad esempio, se headerInclude è un AnchorPane, puoi aggiungere l'header così:
        ((AnchorPane) HeaderInclude).getChildren().add(header);
        headerController.startInitializeHeader();

        File file = new File(client.getFirstGoalCard().front_side_path);
        Image image = new Image(file.toURI().toString());
        card1.setImage(image);
        file = new File(client.getSecondGoalCard().front_side_path);
        image = new Image(file.toURI().toString());
        card2.setImage(image);
        if (! super.client.isGoalCardPlaced() ) {
            showBufferingLabel();
            checkClientState();
        }
    }

    @FXML
    private void handleCard1Click(MouseEvent event) throws IOException, InterruptedException {
        System.out.println("Card 1 selected");
        client.chooseGoal(0);
        showBufferingLabel();
        checkClientState();
    }

    @FXML
    private void handleCard2Click(MouseEvent event) throws IOException, InterruptedException {
        System.out.println("Card 2 selected");
        client.chooseGoal(1);
        showBufferingLabel();
        checkClientState();
    }

    private void showBufferingLabel() {
        Platform.runLater(() -> {
            bottomLabel.setText("Buffering... Please wait.");

            // Disable the ImageView elements
            card1.setDisable(true);
            card2.setDisable(true);

            // Initialize the dynamic buffering animation
            bufferingTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, e -> bottomLabel.setText("Buffering... Please wait")),
                    new KeyFrame(Duration.seconds(1), e -> bottomLabel.setText("Buffering. Please wait.")),
                    new KeyFrame(Duration.seconds(2), e -> bottomLabel.setText("Buffering.. Please wait.")),
                    new KeyFrame(Duration.seconds(3), e -> bottomLabel.setText("Buffering... Please wait."))
            );
            bufferingTimeline.setCycleCount(Animation.INDEFINITE);
            bufferingTimeline.play();
        });
    }

    private void hideBufferingLabel() {
        // Disable the ImageView elements
        card1.setDisable(true);
        card2.setDisable(true);
        Platform.runLater(() -> {
            // Stop the buffering animation
            if (bufferingTimeline != null) {
                bufferingTimeline.stop();
            }
            // Clear the bottom label text
            bottomLabel.setText("");
        });
    }

    private void checkClientState() {
        new Thread(() -> {
            while (true) {
                try {
                    if (!client.getMiniModel().getState().equals("CHOOSE_GOAL")) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(600); // Adjust delay as needed
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // Quando lo stato cambia, nascondi l'etichetta di buffering
            hideBufferingLabel();
            try {
                super.client.getTerminal_interface().chooseStartingCardState();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            // Cambia scena
        }).start();
    }
}