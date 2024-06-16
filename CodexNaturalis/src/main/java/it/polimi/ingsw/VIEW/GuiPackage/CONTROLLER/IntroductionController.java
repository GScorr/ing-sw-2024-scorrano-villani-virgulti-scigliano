package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;

public class IntroductionController extends GenericSceneController {

    public Label titleLabel;
    @FXML
    private ImageView backgroundImage;

    @FXML
    private ProgressBar loadingBar;

    @FXML
    public void initialize() {
        // Adjust the file path as necessary
        File file = new File("src/resources/BackGroundImaging/ConnectionLoadind.png");

        // Set the background image
        Image image = new Image(file.toURI().toString());
        backgroundImage.setImage(image);

        // Impostare la label del titolo
        titleLabel.setText("CODEX NATURALIS");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");


        // Set up a listener to bind the ImageView properties once the scene is available
        backgroundImage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                backgroundImage.fitHeightProperty().bind(newScene.heightProperty());
                backgroundImage.fitWidthProperty().bind(newScene.widthProperty());
            }
        });

        // Set up the loading bar
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(loadingBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(loadingBar.progressProperty(), 1))
        );

        timeline.setCycleCount(1);
        timeline.setOnFinished(e -> {
            try {
                loadNextScene();
            } catch (IOException | NotBoundException | ClassNotFoundException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        timeline.play();
    }

    private void loadNextScene() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        super.client.getTerminal_interface().selectNamePlayer();
    }
}
