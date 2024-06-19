package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;


import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;

/**
 * LoginController handles user login functionality and scene transition to the lobby.
 *
 */
public class LoginController extends GenericSceneController {

    public ImageView backgroundImage;
    private String username;
    private SceneController controller;

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private Label welcomeLabel;

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
     * Handles the login button click, performing username validation
     * and initiating game access through the client interface.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If an error occurs during communication with the server.
     * @throws NotBoundException If the client is not properly connected to the server.
     * @throws ClassNotFoundException If a class used during communication cannot be found.
     * @throws InterruptedException If the communication thread is interrupted.
     */
    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {

        String name = usernameField.getText();
       // loadLobby(username);
        //loadingIndicator.setVisible(true);
        int flag;
            flag = client.checkName(name);
            if(flag==0){
             //   controller.showAlert("Error", "Name already selected");
            }
            else if(flag==2) {
                client.getTerminal_interface().setNewClient(false);
                controller.showAlert("RECONNECTED", "[Continue your game]");
                Thread.sleep(500);
                client.getTerminal_interface().gameAccess(name);
            }
            else{
                client.getTerminal_interface().setNewClient(true);
                client.getTerminal_interface().gameAccess(name);
            }

    }

    /**
     * Clears the username text field.
     */
    public void emptyField(){
        usernameField.clear();
    }

/*
    private void showLoginPopup(String title, String message) {
        // Mostra un popup di conferma del login
        Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();

    }
    */

    /*
    da eliminare
    private void loadLobby(String username) {
        try {
            // Carica la nuova scena
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Lobby.fxml"));
            Parent root = loader.load();

         //   LobbyController lobbyController = loader.getController();
         //   lobbyController.setUsername(username);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1024, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */

    @Override
    public String returnString() {
        return username;
    }

    public void setController(SceneController scene){
        this.controller = scene;
    }

}
