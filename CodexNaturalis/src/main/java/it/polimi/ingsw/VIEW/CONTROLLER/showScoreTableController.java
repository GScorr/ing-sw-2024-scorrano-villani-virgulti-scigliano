package it.polimi.ingsw.VIEW.CONTROLLER;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class showScoreTableController {

    @FXML
    private ImageView myImageView;

    @FXML
    private Button showImageButton;

    @FXML
    private void handleButtonAction() {
        // Metodo chiamato quando il bottone viene premuto
        boolean isVisible = myImageView.isVisible(); // Verifica se l'ImageView è visibile o meno
        myImageView.setVisible(!isVisible); // Inverte la visibilità dell'ImageView

        // Modifica il testo del pulsante in base alla visibilità dell'ImageView
        if (isVisible) {
            showImageButton.setText("Mostra immagine");
        } else {
            showImageButton.setText("Nascondi immagine");
        }
    }
}
