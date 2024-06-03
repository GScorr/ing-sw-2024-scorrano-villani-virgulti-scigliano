package it.polimi.ingsw.VIEW.GuiPackage;
import it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER.AlertSceneController;
import it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER.GenericSceneController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {


    //path has to change
    public static final String CARD_IMAGE_PREFIX = "/images/cards/...";

    private  Scene activeScene;
    private  Stage stage;
    private  GenericSceneController activeController;

    public  void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Returns the active scene.
     *
     * @return active scene.
     */
    public  Scene getActiveScene() {
        return activeScene;
    }

    /**
     * Returns the active controller.
     *
     * @return active controller.
     */
    public  GenericSceneController getActiveController() {
        return activeController;
    }

    /**
     * Changes the root panel of the scene argument.
     *
     * @param scene        the scene whose change the root panel. This will become the active scene.
     * @param fxml         the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     * @param <T>          this is the type parameter.
     * @return the controller of the new scene loaded by the FXMLLoader.
     */
    public  <T> T changeRootPane( Scene scene, String fxml) {
        T controller = null;

        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();
            controller = loader.getController();

            activeController = (GenericSceneController) controller;
            activeScene = new Scene(root,1500,750);
            activeScene.setRoot(root);
            stage.setScene(activeScene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return controller;
    }

    /**
     * Changes the root panel of the scene argument.
     *
     * @param event        the event which is happened into the scene.
     * @param fxml         the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     * @param <T>          this is the type parameter.
     * @return the controller of the new scene loaded by the FXMLLoader.
     */
    public  <T> T changeRootPane( Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        return changeRootPane(scene, fxml);
    }

    /**
     * Changes the root panel of the active scene.
     *
     * @param fxml         the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     * @param <T>          this is the type parameter.
     * @return the controller of the new scene loaded by the FXMLLoader.
     */
    public  <T> T changeRootPane(String fxml) {
        return changeRootPane(activeScene, fxml);
    }


    /**
     * Shows a custom message in a popup.
     *
     * @param title   the title of the popup.
     * @param message the message of the popup.
     */
    public  void showAlert(String title, String message) {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/alert_scene.fxml"));

        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
          //  Client.LOGGER.severe(e.getMessage());
            return;
        }
        AlertSceneController alertSceneController = loader.getController();
        Scene alertScene = new Scene(parent);
        alertSceneController.setScene(alertScene);
        alertSceneController.setAlertTitle(title);
        alertSceneController.setAlertMessage(message);
        alertSceneController.displayAlert();
    }



}
