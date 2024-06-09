package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DragAndDropController extends GenericSceneController implements Initializable {
    @FXML
    private ImageView dragImage;

    @FXML
    private StackPane dropZone;

    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up drag gesture for dragImage
        dragImage.setOnDragDetected(event -> {
            Dragboard dragboard = dragImage.startDragAndDrop(TransferMode.ANY);

            // Add the image to the dragboard
            ClipboardContent content = new ClipboardContent();
            content.putImage(dragImage.getImage());
            dragboard.setContent(content);

            event.consume();
        });

        // Set up drag gesture for dropZone
        dropZone.setOnDragOver(event -> {
            if (event.getGestureSource() != dropZone && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        dropZone.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasImage()) {
                // Add the image to the dropZone
                ImageView droppedImage = new ImageView(dragboard.getImage());
                dropZone.getChildren().add(droppedImage);
                droppedImage.setFitHeight(dragImage.getFitHeight());
                droppedImage.setFitWidth(dragImage.getFitWidth());
                droppedImage.setLayoutX(event.getX() - dragImage.getFitWidth() / 2);
                droppedImage.setLayoutY(event.getY() - dragImage.getFitHeight() / 2);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
}