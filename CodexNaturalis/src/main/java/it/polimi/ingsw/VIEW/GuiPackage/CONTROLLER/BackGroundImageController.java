package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.RMI_FINAL.ChatIndexManager;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract base class for scene controllers in the graphical user interface (GUI)
 * with chat and opponent field functionalities.
 *
 */
public class BackGroundImageController extends GenericSceneController {
    public ImageView backgroundImage;
    @FXML
    private TextField gameNameField;

    @FXML
    public void initialize() {

        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("src/resources/BackGroundImaging/ConnectionLoadind.png");
        // Adjust the file path as necessary
        // Set the background image
        Image image = new Image(resourceStream);
        backgroundImage.setImage(image);

        // Set up a listener to bind the ImageView properties once the scene is available
        backgroundImage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                backgroundImage.fitHeightProperty().bind(newScene.heightProperty());
                backgroundImage.fitWidthProperty().bind(newScene.widthProperty());
            }
        });

    }
}

