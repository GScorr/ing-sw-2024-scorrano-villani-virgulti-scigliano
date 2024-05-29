package it.polimi.ingsw.view.GUI.CONTROLLER;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LobbyController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Button partitaProva;

    public void setUsername(String username) {
        usernameLabel.setText("Welcome, " + username + "!");
    }
    public void handleGameButtonAction() {
        try {
            // Carica la nuova scena
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) partitaProva.getScene().getWindow();
            stage.setScene(new Scene(root, 1024, 1000));
            stage.show();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

}
