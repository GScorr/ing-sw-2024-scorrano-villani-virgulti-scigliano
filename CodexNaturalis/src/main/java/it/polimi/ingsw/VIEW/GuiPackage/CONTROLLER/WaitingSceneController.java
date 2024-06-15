package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

/**
 * WaitingSceneController manages the waiting scene UI and monitors game state changes in the background.
 * Inherits from GenericSceneController.
 */
public class WaitingSceneController extends GenericSceneController{

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Label waitingMessage;

    @FXML
    private Button cancelButton;

    private SceneController controller;

    /**
     * Initializes the waiting scene. Starts a background thread that periodically checks
     * the game state through the client's MiniModel. Once the game state transitions out
     * of "NOT_INITIALIZED" or "NOT_IN_A_GAME", it sets the game field MiniModel and initiates
     * the goal selection process through the client interface. Handles potential exceptions
     * during communication with the server.
     */
    @FXML
    public void initialize() {
        // Start the background task to monitor the variable
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (!super.client.getMiniModel().getState().equals("NOT_INITIALIZED") && !client.getMiniModel().getState().equals("NOT_IN_A_GAME")) {
                        client.setGameFieldMiniModel();
                        client.getTerminal_interface().chooseGoalState();
                        break;
                    };
                } catch (IOException | InterruptedException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Handles the click on the "cancel" button, closing the current stage.
     *
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    private void handleCancelAction(ActionEvent event) {
        // Logic to handle the cancel action
        // For now, we simply close the stage
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Updates the waiting message label with a new message.
     *
     * @param message The new message to be displayed.
     */
    public void updateWaitingMessage(String message) {
        waitingMessage.setText(message);
    }

    public void setController (SceneController scene){
        this.controller = scene;
    }
}
