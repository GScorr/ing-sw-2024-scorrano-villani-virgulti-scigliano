package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;


import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class GameController extends GenericSceneController{

    @FXML
    private StackPane cell_1_1;
    @FXML
    private StackPane cell_1_2;
    @FXML
    private StackPane cell_2_1;
    @FXML
    private StackPane cell_2_2;

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
    private boolean c1, c2, c3; //true quando sono frontside

    @FXML
    private ImageView commonGoal1, commonGoal2, Goal1, Goal2, chooseGoal1, chooseGoal2;

    private ImageView lastClickedImage = null;
    private boolean isZoomedIn = false;

    @FXML
    private Button chooseGoal;

    @FXML
    private ImageView startingCard;
    private boolean sideStartingCard;

    private double initialX;
    private double initialY;
    private double initialTranslateX;
    private double initialTranslateY;
    /**
     * Soglia per distinguere tra clic e trascinamento
     * se trascino per meno di 50 px la carta non viene spostata ma solo si gira
     */
    private static final double CLICK_THRESHOLD = 5;
    private boolean dragged;

    @FXML
    private Pane campoDaGioco;


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

        file = new File("src/resources/img/GOALCARDforntSide/087.png");
        image = new Image(file.toURI().toString());

        commonGoal1.setImage(image);


        file = new File("src/resources/img/GOALCARDforntSide/088.png");
        image = new Image(file.toURI().toString());

        commonGoal2.setImage(image);


        file = new File("src/resources/img/GOALCARDforntSide/089.png");
        image = new Image(file.toURI().toString());

        Goal1.setImage(image);
        chooseGoal1.setImage(image);

        file = new File("src/resources/img/GOALCARDforntSide/090.png");
        image = new Image(file.toURI().toString());

        Goal2.setImage(image);
        chooseGoal2.setImage(image);

        file = new File("src/resources/img/STARTINGCARDfrontSide/081.png");
        image = new Image(file.toURI().toString());

        startingCard.setImage(image);
        sideStartingCard=true;

    }
    public void startInitialize() throws IOException, ClassNotFoundException, InterruptedException {
        loadStartingCard();
    }
    public void loadStartingCard() throws IOException, ClassNotFoundException, InterruptedException {
        // Ottieni la carta dal client
        ImageView startingCard = new ImageView();

        File file = new File(client.showStartingCardGUI().back_side_path);
        Image image = new Image(file.toURI().toString());

        startingCard.setImage(image);

        // Aggiungi la carta alle celle specificate
        cell_1_1.getChildren().add(startingCard);
        cell_1_2.getChildren().add(startingCard);
        cell_2_1.getChildren().add(startingCard);
        cell_2_2.getChildren().add(startingCard);

        // Aggiungi logica per visualizzare correttamente la carta
        startingCard.setFitHeight(cell_1_1.getHeight() * 2); // Adatta l'altezza
        startingCard.setFitWidth(cell_1_1.getWidth() * 2); // Adatta la larghezza
    }

    @FXML
    public void changeSide(ImageView imageView) {

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
        else if (imageView.getId().equals("startingCard")) {
            loadStartingCard(imageView, sideStartingCard);
            sideStartingCard=!sideStartingCard;
        }
        else{
            load(imageView, c3, 3);
            c3=!c3;
        }
    }

    /**
     * da rivedre con le chiamate
     */
    private void load(ImageView card, Boolean side, int numCarta){

        String code;

        if(side){
            /**
             * da fare il check se il colore è rosso carico 001 backside
             */
            code="BACKSIDE";
            numCarta=1;
        }
        else{
            code="FRONTSIDE";
        }
        /**
         * modo semplice per prendere la carta che mi serve
         */
        File file = new File("src/resources/img/"+code+"/00"+numCarta+".png");
        Image image = new Image(file.toURI().toString());
        card.setImage(image);
    }

    private void loadStartingCard(ImageView card, Boolean side){
        String code;

        if(side){
            code="STARTINGCARDbackSide";
        }
        else{
            code="STARTINGCARDfrontSide";
        }
        File file = new File("src/resources/img/"+code+"/081.png");
        Image image = new Image(file.toURI().toString());
        card.setImage(image);
    }

    @FXML
    public void showGoalToChose(){
        chooseGoal1.setVisible(true);
        chooseGoal2.setVisible(true);
        chooseGoal.setVisible(false);
    }

    /**
     * da sistemare con la chiamata a server per il caricamento delle immagini
     * e per la carta selezionata
     */
    @FXML
    public void clickOnChoosedGoal(MouseEvent event){
        ImageView imageView = (ImageView) event.getTarget();
        if(imageView.getId().equals("chooseGoal1")){
            Goal1.setVisible(true);
            chooseGoal1.setVisible(false);
            chooseGoal2.setVisible(false);
        }
        else if(imageView.getId().equals("chooseGoal2")){
            Goal2.setVisible(true);
            chooseGoal2.setVisible(false);
            chooseGoal1.setVisible(false);
        }
    }

    //funzione per ingrandire i goal
    @FXML
    public void clickOnGoal(MouseEvent event){
        ImageView imageView = (ImageView) event.getTarget();
        if (lastClickedImage == imageView && isZoomedIn) {
            zoomOut(imageView);
            isZoomedIn = false;
        } else {
            zoomIn(imageView);
            isZoomedIn = true;
        }
        lastClickedImage = imageView;
    }

    private void zoomIn(ImageView imageView) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), imageView);
        scaleTransition.setToX(2.0); // Imposta la scala sull'asse X al doppio della dimensione originale
        scaleTransition.setToY(2.0); // Imposta la scala sull'asse Y al doppio della dimensione originale
        scaleTransition.play();

        // Transizione di traslazione verso sinistra
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), imageView);
        translateTransition.setToX(-imageView.getFitWidth()*2); // Sposta l'immagine a sinistra di metà della sua larghezza
        translateTransition.setToY(imageView.getFitWidth()*2); // Sposta l'immagine a sinistra di metà della sua larghezza

        // Avvia entrambe le transizioni contemporaneamente
        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, translateTransition);
        parallelTransition.play();
    }

    private void zoomOut(ImageView imageView) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), imageView);
        scaleTransition.setToX(1.0); // Reimposta la scala sull'asse X alla dimensione originale
        scaleTransition.setToY(1.0); // Reimposta la scala sull'asse Y alla dimensione originale
        scaleTransition.play();


        // Transizione di traslazione verso destra
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), imageView);
        translateTransition.setToX(0); // Reimposta la posizione originale
        translateTransition.setToY(0);

        // Avvia entrambe le transizioni contemporaneamente
        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, translateTransition);
        parallelTransition.play();
    }

    @FXML
    public void onMousePressedHandler(MouseEvent event) {
        ImageView card = (ImageView) event.getSource();
        // Memorizza le posizioni iniziali della carta
        if(card.getId().equals("startingCard")){
            double newWidth = 200.0; // Larghezza desiderata
            double newHeight = 196.0; // Altezza desiderata
            card.setFitWidth(newWidth);
            card.setFitHeight(newHeight);
        }
        initialX = event.getSceneX();
        initialY = event.getSceneY();
        initialTranslateX = card.getTranslateX();
        initialTranslateY = card.getTranslateY();
        dragged = false; // reimposta il flag per il trascinamento
    }

    @FXML
    public void onMouseDraggedHandler(MouseEvent event) {
        ImageView card = (ImageView) event.getSource();
        // Calcola la nuova posizione della carta in base al movimento del mouse
        double offsetX = event.getSceneX() - initialX;
        double offsetY = event.getSceneY() - initialY;
        card.setTranslateX(offsetX);
        card.setTranslateY(offsetY);
        dragged = true; // la carta è stata trascinata
    }

    @FXML
    public void onMouseReleasedHandler(MouseEvent event) {
        ImageView card = (ImageView) event.getSource();
        // Se la carta è stata trascinata meno di CLICK_THRESHOLD, ruotala
        if (!dragged) {
            changeSide(card);
        } else {
            // Se la carta è stata trascinata, reimposta la posizione iniziale se è stata rilasciata fuori dal campo di gioco
            if (isOutsideGameField(card)) {
                card.setLayoutX(initialX);
                card.setLayoutY(initialY);
                card.setTranslateX(initialTranslateX);
                card.setTranslateY(initialTranslateY);
            } else {
                disableCardInteraction(card);
            }
        }
        dragged = false;
    }


    private void disableCardInteraction(ImageView card) {
        card.setOnMouseClicked(null); // Rimuove il gestore dell'evento onClick per impedire ulteriori clic sulla carta
        card.setOnMousePressed(null); // Rimuove il gestore dell'evento onMousePressed
        card.setOnMouseDragged(null); // Rimuove il gestore dell'evento onMouseDragged
        card.setDisable(true); // Disabilita la carta
    }

    private boolean isOutsideGameField(ImageView card) {
        double releaseX = card.getLayoutX() - card.getTranslateX();
        double releaseY = card.getLayoutY() - card.getTranslateY();

        double fieldX = campoDaGioco.getLayoutX();
        double fieldY = campoDaGioco.getLayoutY();
        double fieldWidth = campoDaGioco.getPrefWidth();
        double fieldHeight = campoDaGioco.getPrefHeight();

        boolean isOutside = releaseX < fieldX || releaseY < fieldY || releaseX > fieldX + fieldWidth || releaseY > fieldY + fieldHeight;
        System.out.println("Release coordinates: (" + releaseX + ", " + releaseY + ")");
        System.out.println("Field bounds: x=" + (fieldX) + ", y=" + fieldY + ", width=" + fieldWidth + ", height=" + fieldHeight);
        System.out.println("Is outside field? " + isOutside);


        return isOutside;
    }



}
