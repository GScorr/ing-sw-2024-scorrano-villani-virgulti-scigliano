package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class LoginController extends GenericSceneController {

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
       // loadLobby(username);
    }

    private void loadLobby(String username) {
        try {
            // Carica la nuova scena
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Lobby.fxml"));
            Parent root = loader.load();

            LobbyController lobbyController = loader.getController();
            lobbyController.setUsername(username);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1024, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
