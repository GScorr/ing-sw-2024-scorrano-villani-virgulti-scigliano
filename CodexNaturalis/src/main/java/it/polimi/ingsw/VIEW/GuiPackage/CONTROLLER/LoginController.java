
package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;




import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.rmi.NotBoundException;

public class LoginController extends GenericSceneController {
    private String username;
    private SceneController controller;

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {

        String name = usernameField.getText();

       // loadLobby(username);

        //loadingIndicator.setVisible(true);
        int flag;
            flag = client.checkName(name);
            if(flag==0){

                controller.showAlert("Error", "Name already selected");
            }
            else if(flag==2) {
                client.getTerminal_interface().setNewClient(false);
                controller.showAlert("Coglione ti sei riconnesso", "Suca");
                client.getTerminal_interface().gameAccess(name);
            }
            else{
                client.getTerminal_interface().setNewClient(true);
                client.getTerminal_interface().gameAccess(name);
            }

    }


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

    @Override
    public String returnString() {
        return username;
    }

    public void setController(SceneController scene){
        this.controller = scene;
    }
}
