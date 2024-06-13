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

/**
 * This class implements the controller for the "Choose Goal" scene in the game's GUI.
 * It allows the player to select their desired goal card from two options.
 *
 */
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

    /**
     * Initializes the scene by:
     *  - Loading the header scene and setting its controller properties.
     *  - Loading images for the two goal cards.
     *  - If the goal card hasn't been placed yet (by the server), shows a buffering message and starts checking the client state.
     *
     * @throws IOException If there's an error loading FXML or images.
     * @throws ClassNotFoundException If there's a class not found exception during header initialization.
     * @throws InterruptedException If the thread checking client state is interrupted.
     */
    public void startInitialize() throws IOException, ClassNotFoundException, InterruptedException {

        /**
         * header
         */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/header.fxml"));
        Parent header = loader.load();
        HeaderController headerController = loader.getController();
        headerController.setThe_client(super.client);
        System.out.println("--------------------" + scene_controller);
        headerController.setScene(scene_controller);

        // Add the header to the desired position in the main layout
// For example, if headerInclude is an AnchorPane, you can add the header like this:

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

    /**
     * Handles clicking on the first card image.
     * Sends a message to the client indicating the chosen goal card (index 0).
     * Shows a buffering message and starts checking the client state.
     *
     * @param event The MouseEvent triggered by clicking the card.
     * @throws IOException If there's an error communicating with the client.
     * @throws InterruptedException If the thread checking client state is interrupted.
     */
    @FXML
    private void handleCard1Click(MouseEvent event) throws IOException, InterruptedException {
        System.out.println("Card 1 selected");
        client.chooseGoal(0);
        showBufferingLabel();
        checkClientState();
    }

    /**
     * Handles clicking on the second card image.
     * Sends a message to the client indicating the chosen goal card (index 1).
     * Shows a buffering message and starts checking the client state.
     *
     * @param event The MouseEvent triggered by clicking the card.
     * @throws IOException If there's an error communicating with the client.
     * @throws InterruptedException If the thread checking client state is interrupted.
     */
    @FXML
    private void handleCard2Click(MouseEvent event) throws IOException, InterruptedException {
        System.out.println("Card 2 selected");
        client.chooseGoal(1);
        showBufferingLabel();
        checkClientState();
    }

    /**
     * Shows a "Buffering... Please wait." message at the bottom of the scene and disables the card image views.
     * Also starts an animation that changes the message text slightly every second.
     */
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

    /**
     * Hides the buffering message and stops the animation.
     */
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

    /**
     * Starts a background thread to periodically check the client state.
     * The thread keeps checking if the game state is still "CHOOSE_GOAL".
     * If the state changes, the thread stops the buffering animation, hides the message, and calls the `chooseStartingCardState` method of the terminal interface (likely to handle the next scene).
     */
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
            //when the state change hide the buffering label
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
            // change scene
        }).start();
    }

}