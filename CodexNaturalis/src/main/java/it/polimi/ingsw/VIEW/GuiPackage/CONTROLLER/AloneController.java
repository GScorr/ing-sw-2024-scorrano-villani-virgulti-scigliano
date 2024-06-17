package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;


import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.rmi.NotBoundException;

/**
 * LoginController handles user login functionality and scene transition to the lobby.
 *
 */
public class AloneController extends GenericSceneController {
    @FXML
    private Label messageLabel;

    @Override
    public void updateMessageServer(String message) {
        messageLabel.setText(message);
    }
}
