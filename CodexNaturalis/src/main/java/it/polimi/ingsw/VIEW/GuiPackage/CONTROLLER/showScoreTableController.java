package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * showScoreTableController handles showing and hiding an image and updating a button text accordingly.
 */
public class showScoreTableController {

    @FXML
    private ImageView myImageView;

    @FXML
    private Button showImageButton;

    /**
     * Toggles the visibility of the `myImageView` and updates the text of the `showImageButton` accordingly.
     */
    @FXML
    private void handleButtonAction() {
        // Method called when the button is pressed
        boolean isVisible = myImageView.isVisible(); // Check if the ImageView is visible or not
        myImageView.setVisible(!isVisible); // Toggle the visibility of the ImageView

        // Modify the button text based on the visibility of the ImageView
        if (isVisible) {
            showImageButton.setText("Mostra immagine");
        } else {
            showImageButton.setText("Nascondi immagine");
        }
    }

}
