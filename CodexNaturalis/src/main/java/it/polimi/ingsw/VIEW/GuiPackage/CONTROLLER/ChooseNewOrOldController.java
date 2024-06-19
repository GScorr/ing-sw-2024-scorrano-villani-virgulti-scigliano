package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
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

    public ImageView backgroundImage;
    @FXML
    private Button createNewGameButton;

    @FXML
    private Button joinExistingGameButton;

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
