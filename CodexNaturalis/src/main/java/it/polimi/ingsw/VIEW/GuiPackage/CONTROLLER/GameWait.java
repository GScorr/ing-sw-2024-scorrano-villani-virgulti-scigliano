package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.RMI_FINAL.ChatIndexManager;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawCenter;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawGold;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawResource;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GameWait extends GenericSceneController {

    private ChatIndexManager chat_manager = new ChatIndexManager();

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

    @FXML
    private Menu chatsMenu;

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
    private AnchorPane HeaderInclude;



    private void showChat(String chatName, int chatId) throws IOException {

        /*chatBox.getChildren().clear();  //
        chatBox.setVisible(true);
        System.out.println("chat is now visible: " + chatBox.isVisible());*/

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
    }

    public void addChatItem(String chatName, int chatId) {
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
    }

    private void updateUnreadTotal() throws IOException {
        client.getMiniModel().setUnread_total(0);
        for (Integer i : client.getMiniModel().getNot_read()) {
            client.getMiniModel().setUnread_total(client.getMiniModel().getUnread_total() + i);
        }
    }
    @FXML
    public void startInitialize() throws IOException, InterruptedException {

        //header
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/header.fxml"));
        Parent header = loader.load();
        HeaderController headerController = loader.getController();
        headerController.setThe_client(super.client);
        // Aggiungi l'header alla posizione desiderata nel layout principale
        // Ad esempio, se headerInclude è un AnchorPane, puoi aggiungere l'header così:
        ((AnchorPane) HeaderInclude).getChildren().add(header);
        headerController.startInitializeHeader();

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

        token_client = client.getToken();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                        super.client.getTerminal_interface().manageGame();
                        break;
                    };
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        startMenuCheck();


    }

    private void startMenuCheck() throws IOException {
        Thread menuUpdater = new Thread(() -> {
            while (true) {

                chatsMenu.getItems().clear();
                int i=1;
                try {
                    if(client.getMiniModel().getNum_players() > 2){
                        for( i = 1; i<=client.getMiniModel().getNum_players(); ++i){
                            if( i!=client.getMiniModel().getMy_index() ) addChatItem("-CHAT WITH PLAYER " + client.getMiniModel().getNum_to_player().get(i) + " - New messages (" + client.getMiniModel().getNot_read().get(chat_manager.getChatIndex(client.getMiniModel().getMy_index(),i)) + ")", i);}
                        addChatItem("PUBLIC CHAT" + " - New messages (" + client.getMiniModel().getNot_read().get(6) + ")", i);
                    }else{ addChatItem("PUBLIC CHAT" + " - New messages (" + client.getMiniModel().getNot_read().get(6) + ")", i);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        menuUpdater.start();
    }

    @FXML
    private void handleChatsMenuClick() throws IOException {

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
    public void openPopup(int i){
        System.out.println("se arriva qua è sbagliato");
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


    @FXML
    private void handleViewOpponentFields() throws IOException {

            // Create a list of player descriptions from the map

            List<String> playerDescriptions = client.getMiniModel().getNum_to_player().entrySet().stream()
                    .map(entry -> "Player " + entry.getKey() + " Name: " + entry.getValue())
                    .collect(Collectors.toList());

            // Initialize the ChoiceDialog with the list of player descriptions
            ChoiceDialog<String> dialog = new ChoiceDialog<>(playerDescriptions.get(0), playerDescriptions);
            dialog.setTitle("Visualizza campo da gioco avversari");
            dialog.setHeaderText("Quale campo vuoi vedere?");
            dialog.setContentText("Scegli il giocatore:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(description -> {
                // Extract the player number from the selected description
                int playerNumber = Integer.parseInt(description.split(" ")[1]);
                // Load and display the selected player's field
                try {
                    showOpponentField(playerNumber);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

    }

    @FXML
    private void handleViewDeck() throws IOException {


        // Create a new stage for the pop-up
        Stage stage = new Stage();
        stage.setTitle("Deck Cards");

        // Create a VBox
        VBox handBox = new VBox();
        handBox.setAlignment(javafx.geometry.Pos.CENTER);
        handBox.setMaxWidth(500.0);
        handBox.setSpacing(10);

        // Get center cards
        List<PlayCard> gold_centerCards = client.getMiniModel().getCards_in_center().getGold_list();

        // Add center cards to the VBox
        for (int i = 0; i < gold_centerCards.size(); i++) {
            PlayCard card = gold_centerCards.get(i);
            String labelText = "Center Card " + (i + 1) + ":";
            String imagePath = card.front_side_path;

            Label cardLabel = new Label(labelText);
            ImageView cardImageView = new ImageView();
            cardImageView.setFitHeight(133);
            cardImageView.setFitWidth(100);
            cardImageView.setPreserveRatio(true);

            // Load and set the image for the ImageView
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            cardImageView.setImage(image);


            // Add the label and image view to the VBox
            handBox.getChildren().add(cardLabel);
            handBox.getChildren().add(cardImageView);
        }

        List<PlayCard> resource_centerCards = client.getMiniModel().getCards_in_center().getResource_list();
        // Add center cards to the VBox
        for (int i = 0; i < resource_centerCards.size(); i++) {
            PlayCard card = resource_centerCards.get(i);
            String labelText = "Center Card " + (i + 1) + ":";
            String imagePath = card.front_side_path;

            Label cardLabel = new Label(labelText);
            ImageView cardImageView = new ImageView();
            cardImageView.setFitHeight(133);
            cardImageView.setFitWidth(100);
            cardImageView.setPreserveRatio(true);

            // Load and set the image for the ImageView
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            cardImageView.setImage(image);

            // Add the label and image view to the VBox
            handBox.getChildren().add(cardLabel);
            handBox.getChildren().add(cardImageView);
        }

        // Get deck cards
        PlayCard deckCard1 = client.getMiniModel().getTop_gold();
        PlayCard deckCard2 = client.getMiniModel().getTop_resource();

        // Add deck cards to the VBox
        if (deckCard1 != null) {
            String labelText = "GOLD DECK :";
            String imagePath = deckCard1.back_side_path;

            Label cardLabel = new Label(labelText);
            ImageView cardImageView = new ImageView();
            cardImageView.setFitHeight(133);
            cardImageView.setFitWidth(100);
            cardImageView.setPreserveRatio(true);

            // Load and set the image for the ImageView
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            cardImageView.setImage(image);


            // Add the label and image view to the VBox
            handBox.getChildren().add(cardLabel);
            handBox.getChildren().add(cardImageView);
        }else{
            String labelText =  "GOLD DECK is empty:";
            String imagePath = "src/resources/Card/Bianco.png";

            Label cardLabel = new Label(labelText);
            ImageView cardImageView = new ImageView();
            cardImageView.setFitHeight(133);
            cardImageView.setFitWidth(100);
            cardImageView.setPreserveRatio(true);

            // Load and set the image for the ImageView
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            cardImageView.setImage(image);


            // Add the label and image view to the VBox
            handBox.getChildren().add(cardLabel);
            handBox.getChildren().add(cardImageView);
        }

        if (deckCard2 != null) {
            String labelText = "RESOURCE DECK :";
            String imagePath = deckCard2.back_side_path;

            Label cardLabel = new Label(labelText);
            ImageView cardImageView = new ImageView();
            cardImageView.setFitHeight(133);
            cardImageView.setFitWidth(100);
            cardImageView.setPreserveRatio(true);

            // Load and set the image for the ImageView
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            cardImageView.setImage(image);


            // Add the label and image view to the VBox
            handBox.getChildren().add(cardLabel);
            handBox.getChildren().add(cardImageView);
        }else{
            String labelText =  "RESOURCE DECK is empty:";
            String imagePath = "src/resources/Card/Bianco.png";

            Label cardLabel = new Label(labelText);
            ImageView cardImageView = new ImageView();
            cardImageView.setFitHeight(133);
            cardImageView.setFitWidth(100);
            cardImageView.setPreserveRatio(true);

            // Load and set the image for the ImageView
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            cardImageView.setImage(image);


            // Add the label and image view to the VBox
            handBox.getChildren().add(cardLabel);
            handBox.getChildren().add(cardImageView);
        }

        // Set the scene for the new stage
        Scene scene = new Scene(handBox);
        stage.setScene(scene);

        // Optionally, explicitly set the popup to be non-modal (if needed)
        stage.initModality(Modality.NONE); // This line ensures the stage is non-modal

        // Show the new stage without waiting
        stage.show();
    }
    @FXML
    private void handleChat() {

    }

    private void showOpponentField(int playerNumber) throws IOException {
        // Create a new stage for the pop-up
        Stage stage = new Stage();
        stage.setTitle("Campo da gioco di Player " + playerNumber + " - " +  client.getMiniModel().getNum_to_player().get(playerNumber));

        // Create a ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(false);
        scrollPane.setFitToWidth(false);
        scrollPane.setMaxWidth(1000.0);
        scrollPane.setPannable(true);
        scrollPane.setPrefHeight(450.0);

        // Create the GridPane
        GridPane gameGrid = new GridPane();
        gameGrid.setHgap(20);
        gameGrid.setPrefHeight(1500);
        gameGrid.setPrefWidth(1500);
        gameGrid.setVgap(5);

        // Add column and row constraints
        for (int i = 0; i < 45; i++) {
            ColumnConstraints col = new ColumnConstraints(49.65);
            gameGrid.getColumnConstraints().add(col);

            RowConstraints row = new RowConstraints(33.1);
            gameGrid.getRowConstraints().add(row);
        }

        // Populate the GridPane with game data
        Set<Integer> visibleRows = new HashSet<>();
        Set<Integer> visibleCols = new HashSet<>();

        int count = 1;
        //errore può essere qua
        int tmp = 0 ;
        while (count <= client.getMiniModel().game_fields.get(playerNumber - 1).card_inserted) {
            for (int i = 0; i < Constants.MATRIXDIM; i++) {
                for (int j = 0; j < Constants.MATRIXDIM; j++) {
                    if (client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getOrder_above() == count) {
                        tmp = count;
                        count = 1500;
                        if (client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().flipped) {
                            addImageToGrid(gameGrid, i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().back_side_path);
                        } else {
                            addImageToGrid(gameGrid, i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().front_side_path);
                        }
                        updateVisibleIndices(visibleRows, visibleCols, i, j);
                    } else if (client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getOrder_below() == count) {
                        tmp = count;
                        count = 1500;
                        if (client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().flipped) {
                            addImageToGrid(gameGrid, i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().back_side_path);
                        } else {
                            addImageToGrid(gameGrid, i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().front_side_path);
                        }
                        updateVisibleIndices(visibleRows, visibleCols, i, j);
                    }
                }
            }
            count = tmp;
            count++;
        }

        adjustGridVisibility(gameGrid, visibleRows, visibleCols);

        // Set the GridPane as the content of the ScrollPane
        scrollPane.setContent(gameGrid);

        // Set the scene for the new stage
        Scene scene = new Scene(scrollPane);
        stage.setScene(scene);

        // Optionally, explicitly set the popup to be non-modal (if needed)
        stage.initModality(Modality.NONE); // This line ensures the stage is non-modal

        // Show the new stage without waiting
        stage.show();
    }

    private void addImageToGrid(GridPane grid, int row, int col, String imagePath) {
        File file = new File(imagePath);
        Image image = new Image(file.toURI().toString());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(49.65 * 2);
        imageView.setFitHeight(33.1 * 2);

        StackPane stackPane = new StackPane();
        imageView.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10;");
        stackPane.getChildren().add(imageView);
        grid.add(stackPane, row, col);
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

    private void adjustGridVisibility(GridPane grid, Set<Integer> visibleRows, Set<Integer> visibleCols) {
        for (int i = 0; i < Constants.MATRIXDIM; i++) {
            if (!visibleRows.contains(i)) {
                grid.getRowConstraints().get(i).setMinHeight(0);
                grid.getRowConstraints().get(i).setPrefHeight(0);
                grid.getRowConstraints().get(i).setMaxHeight(0);
            }
        }
        for (int j = 0; j < Constants.MATRIXDIM; j++) {
            if (!visibleCols.contains(j)) {
                grid.getColumnConstraints().get(j).setMinWidth(0);
                grid.getColumnConstraints().get(j).setPrefWidth(0);
                grid.getColumnConstraints().get(j).setMaxWidth(0);
            }
        }
    }
}
