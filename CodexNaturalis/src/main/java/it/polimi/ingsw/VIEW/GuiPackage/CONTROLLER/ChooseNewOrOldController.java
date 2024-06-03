package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER.GenericSceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.rmi.NotBoundException;

public class ChooseNewOrOldController extends GenericSceneController {

    @FXML
    private Button createNewGameButton;

    @FXML
    private Button joinExistingGameButton;

    @FXML
    private void handleCreateNewGame(ActionEvent event) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        super.client.getTerminal_interface().newGame(super.client.getTerminal_interface().getName(),true );
    }

    @FXML
    private void handleJoinExistingGame(ActionEvent event) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        super.client.getTerminal_interface().chooseMatch(super.client.getTerminal_interface().getName());
    }
}
