package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.rmi.NotBoundException;

public class GameAccessController extends GenericSceneController {

    @FXML
    private TextField gameNameField;

    @FXML
    private ComboBox<String> playerNumberComboBox;

    @FXML
    private Button createGameButton;

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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
