package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * This class implements the controller of a generic Alert Scene.
 */
public class MessageSceneController extends GenericSceneController {
    @FXML
    private Label messageTitle;
    @FXML
    private Label messageMessage;
    private SceneController controller;
    private Stage stage;

    public void setMessageTitle(String title) {
        messageTitle.setText(title);
    }

    public void setMessageMessage(String message) {
        messageMessage.setText(message);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the click on the "OK" button of a popup window.
     * Closes the current stage (popup window) if it exists.
     */
    @FXML
    private void handleOkAction() {
        if (stage != null) {
            stage.close();
        }
    }

    public void setController (SceneController scene){
        this.controller = scene;
    }
}