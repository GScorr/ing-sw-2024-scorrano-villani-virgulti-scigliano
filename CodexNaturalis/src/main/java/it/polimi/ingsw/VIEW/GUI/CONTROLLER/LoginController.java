package it.polimi.ingsw.view.GUI.CONTROLLER;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
       // loadLobby(username);
        loadingIndicator.setVisible(true);
        showLoginPopup("ciao", "sono il popup");

    }

    private void showLoginPopup(String title, String message) {
        // Mostra un popup di conferma del login
        Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void loadLobby(String username) {
        try {
            // Carica la nuova scena
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Lobby.fxml"));
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
