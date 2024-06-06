package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;

public class GameController2 extends GenericSceneController{

    @FXML
    private ImageView imageView;

    private StringProperty imagePath = new SimpleStringProperty();

    public String getImagePath() {
        return imagePath.get();
    }

    public void setImagePath(String path) {
        this.imagePath.set(path);
    }

    @FXML
    private void handleTopLeft() {
        showAlert("Hai cliccato in alto a sinistra");
    }

    @FXML
    private void handleTopRight() {
        showAlert("Hai cliccato in alto a destra");
    }

    @FXML
    private void handleBottomLeft() {
        showAlert("Hai cliccato in basso a sinistra");
    }

    @FXML
    private void handleBottomRight() {
        showAlert("Hai cliccato in basso a destra");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informazione");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void startInitialize() throws IOException, ClassNotFoundException, InterruptedException {
        // Ottenere l'immagine dalla funzione showStartingCardGUI()
        PlayCard startingCard = client.showStartingCardGUI();
        String imagePath = startingCard.front_side_path;
        File file = new File(imagePath);
        Image image = new Image(file.toURI().toString());

        // Imposta l'immagine nell'ImageView
        imageView.setImage(image);
    }
}
