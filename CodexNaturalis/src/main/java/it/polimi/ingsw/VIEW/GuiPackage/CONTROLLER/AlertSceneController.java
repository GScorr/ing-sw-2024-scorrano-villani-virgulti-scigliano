package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * This class implements the controller of a generic Alert Scene.
 */
public class AlertSceneController extends GenericSceneController {
    @FXML
    private Label alertTitle;
    @FXML
    private Label alertMessage;
    private SceneController controller;
    private Stage stage;

    public void setAlertTitle(String title) {
        alertTitle.setText(title);
    }

    public void setAlertMessage(String message) {
        alertMessage.setText(message);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    private void handleOkAction() {
        if (stage != null) {
            stage.close();
            controller.getActiveController().emptyField();
        }
    }

    public void setController (SceneController scene){
        this.controller = scene;
    }
}