package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;

/**
 * WaitingSceneController manages the waiting scene UI and monitors game state changes in the background.
 * Inherits from GenericSceneController.
 */
public class WaitingSceneController extends GenericSceneController{

    public ImageView backgroundImage;
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


        // Set the background image
        File file = new File("src/resources/BackGroundImaging/BackGround.png");
        Image image = new Image(file.toURI().toString());
        backgroundImage.setImage(image);

        // Bind the background image size to the scene size
        backgroundImage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                backgroundImage.fitHeightProperty().bind(newScene.heightProperty());
                backgroundImage.fitWidthProperty().bind(newScene.widthProperty());
            }
        });
    }

    @Override
    public void startInitialize() throws IOException, ClassNotFoundException, InterruptedException {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (!client.getMiniModel().getState().equals("NOT_INITIALIZED") && !client.getMiniModel().getState().equals("NOT_IN_A_GAME")) {
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
