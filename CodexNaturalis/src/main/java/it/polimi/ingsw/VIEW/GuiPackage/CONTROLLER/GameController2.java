package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameController2 extends GenericSceneController {

    public ImageView deck1;
    public ImageView deck2;
    public ImageView deck3;
    public ImageView deck4;
    public ImageView deck5;
    public ImageView deck6;
    public VBox handBox;
    public ImageView handCard1;
    public ImageView handCard2;
    public ImageView handCard3;
    public VBox deckBox;
    @FXML
    private GridPane gameGrid;

    File file;
    Image image;

    PlayCard card_1;
    Image card_1_front,card_1_back;
    boolean card_1_flip = false;
    PlayCard card_2;
    boolean card_2_flip = false;
    Image card_2_front,card_2_back;
    PlayCard card_3;
    boolean card_3_flip = false;
    Image card_3_front,card_3_back;

    // Definisci un timer
    private Timer clickTimer;
    // Definisci una variabile per memorizzare se il clic è stato mantenuto o meno
    private boolean clickHeld = false;
    private boolean just_pressed = false;


    

    @FXML
    public void startInitialize() throws IOException {
        Set<Integer> visibleRows = new HashSet<>();
        Set<Integer> visibleCols = new HashSet<>();

        int count = 1;
        int tmp = 0;
        while (count <= client.getMiniModel().getMyGameField().card_inserted) {
            for (int i = 0; i < Constants.MATRIXDIM; i++) {
                for (int j = 0; j < Constants.MATRIXDIM; j++) {
                    if (client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getOrder_above() == count) {
                        tmp = count;
                        count = 1500;
                        if (client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().flipped) {
                            addImageToGrid(i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().back_side_path);
                        } else {
                            addImageToGrid(i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().front_side_path);
                        }
                        updateVisibleIndices(visibleRows, visibleCols, i, j);
                    } else if (client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getOrder_below() == count) {
                        tmp = count;
                        count = 1500;
                        if (client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().flipped) {
                            addImageToGrid(i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().back_side_path);
                        } else {
                            addImageToGrid(i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().front_side_path);
                        }
                        updateVisibleIndices(visibleRows, visibleCols, i, j);
                    }
                }
            }
            count = tmp;
            count++;
        }

        adjustGridVisibility(visibleRows, visibleCols);

        card_1  = super.client.getMiniModel().getCards_in_hand().get(0);
        if(card_1.front_side_path != null){
            //front
            file = new File(card_1.front_side_path);
            card_1_front = new Image(file.toURI().toString());
            handCard1.setImage( card_1_front);
            System.out.println(card_1.front_side_path);
            //back
            file = new File(card_1.back_side_path);
            card_1_back = new Image(file.toURI().toString());
        }else{
            file = new File("src/resources/Card/Bianco.png");
            card_1_front = new Image(file.toURI().toString());
            card_1_back= new Image(file.toURI().toString());
            handCard1.setImage( card_1_back);
        }

        card_2  = super.client.getMiniModel().getCards_in_hand().get(1);
        if(card_2.front_side_path != null){
            //front
            file = new File(card_2.front_side_path);
            card_2_front = new Image(file.toURI().toString());
            handCard2.setImage(card_2_front);
            //back
            file = new File(card_2.back_side_path);
            card_2_back = new Image(file.toURI().toString());
        }else{
            file = new File("src/resources/Card/Bianco.png");
            card_2_front = new Image(file.toURI().toString());
            card_2_back = new Image(file.toURI().toString());
            handCard2.setImage(card_2_front);
        }
        card_3  = super.client.getMiniModel().getCards_in_hand().get(2);
        if(card_3.front_side_path != null){
            //front
            file = new File(card_3.front_side_path);
            card_3_front = new Image(file.toURI().toString());
            handCard3.setImage(card_3_front);

            //back
            file = new File(card_3.back_side_path);
            card_3_back = new Image(file.toURI().toString());
        }else{
            file = new File("src/resources/Card/Bianco.png");
            card_3_front = new Image(file.toURI().toString());
            card_3_back = new Image(file.toURI().toString());
            handCard3.setImage(card_3_front);
        }


    }

    private void addImageToGrid(int row, int col, String imagePath) {
         file = new File(imagePath);
         image = new Image(file.toURI().toString());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(49.65 * 2);
        imageView.setFitHeight(33.1 * 2);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(imageView);

        gameGrid.add(stackPane, col, row, 2, 2); // Occupy 2 columns and 2 rows
    }

    private void updateVisibleIndices(Set<Integer> visibleRows, Set<Integer> visibleCols, int row, int col) {
        for (int i = row - 1; i <= row + 2; i++) {
            if (i >= 0 && i < Constants.MATRIXDIM) {
                visibleRows.add(i);
            }
        }
        for (int j = col - 1; j <= col + 2; j++) {
            if (j >= 0 && j < Constants.MATRIXDIM) {
                visibleCols.add(j);
            }
        }
    }

    private void adjustGridVisibility(Set<Integer> visibleRows, Set<Integer> visibleCols) {
        for (int i = 0; i < Constants.MATRIXDIM; i++) {
            if (!visibleRows.contains(i)) {
                gameGrid.getRowConstraints().get(i).setMinHeight(0);
                gameGrid.getRowConstraints().get(i).setPrefHeight(0);
                gameGrid.getRowConstraints().get(i).setMaxHeight(0);
            }
        }
        for (int j = 0; j < Constants.MATRIXDIM; j++) {
            if (!visibleCols.contains(j)) {
                gameGrid.getColumnConstraints().get(j).setMinWidth(0);
                gameGrid.getColumnConstraints().get(j).setPrefWidth(0);
                gameGrid.getColumnConstraints().get(j).setMaxWidth(0);
            }
        }
    }

    public void handleCard1Click(MouseEvent mouseEvent) {
        if (just_pressed){
            just_pressed = false;
            return;
        }
        if(!card_1_flip){
            handCard1.setImage(card_1_back);
            card_1_flip = true;
        }else{
            handCard1.setImage(card_1_front);
            card_1_flip = false;
        }
    }

    public void handleCard2Click(MouseEvent mouseEvent) {
        if (just_pressed){
            just_pressed = false;
            return;
        }
        if(!card_2_flip){
            handCard2.setImage(card_2_back);
            card_2_flip = true;
        }else{
            handCard2.setImage(card_2_front);
            card_2_flip = false;
        }
    }

    public void handleCard3Click(MouseEvent mouseEvent) {
        if (just_pressed){
            just_pressed = false;
            return;
        }
        if(!card_3_flip){
            handCard3.setImage(card_3_back);
            card_3_flip = true;
        }else{
            handCard3.setImage(card_3_front);
            card_3_flip = false;
        }
    }
    public void openPopup(int i){TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Inserisci le coordinate");
        dialog.setHeaderText(null);
        dialog.setContentText("Inserisci le coordinate (x,y) [ inseririsci la virgola senza spazio ]:");

        dialog.showAndWait().ifPresentOrElse(coordinates -> {
            // Divide la stringa delle coordinate in base al separatore (ad esempio la virgola)
            String[] parts = coordinates.split(",");
            if (parts.length == 2) {
                try {
                    // Parsa le coordinate x e y come numeri interi o numeri decimali
                    int x = Integer.parseInt(parts[0].trim());
                    int y = Integer.parseInt(parts[1].trim());

                    // Ora puoi utilizzare le coordinate x e y come desideri
                    System.out.println("Coordinate X: " + x);
                    System.out.println("Coordinate Y: " + y);
                    boolean flipped;
                    if(i == 1){
                        flipped = card_1_flip;
                    } else
                    if (i == 2 ) {
                        flipped = card_2_flip;
                    }else{
                        flipped = card_3_flip;
                    }
                    System.out.println("Index of card insert is :");
                    System.out.println( i);
                    client.selectAndInsertCard(i,x,y,flipped);
                    showLoadingPopup();
                    client.getTerminal_interface().manageGame();
                } catch (NumberFormatException e) {
                    // Se non è possibile parsare le coordinate come numeri, gestisci l'eccezione
                    System.err.println("Errore durante il parsing delle coordinate.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Se la stringa delle coordinate non ha il formato corretto, gestisci l'errore
                System.err.println("Formato delle coordinate non valido.");
            }
        }, () -> {
            // Se l'utente ha premuto "Annulla", non fare nulla
            System.out.println("Popup chiuso senza inserire coordinate");
        });
    }

    public void handleCard1Pressed(MouseEvent mouseEvent) {
        // Avvia un timer quando il mouse viene premuto sulla carta
        clickTimer = new Timer();
        clickTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Quando il timer scatta, imposta il flag per indicare che il clic è stato mantenuto
                clickHeld = true;
                just_pressed = true;
            }
        }, 500); // Imposta il ritardo in millisecondi (ad esempio 500 millisecondi)
    }

    public void handleCard1Released(MouseEvent mouseEvent) throws IOException {

        // Interrompi il timer quando il mouse viene rilasciato
        clickTimer.cancel();

        // Se il clic è stato mantenuto, esegui l'azione desiderata
        if (clickHeld) {
            if(super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                openPopup(1);
            }else{
                showError("IS NOT YOUR TURN, WAIT !!! ");
            }
        } else {
            // Altrimenti, esegui un'altra azione (se necessario)
        }

        // Reimposta il flag per il prossimo utilizzo
        clickHeld = false;

    }

    @FXML
    public void handleCard2Pressed(MouseEvent mouseEvent) {
        // Avvia un timer quando il mouse viene premuto sulla carta
        clickTimer = new Timer();
        clickTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Quando il timer scatta, imposta il flag per indicare che il clic è stato mantenuto
                clickHeld = true;
                just_pressed = true;
            }
        }, 500); // Imposta il ritardo in millisecondi (ad esempio 500 millisecondi)
    }

    @FXML
    public void handleCard2Released(MouseEvent mouseEvent) throws IOException {
        // Interrompi il timer quando il mouse viene rilasciato
        clickTimer.cancel();

        // Se il clic è stato mantenuto, esegui l'azione desiderata
        if (clickHeld) {
            if(super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                openPopup(2);
            }else{
                showError("IS NOT YOUR TURN, WAIT !!! ");
            }
        } else {
            // Altrimenti, esegui un'altra azione (se necessario)
        }

        // Reimposta il flag per il prossimo utilizzo
        clickHeld = false;
    }

    public void handleCard3Pressed(MouseEvent mouseEvent) {
        // Avvia un timer quando il mouse viene premuto sulla carta
        clickTimer = new Timer();
        clickTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Quando il timer scatta, imposta il flag per indicare che il clic è stato mantenuto
                clickHeld = true;
                just_pressed = true;
            }
        }, 500); // Imposta il ritardo in millisecondi (ad esempio 500 millisecondi)
    }

    public void handleCard3Released(MouseEvent mouseEvent) throws IOException {
        // Interrompi il timer quando il mouse viene rilasciato
        clickTimer.cancel();

        // Se il clic è stato mantenuto, esegui l'azione desiderata
        if (clickHeld) {
            if(super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                openPopup(3);
            }else{
                showError("IS NOT YOUR TURN, WAIT !!! ");
            }
        } else {
            // Altrimenti, esegui un'altra azione (se necessario)
        }

        // Reimposta il flag per il prossimo utilizzo
        clickHeld = false;
    }

    // Metodo per mostrare un messaggio di errore
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void showLoadingPopup() {
        // Crea un alert di tipo INFORMATION per il loading
        Alert loadingAlert = new Alert(Alert.AlertType.INFORMATION, "Loading...", ButtonType.OK);
        loadingAlert.setHeaderText(null);
        loadingAlert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false); // Nasconde il pulsante OK

        // Mostra l'alert
        loadingAlert.show();

        // Crea un timeline per chiudere l'alert dopo 1 secondo
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            loadingAlert.close();
        }));

        // Fa partire il timeline
        timeline.setCycleCount(1);
        timeline.play();
    }
}