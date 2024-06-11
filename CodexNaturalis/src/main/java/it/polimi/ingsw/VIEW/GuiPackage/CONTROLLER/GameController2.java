package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.RMI_FINAL.ChatIndexManager;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawCenter;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawGold;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawResource;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameController2 extends GenericSceneController {

    @FXML
    private Button openChatButton;

    private ChatIndexManager chat_manager = new ChatIndexManager();

    @FXML
    private Button showChat;

    @FXML
    private TextField messageInput;

    @FXML
    private ImageView tableScoreImage;

    @FXML
    private VBox chatBox;

    @FXML
    private VBox chatBox2;

    @FXML
    private Menu chatsMenu;


    @FXML
    private VBox chatContainer;

    @FXML
    private ListView<String> chatMessages;

    public ImageView gold_deck;
    public ImageView resurce_deck;
    public ImageView center_card_0;
    public ImageView center_card_1;
    public ImageView center_card_2;
    public ImageView center_card_3;
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
    String token_client;
    private SendFunction function;
    @FXML
    private ToggleButton chatToggleButton;

    @FXML
    private AnchorPane HeaderInclude;

    // Aggiunge un nuovo chat item al menu "Chats"
    /*public void addChatItem(String chatName, int chatId) {
        MenuItem chatItem = new MenuItem(chatName);
        chatItem.setUserData(chatId);
        chatItem.setOnAction(event -> {
            try {
                showChat(chatName, chatId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        chatsMenu.getItems().add(chatItem);
    }*/

    private void updateUnreadTotal() throws IOException {
        client.getMiniModel().setUnread_total(0);
        for (Integer i : client.getMiniModel().getNot_read()) {
            client.getMiniModel().setUnread_total(client.getMiniModel().getUnread_total() + i);
        }
    }

    // Aggiunge un messaggio alla chat box
    private void addMessageToChatBox(String message) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        chatBox.getChildren().add(messageLabel);
    }



    // Mostra un popup quando un chat item è premuto
    /*private void showChat(String chatName, int chatId) throws IOException {

        /*chatBox.getChildren().clear();  //
        chatBox.setVisible(true);
        System.out.println("chat is now visible: " + chatBox.isVisible());

        if (client.getMiniModel().getNum_players() != 2) {
            if (chatId == client.getMiniModel().getNum_players() + 1) {
                client.getMiniModel().getNot_read().set(6, 0);
                updateUnreadTotal();
                scene_controller.showChat("Chat", 6, client, chatId);
            } else {
                client.getMiniModel().getNot_read().set(chat_manager.getChatIndex(client.getMiniModel().getMy_index(), chatId), 0);
                updateUnreadTotal();
                scene_controller.showChat("Chat", chat_manager.getChatIndex(client.getMiniModel().getMy_index(), chatId), client, chatId);
            }
        } else {
            client.getMiniModel().getNot_read().set(6, 0);
            updateUnreadTotal();
            scene_controller.showChat("Chat", 6, client, chatId);

        }
    }*/




    

    @FXML
    public void startInitialize() throws IOException, InterruptedException {
        Set<Integer> visibleRows = new HashSet<>();
        Set<Integer> visibleCols = new HashSet<>();

        //header
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/header.fxml"));
        Parent header = loader.load();
        HeaderController headerController = loader.getController();
        headerController.setThe_client(super.client);
        System.out.println("--------------------" + scene_controller);
        headerController.setScene(scene_controller);
        // Aggiungi l'header alla posizione desiderata nel layout principale
        // Ad esempio, se headerInclude è un AnchorPane, puoi aggiungere l'header così:
        ((AnchorPane) HeaderInclude).getChildren().add(header);
        headerController.startInitializeHeader();

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


        updateCardsInHand();
        updateDecks();
        updateCardsCenter();

        token_client = client.getToken();
/*

            int i=1;
            if(client.getMiniModel().getNum_players() > 2){
                for( i = 1; i<=client.getMiniModel().getNum_players(); ++i){
                    if( i!=client.getMiniModel().getMy_index() ) addChatItem("-CHAT WITH PLAYER " + client.getMiniModel().getNum_to_player().get(i) + " - New messages (" + client.getMiniModel().getNot_read().get(chat_manager.getChatIndex(client.getMiniModel().getMy_index(),i)) + ")", i);}
                addChatItem("PUBLIC CHAT" + " - New messages (" + client.getMiniModel().getNot_read().get(6) + ")", i);
            }else{ addChatItem("PUBLIC CHAT" + " - New messages (" + client.getMiniModel().getNot_read().get(6) + ")", i);
            }

            //this.chatmenu.set(5, "5- WRITE MESSAGE 1 PLAYER");
 */
  //      scene_controller.getHeader_controller().startInitializeHeader();

    }

    private void addImageToGrid(int row, int col, String imagePath) {
         file = new File(imagePath);
         image = new Image(file.toURI().toString());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(49.65 * 2);
        imageView.setFitHeight(33.1 * 2);

        StackPane stackPane = new StackPane();
        imageView.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10;");
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
                    int y = Integer.parseInt(parts[0].trim());
                    int x = Integer.parseInt(parts[1].trim());

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
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            loadingAlert.close();
        }));

        // Fa partire il timeline
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void sendFunction(SendFunction function) throws IOException, InterruptedException, ClassNotFoundException {
        client.drawCard(function);
        showLoadingPopup();
        client.getTerminal_interface().manageGame();
    }

    public void handleGoldDeckClick(MouseEvent mouseEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if(errorDrawState() == false){
            function = new SendDrawGold(token_client);
            sendFunction(function);
            updateDecks();

        }
    }



    public void handleResourceDeckClick(MouseEvent mouseEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if(errorDrawState() == false){
            function = new SendDrawResource(token_client);
            sendFunction(function);
            updateDecks();
        }
    }

    public void handleCenterCard_0Click(MouseEvent mouseEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if(!errorDrawState()){
            function = new SendDrawCenter(token_client, 0);
            sendFunction(function);
            updateCardsCenter();
        }
    }

    public void handleCenterCard_1Click(MouseEvent mouseEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if(errorDrawState() == false){
            function = new SendDrawCenter(token_client, 1);
            sendFunction(function);
            updateCardsCenter();
        }
    }

    public void handleCenterCard_2Click(MouseEvent mouseEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if(errorDrawState() == false){
            function = new SendDrawCenter(token_client, 2);
            sendFunction(function);
            updateCardsCenter();
        }
    }

    public void handleCenterCard_3Click(MouseEvent mouseEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if(errorDrawState() == false){
            function = new SendDrawCenter(token_client, 3);
            sendFunction(function);
            updateCardsCenter();
        }
    }


    private boolean errorDrawState() throws IOException {
        if(! super.client.getMiniModel().getState().equals("DRAW_CARD")){
            showError("IS NOT YOUR TURN, WAIT !!! ");
            return true;
        }
        else return false;
    }

    private void updateDecks() throws IOException {
        if(super.client.getMiniModel().getTop_resource() == null){
            file = new File("src/resources/Card/Bianco.png");
            image = new Image(file.toURI().toString());
            this.resurce_deck.setImage(image);
        }else{
            file = new File(super.client.getMiniModel().getTop_resource().back_side_path);
            image = new Image(file.toURI().toString());
            this.resurce_deck.setImage(image);
        }

        if(super.client.getMiniModel().getTop_gold() == null){
            file = new File("src/resources/Card/Bianco.png");
            image = new Image(file.toURI().toString());
            this.gold_deck.setImage(image);
        }else{
            file = new File(super.client.getMiniModel().getTop_gold().back_side_path);
            image = new Image(file.toURI().toString());
            this.gold_deck.setImage(image);}
    }

    private void updateCardsCenter() throws IOException {

        PlayCard card0 = super.client.getMiniModel().getCards_in_center().getGold_list().get(0);
        PlayCard card1 = super.client.getMiniModel().getCards_in_center().getGold_list().get(1);
        PlayCard card2 = super.client.getMiniModel().getCards_in_center().getResource_list().get(0);
        PlayCard card3 = super.client.getMiniModel().getCards_in_center().getResource_list().get(1);


        file = new File(card0.front_side_path);
        image = new Image(file.toURI().toString());
        this.center_card_0.setImage(image);

        file = new File(card1.front_side_path);
        image = new Image(file.toURI().toString());
        this.center_card_1.setImage(image);

        file = new File(card2.front_side_path);
        image = new Image(file.toURI().toString());
        this.center_card_2.setImage(image);

        file = new File(card3.front_side_path);
        image = new Image(file.toURI().toString());
        this.center_card_3.setImage(image);
    }

    private void updateCardsInHand() throws IOException {
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


}