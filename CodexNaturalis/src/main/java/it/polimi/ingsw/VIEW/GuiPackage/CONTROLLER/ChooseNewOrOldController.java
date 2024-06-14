package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.rmi.NotBoundException;

/*
da eliminare probabilmente
 */
/**
 * This class implements the controller for the "Choose New or Old Game" scene in the game's GUI.
 * It allows the user to choose between creating a new game or joining an existing one.
 */
public class ChooseNewOrOldController extends GenericSceneController {

    @FXML
    private Button createNewGameButton;

    @FXML
    private Button joinExistingGameButton;

    /**
     * Handles clicking the "Create New Game" button.
     * Calls the `newGame` method of the terminal interface with player name and a flag indicating creating a new game.
     *
     * @param event The ActionEvent triggered by clicking the button.
     * @throws IOException If there's an error communicating with the client.
     * @throws NotBoundException If the RMI registry is not bound.
     * @throws ClassNotFoundException If a class is not found during remote method invocation.
     * @throws InterruptedException If the thread is interrupted.
     */
    @FXML
    private void handleCreateNewGame(ActionEvent event) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        super.client.getTerminal_interface().newGame(super.client.getTerminal_interface().getName(),true );
    }

    /**
     * Handles clicking the "Join Existing Game" button.
     * Calls the `chooseMatch` method of the terminal interface with the player name.
     *
     * @param event The ActionEvent triggered by clicking the button.
     * @throws IOException If there's an error communicating with the client.
     * @throws NotBoundException If the RMI registry is not bound.
     * @throws ClassNotFoundException If a class is not found during remote method invocation.
     * @throws InterruptedException If the thread is interrupted.
     */
    @FXML
    private void handleJoinExistingGame(ActionEvent event) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        super.client.getTerminal_interface().chooseMatch(super.client.getTerminal_interface().getName());
    }
}
