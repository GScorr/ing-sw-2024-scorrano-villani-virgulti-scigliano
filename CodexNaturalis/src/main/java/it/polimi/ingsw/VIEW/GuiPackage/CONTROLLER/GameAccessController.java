package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;

/**
 * This class implements the controller logic for the game access scene. It handles user interaction for creating a new game.
 */
public class GameAccessController extends GenericSceneController {

    public Label titleLabel_0;
    public Label titleLabel_1;
    public ImageView backgroundImage;
    @FXML
    private TextField gameNameField;

    @FXML
    private ComboBox<String> playerNumberComboBox;

    @FXML
    private Button createGameButton;


    public void initialize() {

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

    /**
     * Handles the click event on the create game button. It retrieves the game name and player number
     * from the UI elements, validates the input, and attempts to create the game using the client object.
     * If successful, it shows a success message and waits for the game to be full.
     *
     * @param event The ActionEvent triggered by clicking the create game button.
     * @throws NotBoundException If the client object is not properly bound.
     * @throws IOException If there's an I/O error during communication.
     * @throws ClassNotFoundException If a class related to game creation cannot be found.
     * @throws InterruptedException If the waiting for the game to be full is interrupted.
     */
    @FXML
    private void handleCreateGameButtonAction(ActionEvent event) throws NotBoundException, IOException, ClassNotFoundException, InterruptedException {
        String gameName = gameNameField.getText();
        String playerNumber = playerNumberComboBox.getValue();

        if (gameName == null || gameName.isEmpty()) {
            showAlert("Game Creation Error", "Please enter a game name.");
            return;
        }

        if (playerNumber == null || playerNumber.isEmpty()) {
            showAlert("Game Creation Error", "Please select the number of players.");
            return;
        }

        // Logic to handle game creation with the provided game name and number of players
        client.createGame(gameName,Integer.parseInt(playerNumber), client.getTerminal_interface().getName());
        super.scene_controller.showMessage("Success", "YOUR PLAYER HAS BEEN CREATED!");
        super.client.getTerminal_interface().waitFullGame();
        // Add your game creation handling code here
    }

    /**
     * Shows an alert dialog with the provided title and content.
     *
     * @param title The title of the alert dialog.
     * @param content The content message displayed in the alert dialog.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
