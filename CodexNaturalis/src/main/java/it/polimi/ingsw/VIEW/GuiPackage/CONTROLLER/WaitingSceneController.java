package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class WaitingSceneController extends GenericSceneController{

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Label waitingMessage;

    @FXML
    private Button cancelButton;

    private SceneController controller;
    @FXML
    public void initialize() {
        System.out.println("awa");
        // Start the background task to monitor the variable
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    System.out.println(super.client.getMiniModel().getState());
                    if (!super.client.getMiniModel().getState().equals("NOT_INITIALIZED") && !client.getMiniModel().getState().equals("NOT_IN_A_GAME")) {
                        client.setGameFieldMiniModel();
                        System.out.println("fatto");
                        //controller.showMessage("SUCCESS","Your game is about to start!");
                        client.getTerminal_interface().chooseGoalState();
                        break;
                    };
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        // Logic to handle the cancel action
        // For now, we simply close the stage
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void updateWaitingMessage(String message) {
        waitingMessage.setText(message);
    }

    public void setController (SceneController scene){
        this.controller = scene;
    }
}
