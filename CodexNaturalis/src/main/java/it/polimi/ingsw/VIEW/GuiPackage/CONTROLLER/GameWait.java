package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.ChatIndexManager;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawCenter;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawGold;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawResource;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.SOCKET_FINAL.clientSocket;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * this class manage the gui when the game is in waitin state
 */
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
    InputStream resourceStream;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Menu chatsMenu;


    Image image;

    PlayCard card_1;

    @FXML
    private Label playerNameLabel;

    @FXML
    private Circle playerColorCircle;

    private ColorCoordinatesHelper helper = new ColorCoordinatesHelper();

    private boolean useless = false;

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

    @FXML
    private Label currentPlayerLabel;

    public void updateCurrentPlayer(String playerName) {
        currentPlayerLabel.setText("Now is playing: " + playerName);
        currentPlayerLabel.setStyle("-fx-text-fill: white;");
    }



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

    /**
     * Updates the total number of unread messages for the client.
     *
     * @throws IOException  In case of potential I/O exceptions during communication with the client.
     */
    private void updateUnreadTotal() throws IOException {
        client.getMiniModel().setUnread_total(0);
        for (Integer i : client.getMiniModel().getNot_read()) {
            client.getMiniModel().setUnread_total(client.getMiniModel().getUnread_total() + i);
        }
    }

    /**
     * Initializes the scene elements and starts a thread to monitor game state changes.
     *
     * @throws IOException  In case of potential I/O exceptions during communication with the client.
     * @throws InterruptedException  If the thread is interrupted.
     */
    @FXML
    public void startInitialize() throws IOException, InterruptedException {

        //header
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/header.fxml"));
        Parent header = loader.load();
        HeaderController headerController = loader.getController();
        headerController.setThe_client(super.client);
        headerController.setScene(scene_controller);

        // Add the header to the desired position in the main layout
        // For example, if headerInclude is an AnchorPane, you can add the header like this:

        ((AnchorPane) HeaderInclude).getChildren().add(header);
        headerController.startInitializeHeader();
        setPlayerColor(helper.fromEnumtoColor(client.getMiniModel().getMy_player().getColor()));

        card_1  = super.client.getMiniModel().getCards_in_hand().get(0);
        if(card_1.front_side_path != null){

            //front
            resourceStream = getClass().getClassLoader().getResourceAsStream(card_1.front_side_path);
            card_1_front = new Image(resourceStream);
            handCard1.setImage( card_1_front);
            //System.out.println(card_1.front_side_path);
            //back
            resourceStream = getClass().getClassLoader().getResourceAsStream(card_1.back_side_path);
            card_1_back = new Image(resourceStream);
        }else{
            resourceStream = getClass().getClassLoader().getResourceAsStream("Card/Bianco.png");
            card_1_front = new Image(resourceStream);
            card_1_back= new Image(resourceStream);
            handCard1.setImage( card_1_back);
        }

        card_2  = super.client.getMiniModel().getCards_in_hand().get(1);
        if(card_2.front_side_path != null){
            //front
            resourceStream = getClass().getClassLoader().getResourceAsStream(card_2.front_side_path);
            card_2_front = new Image(resourceStream);
            handCard2.setImage(card_2_front);
            //back
            resourceStream = getClass().getClassLoader().getResourceAsStream(card_2.back_side_path);
            card_2_back = new Image(resourceStream);
        }else{
            resourceStream = getClass().getClassLoader().getResourceAsStream("Card/Bianco.png");
            card_2_front = new Image(resourceStream);
            card_2_back = new Image(resourceStream);
            handCard2.setImage(card_2_front);
        }
        card_3  = super.client.getMiniModel().getCards_in_hand().get(2);
        if(card_3.front_side_path != null){
            //front
            resourceStream = getClass().getClassLoader().getResourceAsStream(card_3.front_side_path);
            card_3_front = new Image(resourceStream);
            handCard3.setImage(card_3_front);

            //back
            resourceStream = getClass().getClassLoader().getResourceAsStream(card_3.back_side_path);
            card_3_back = new Image(resourceStream);
        }else{
            resourceStream = getClass().getClassLoader().getResourceAsStream("Card/Bianco.png");
            card_3_front = new Image(resourceStream);
            card_3_back = new Image(resourceStream);
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
                    if (super.client.getTerminal_interface().getIsAlone() == true ) break;
                    if (super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                        useless = true;
                        super.client.getTerminal_interface().manageGame();
                        break;
                    };
                    if (super.client.getMiniModel().getState().equals("END_GAME")) {
                        useless = true;
                        super.client.getTerminal_interface().endGame();
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

        setPlayerName(client.getMiniModel().getMy_player().getName());
        startUpdatePlaying();

        //startMenuCheck();
        checkLastTurn();

    }

    public void setPlayerColor(Color color) {
        playerColorCircle.setFill(color);
    }

    private void checkLastTurn() throws IOException {
        new Thread(() -> {
            boolean superendgame = false;
            while(!superendgame && !useless) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                boolean endgame = false;
                try {
                    for (GameField g : client.getMiniModel().getGame_fields()) {
                        if (g.getPlayer().getPlayerPoints() >= 20) {
                            endgame = true;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (endgame && getActivePlayer()) {
                        superendgame = true;
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Game Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Somebody has reached 20 points, last turn of the game!");
                            alert.showAndWait();
                        });

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    private boolean getActivePlayer() throws IOException {
        for(GameField g : client.getMiniModel().getGame_fields()){

            if(g.getPlayer().getActual_state().getNameState().equals("DRAW_CARD")
            ){
                return g.getPlayer().getIsFirst();
            }
        }
        return false;
    }

    private void startUpdatePlaying() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {


                        for(GameField g : client.getMiniModel().getGame_fields()){
                            if(g.getPlayer().getActual_state().getNameState().equals("DRAW_CARD")||
                                    g.getPlayer().getActual_state().getNameState().equals("PLACE_CARD")){
                                Platform.runLater(() -> updateCurrentPlayer(g.getPlayer().getName()));
                            }
                        }


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /*private void startMenuCheck() throws IOException {
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
    }*/

    /**
     * Handles mouse click events on the first card in the hand.
     *
     * @param mouseEvent The mouse event associated with the click.
     */
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

    /**
     * Handles mouse click events on the second card in the hand.
     *
     * @param mouseEvent The mouse event associated with the click.
     */
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

    /**
     * Handles mouse click events on the third card in the hand.
     *
     * @param mouseEvent The mouse event associated with the click.
     */
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

    /**
     * Opens a popup window with a message.
     *
     * @param i The message to display in the popup.
     */
    public void openPopup(int i){
        System.out.println("se arriva qua è sbagliato");
    }

    /**
     * Handles a mouse press event on the first card in the hand, detecting a long press.
     *
     * @param mouseEvent The mouse event associated with the press.
     */
    public void handleCard1Pressed(MouseEvent mouseEvent) {
        // Start a timer when the mouse is pressed on the card
        clickTimer = new Timer();
        clickTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // When the timer fires, set the flag to indicate that the click is maintained
                clickHeld = true;
                just_pressed = true;
            }
        }, 500); // Set the delay in milliseconds (for example, 500 milliseconds)
    }

    /**
     * Handles a mouse release event on the first card in the hand, performing actions based on click duration (short click vs long press) and game state.
     *
     * @param mouseEvent The mouse event associated with the release.
     * @throws IOException If there's an error opening the popup window (assuming openPopup throws IOException).
     */
    public void handleCard1Released(MouseEvent mouseEvent) throws IOException {

        // Stop the timer when the mouse is released
        clickTimer.cancel();

        // If the click is maintained, perform the desired action
        if (clickHeld) {
            if(super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                openPopup(1);
            }else{
                showError("IS NOT YOUR TURN, WAIT !!! ");
            }
        } else {
            // Otherwise, perform another action (if needed)
        }
        //Reset the flag for the next use
        clickHeld = false;

    }

    /**
     * Handles a mouse press event on the second card in the hand, detecting a long press.
     *
     * @param mouseEvent The mouse event associated with the press.
     */
    @FXML
    public void handleCard2Pressed(MouseEvent mouseEvent) {
        clickTimer = new Timer();
        clickTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                clickHeld = true;
                just_pressed = true;
            }
        }, 500);
    }

    /**
     * Handles a mouse release event on the second card in the hand, performing actions based on click duration (short click vs long press) and game state.
     *
     * @param mouseEvent The mouse event associated with the release.
     * @throws IOException If there's an error opening the popup window (assuming openPopup throws IOException).
     */
    @FXML
    public void handleCard2Released(MouseEvent mouseEvent) throws IOException {
        clickTimer.cancel();
        if (clickHeld) {
            if(super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                openPopup(2);
            }else{
                showError("IS NOT YOUR TURN, WAIT !!! ");
            }
        } else {
        }
        clickHeld = false;
    }

    /**
     * Handles a mouse press event on the third card in the hand, detecting a long press.
     *
     * @param mouseEvent The mouse event associated with the press.
     */
    public void handleCard3Pressed(MouseEvent mouseEvent) {
        clickTimer = new Timer();
        clickTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                clickHeld = true;
                just_pressed = true;
            }
        }, 500);
    }

    /**
     * Handles a mouse release event on the third card in the hand, performing actions based on click duration (short click vs long press) and game state.
     *
     * @param mouseEvent The mouse event associated with the release.
     * @throws IOException If there's an error opening the popup window (assuming openPopup throws IOException).
     */
    public void handleCard3Released(MouseEvent mouseEvent) throws IOException {
        clickTimer.cancel();
        if (clickHeld) {
            if(super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                openPopup(3);
            }else{
                showError("IS NOT YOUR TURN, WAIT !!! ");
            }
        } else {
        }
        clickHeld = false;
    }

    /**
     * Displays an error message to the user in a modal dialog window.
     *
     * @param message The error message to be displayed.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Handles the action of viewing the player's deck cards. This method is presumably triggered by an FXML button click.
     *
     * @throws IOException If there's an error loading the card images.
     */
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
            resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            Image image = new Image(resourceStream);
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
            resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            Image image = new Image(resourceStream);
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
            resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            Image image = new Image(resourceStream);
            cardImageView.setImage(image);


            // Add the label and image view to the VBox
            handBox.getChildren().add(cardLabel);
            handBox.getChildren().add(cardImageView);
        }
        else{
            String labelText =  "GOLD DECK is empty:";
            String imagePath = "src/resources/Card/Bianco.png";

            Label cardLabel = new Label(labelText);
            ImageView cardImageView = new ImageView();
            cardImageView.setFitHeight(133);
            cardImageView.setFitWidth(100);
            cardImageView.setPreserveRatio(true);

            // Load and set the image for the ImageView
            resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            Image image = new Image(resourceStream);
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
            resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            Image image = new Image(resourceStream);
            cardImageView.setImage(image);


            // Add the label and image view to the VBox
            handBox.getChildren().add(cardLabel);
            handBox.getChildren().add(cardImageView);
        }
        else{
            String labelText =  "RESOURCE DECK is empty:";
            String imagePath = "src/resources/Card/Bianco.png";

            Label cardLabel = new Label(labelText);
            ImageView cardImageView = new ImageView();
            cardImageView.setFitHeight(133);
            cardImageView.setFitWidth(100);
            cardImageView.setPreserveRatio(true);

            // Load and set the image for the ImageView
            resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            Image image = new Image(resourceStream);
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

    /**
     * Handles the action of viewing an opponent's playing field.
     * This method is presumably triggered by an FXML button click.
     *
     * @throws IOException If there's an error loading or displaying the opponent's field.
     */
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

    /**
     * Handles displaying the playing field of a specified opponent.
     * This method is presumably called when the user wants to see another player's field.
     *
     * @param playerNumber The player number (index) of the opponent whose field to display.
     * @throws IOException If there's an error loading card images.
     */
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
        //error probably here
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

    /**
     * Adds an image to a GridPane at the specified location with a border.
     *
     * @param grid the GridPane to add the image to
     * @param row the row index in the GridPane
     * @param col the column index in the GridPane
     * @param imagePath the path to the image file
     */
    private void addImageToGrid(GridPane grid, int row, int col, String imagePath) {
        resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
        Image image = new Image(resourceStream);

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(49.65 * 2);
        imageView.setFitHeight(33.1 * 2);

        StackPane stackPane = new StackPane();
        imageView.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10;");
        stackPane.getChildren().add(imageView);
        grid.add(stackPane, row, col);
    }

    /**
     * Updates the sets of visible rows and columns based on a given cell location.
     *
     * This method calculates and adds the visible row and column indices around
     * the specified cell (`row`, `col`) to the provided sets (`visibleRows` and `visibleCols`).
     * It considers the boundaries of the matrix defined by `Constants.MATRIXDIM`.
     *
     * @param visibleRows the set to store visible row indices
     * @param visibleCols the set to store visible column indices
     * @param row the row index of the cell
     * @param col the column index of the cell
     */
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

    /**
     * Hides rows/columns in GridPane based on visible sets.
     *
     * @param grid the GridPane to adjust
     * @param visibleRows rows to show
     * @param visibleCols cols to show
     */
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

    public void setPlayerName(String playerName) {
        playerNameLabel.setText(playerName);
    }

}
