package it.polimi.ingsw.VIEW.CONTROLLER;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class GameController {

    private Button showTableScore;

    @FXML
    private Button showChat;

    @FXML
    private ImageView tableScoreImage;

    @FXML
    private VBox chatBox;

    @FXML
    private ListView<String> chatMessages;

    @FXML
    private TextField messageInput;

    @FXML
    private ImageView card1, card2, card3;
    private boolean c1, c2, c3;

    public void handleShowChatAction() { //imposto la visibilità del box della chat
        chatBox.setVisible(!chatBox.isVisible());
    }

    @FXML
    private void handleSendMessage() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            addMessageToChat("You: " + message);
            // Qui potresti gestire l'invio del messaggio al server o ad altri giocatori
            messageInput.clear(); // Pulisce il campo di input del messaggio dopo l'invio
        }
    }

    // Funzione per aggiungere un messaggio alla ListView della chat
    private void addMessageToChat(String message) {
        chatMessages.getItems().add(message);
        // Scrolle la ListView in modo che l'ultimo messaggio sia visibile
        chatMessages.scrollTo(chatMessages.getItems().size() - 1);
    }

    @FXML
    private void handleShowTableScore() {
        tableScoreImage.setVisible(!tableScoreImage.isVisible());
    }

    @FXML
// Metodo per caricare un'immagine dinamicamente
    public void initialize() {

        File file = new File("src/resources/img/FRONTSIDE/001.png");
        Image image = new Image(file.toURI().toString());

        card1.setImage(image);
        c1=true; //se true carta front


        file = new File("src/resources/img/FRONTSIDE/002.png");
        image = new Image(file.toURI().toString());

        card2.setImage(image);
        c2=true;

        file = new File("src/resources/img/FRONTSIDE/003.png");
        image = new Image(file.toURI().toString());

        card3.setImage(image);
        c3=true;

    }

    @FXML
    public void changeSide(MouseEvent event) {

        // Ottieni l'ImageView associato all'evento
        ImageView imageView = (ImageView) event.getTarget();
       // System.out.println(imageView.getId());

        // Verifica se l'ID dell'ImageView è "card2"
        if (imageView.getId().equals("card1")) {
           load(imageView, c1, 1);
           c1=!c1;
        }
        else if (imageView.getId().equals("card2")) {
            load(imageView, c2, 2);
            c2=!c2;
        }
        else{
            load(imageView, c3, 3);
            c3=!c3;
        }

    }

    private void load(ImageView card, Boolean side, int numCarta){ //da rivedre quando saranno da caricare le carte reali

        String code;

        if(side){
            code="BACKSIDE";
        }
        else{
            code="FRONTSIDE";
        }

        File file = new File("src/resources/img/"+code+"/00"+numCarta+".png");
        Image image = new Image(file.toURI().toString());
        card.setImage(image);
    }



}
