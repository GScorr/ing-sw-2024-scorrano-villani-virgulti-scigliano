
package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * LobbyController handles UI elements and scene transition for the lobby scene.
 */
public class LobbyController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Button partitaProva;

    /**
     * Sets the username displayed in the lobby scene.
     *
     * @param username The username to be displayed.
     */
    public void setUsername(String username) {
        usernameLabel.setText("Welcome, " + username + "!");
    }

    /**
     * Handles the click on the game button, transitioning to the game scene.
     *
     * @throws IOException If an error occurs while loading the game scene FXML.
     */
    public void handleGameButtonAction() {
        try {
            // load new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
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
