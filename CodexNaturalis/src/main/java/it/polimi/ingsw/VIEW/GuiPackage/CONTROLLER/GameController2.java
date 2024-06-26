package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.CONTROLLER.GameFieldController;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.ChatIndexManager;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawCenter;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawGold;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawResource;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.VIEW.GuiPackage.ClickableCardImageView;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 *
 */
public class GameController2 extends GenericSceneController {

    public Label num_animal;
    public Label num_mushroom;
    public Label num_insect;
    public Label num_plant;
    public Label num_paper;
    public Label num_piuma;
    public Label num_calamaio;
    public ImageView playAreaImage;
    public ScrollPane scrollPane;

    @FXML
    private Button openChatButton;

    private ImageView dragPreview;

    private ChatIndexManager chat_manager = new ChatIndexManager();

    @FXML
    private Button showChat;
    private ResizeImage resizer;

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

    private ColorCoordinatesHelper helper = new ColorCoordinatesHelper();
    @FXML
    private ListView<String> chatMessages;

    @FXML
    private Label playerNameLabel;

    public ImageView gold_deck;
    public ImageView resurce_deck;
    public ImageView center_card_0;
    public ImageView center_card_1;
    public ImageView center_card_2;
    public ImageView center_card_3;
    public AnchorPane handBox;
    public ImageView handCard1;
    public ImageView handCard2;
    public ImageView handCard3;
    private Image handCard1image;
    private Image handCard2image;
    private Image handCard3image;
    public AnchorPane deckBox;

    @FXML
    private GridPane gameGrid;


    InputStream resourceStream;
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

    /**
     * deine a timer
     */
    private Timer clickTimer;

    /**
     * Define a variable to save if the click is maintained or not
     */
    private boolean clickHeld = false;
    private boolean just_pressed = false;
    String token_client;
    private SendFunction function;
    @FXML
    private ToggleButton chatToggleButton;

    @FXML
    private AnchorPane HeaderInclude;

    private List<Node> addedImageViews = new ArrayList<>();
    @FXML
    private Circle playerColorCircle;

    private ColorCoordinatesHelper helper2 = new ColorCoordinatesHelper();




    /**
     * Initializes the game board UI and player information.
     *
     * @throws IOException If an I/O error occurs during FXML loading.
     * @throws InterruptedException If the initialization process is interrupted.
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

        gameGrid.setHgap(25.5);
        gameGrid.setPrefHeight(730.0);
        gameGrid.setPrefWidth(730.0);
        gameGrid.setVgap(5);

        // Load background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/BackGroundImaging/8811189.jpg"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1500);
        backgroundImageView.setFitHeight(1500);

        // Add background image to GridPane
        gameGrid.getChildren().add(backgroundImageView);

        // Add column and row constraints
        for (int i = 0; i < 45; i++) {
            ColumnConstraints col = new ColumnConstraints(49.65);
            gameGrid.getColumnConstraints().add(col);

            RowConstraints row = new RowConstraints(33.1);
            gameGrid.getRowConstraints().add(row);
        }
        gameGrid.setHgap(25.5);
        gameGrid.setVgap(5);

        // Populate the GridPane with game data
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

        updateCardsInHand();
        updateDecks();
        updateCardsCenter();
         this.num_animal.setText( String.valueOf( client.getMiniModel().getMyGameField().getNumOfAnimal()));
         this.num_insect.setText(String.valueOf( client.getMiniModel().getMyGameField().getNumOfInsect()));
         this.num_mushroom.setText(String.valueOf( client.getMiniModel().getMyGameField().getNumOfMushroom()));
         this.num_plant.setText(String.valueOf( client.getMiniModel().getMyGameField().getNumOfPlant()));
         this.num_piuma.setText(String.valueOf( client.getMiniModel().getMyGameField().getNumOfFeather()));
         this.num_paper.setText(String.valueOf( client.getMiniModel().getMyGameField().getNumOfPaper()));
         this.num_calamaio.setText(String.valueOf( client.getMiniModel().getMyGameField().getNumOfPen()));

        token_client = client.getToken();
        setPlayerColor(helper.fromEnumtoColor(client.getMiniModel().getMy_player().getColor()));
        setPlayerName(client.getMiniModel().getMy_player().getName());
        checkLastTurn();


    }

    private boolean endgameAlertShown = false;

    private void checkLastTurn() throws IOException {
        boolean endgame = false;
        for (GameField g : client.getMiniModel().getGame_fields()) {
            if (g.getPlayer().getPlayerPoints() >= 20) {
                endgame = true;
                break; // Non ha senso continuare a controllare dopo aver trovato un giocatore con >= 20 punti
            }
        }

        if (endgame && !endgameAlertShown && getActivePlayer()) {
            endgameAlertShown = true; // Imposta la variabile di stato a true per evitare future visualizzazioni dell'alert
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Information");
                alert.setHeaderText(null);
                alert.setContentText("Somebody has reached 20 points, last turn of the game!");
                alert.showAndWait();
            });
        }
    }

    private boolean getActivePlayer() throws IOException {
        for(GameField g : client.getMiniModel().getGame_fields()){
            System.out.println(g.getPlayer().getActual_state().getNameState());
            if(g.getPlayer().getActual_state().getNameState().equals("DRAW_CARD")
            ){
                return g.getPlayer().getIsFirst();
            }
        }
        return false;
    }
    private void startLastPopup() {
        new Thread(() -> {
            while (true) {
                try {
                    if (client.getMiniModel().isFinal_state()) {
                        // Mostra il pop-up
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Game Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Somebody has reached 20 points, last turn of the game!");
                            alert.showAndWait();
                        });
                        break;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void setPlayerColor(Color color) {
        playerColorCircle.setFill(color);
    }

    /**
     * Adds an image to the game board grid at the specified location.
     *
     * @param row The row index where the image should be placed in the grid.
     * @param col The column index where the image should be placed in the grid.
     * @param imagePath The path to the image file to be displayed.
     */
    private void addImageToGrid(int row, int col, String imagePath) {
        resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);

         image = new Image(resourceStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(49.65 * 2);
        imageView.setFitHeight(33.1 * 2);

        StackPane stackPane = new StackPane();
        imageView.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10;");
        stackPane.getChildren().add(imageView);

        gameGrid.add(stackPane, col, row, 2, 2); // Occupy 2 columns and 2 rows
    }

    /**
     * Updates the sets of visible rows and columns based on the placement of a card on the game board.
     *
     * @param visibleRows A set containing the currently visible row indices in the grid.
     * @param visibleCols A set containing the currently visible column indices in the grid.
     * @param row The row index where the card is placed.
     * @param col The column index where the card is placed.
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
     * Adjusts the visibility of rows and columns in the game board grid based on the sets of visible indices.
     *
     * @param visibleRows A set containing the visible row indices in the grid.
     * @param visibleCols A set containing the visible column indices in the grid.
     */
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

    /**
     * Initializes the game board UI and related components. This method is called automatically when the FXML file is loaded.
     */
    public void initialize() {
        //  Initialization of other components
        // Add an event handler to the GridPane to deselect the card when clicking anywhere on the game field
        gameGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            //  Check if the card is selected and deselect it if necessary
            deselectCard();
        });
        deckBox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            //  Check if the card is selected and deselect it if necessary
            deselectCard();
        });
        handBox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            // Check if the click did not occur directly on one of the cards in the handBox
            Node clickedNode = event.getPickResult().getIntersectedNode();
            if (!(clickedNode instanceof ImageView && handBox.getChildren().contains(clickedNode))) {
                // Add an event handler to the GridPane to deselect the card when clicking anywhere on the game field
                deselectCard();
            }
        });

    }

    /**
     * Deselects the currently selected card in the hand or on the game board.
     */
    private void deselectCard() {
        handCard1.setStyle("-fx-border-width: 0;");
        handCard2.setStyle("-fx-border-width: 0;");
        handCard3.setStyle("-fx-border-width: 0;");
        removeAddedImages();
    }

    /**
     * Handles a click event on the first hand card (`handCard1`).
     *
     * @param mouseEvent The MouseEvent object associated with the click.
     */
    public void handleCard1Click(MouseEvent mouseEvent) {
        if (just_pressed){
            just_pressed = false;
            return;
        }
        removeAddedImages();
        if(!card_1_flip){
            handCard1.setImage(card_1_back);
            handCard1.setStyle("-fx-border-color: blue; -fx-border-width: 5px;");

            card_1_flip = true;
        }else{
            handCard1.setImage(card_1_front);
            handCard1.setStyle("-fx-border-color: blue; -fx-border-width: 5px;");

            card_1_flip = false;
        }
    }

    /**
     * Handles a click event on the first hand card (`handCard2`).
     *
     * @param mouseEvent The MouseEvent object associated with the click.
     */
    public void handleCard2Click(MouseEvent mouseEvent) {
        if (just_pressed){
            just_pressed = false;
            return;
        }
        removeAddedImages();
        if(!card_2_flip){
            handCard2.setImage(card_2_back);
            handCard2.setStyle("-fx-border-color: blue; -fx-border-width: 5px;");
            card_2_flip = true;
        }
        else{
            handCard2.setImage(card_2_front);
            handCard2.setStyle("-fx-border-color: blue; -fx-border-width: 5px;");
            card_2_flip = false;
        }
    }

    /**
     * Handles a click event on the third hand card (`handCard3`).
     *
     * @param mouseEvent The MouseEvent object associated with the click.
     */
    public void handleCard3Click(MouseEvent mouseEvent) {
        if (just_pressed){
            just_pressed = false;
            return;
        }
        removeAddedImages();
        if(!card_3_flip){
            handCard3.setImage(card_3_back);
            handCard3.setStyle("-fx-border-color: blue; -fx-border-width: 5px;");
            card_3_flip = true;
        }else{
            handCard3.setImage(card_3_front);
            handCard3.setStyle("-fx-border-color: blue; -fx-border-width: 5px;");
            card_3_flip = false;
        }
    }

    /**
     * Opens a dialog for the user to enter card placement coordinates.
     *
     * @param i The index of the selected card (likely 1, 2, or 3).
     */
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
                    // If it's not possible to parse the coordinates as numbers, handle the exception
                    System.err.println("Errore durante il parsing delle coordinate.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // If the coordinate string does not have the correct format, handle the error
                System.err.println("Formato delle coordinate non valido.");
            }
        }, () -> {
            // If the user has pressed 'Cancel', do nothing
            System.out.println("Popup chiuso senza inserire coordinate");
        });
    }

    /**
     * Handles a mouse press event on the first hand card (`handCard1`).
     *
     * @param mouseEvent The MouseEvent object associated with the press.
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
     * Handles a mouse release event on the first hand card (`handCard1`).
     *
     * @param mouseEvent The MouseEvent object associated with the release.
     * @throws IOException  In case of potential I/O exceptions during error message display (implementation detail).
     */
    public void handleCard1Released(MouseEvent mouseEvent) throws IOException {

        // Stop the timer when the mouse is released
        clickTimer.cancel();

        // If the click is maintained, perform the desired action
        if (clickHeld) {
            if(super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                removeAddedImages();
                for (int i = 0; i < Constants.MATRIXDIM - 1; i++) {
                    for (int j = 0; j < Constants.MATRIXDIM - 1; j++) {
                        if (helper.checkPlacing(card_1_flip, client.getMiniModel().getCards_in_hand().get(0), client.getMiniModel().getMyGameField(), i, j)) {
                            addClickableCardImageToGrid(1, i, j, client.getMiniModel().getCards_in_hand().get(0), "Card/Bianco.png");
                            System.out.println(i + " " + j);
                        }

                    }
                }
            }
            else{
                if(super.client.getMiniModel().getState().equals("DRAW_CARD")) {
                    showError("DRAW A CARD !!! ");
                }
                else{
                    showError("IS NOT YOUR TURN, WAIT !!! ");
                }
            }
            /*if(super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                openPopup(1);
            }else{
                showError("IS NOT YOUR TURN, WAIT !!! ");
            }*/
        }
        else {
            // Otherwise, perform another action (if needed)
        }

        // Reset the flag for the next use
        clickHeld = false;

    }

    /**
     * Handles a mouse press event on the second hand card (`handCard2`).
     *
     * @param mouseEvent The MouseEvent object associated with the press.
     */
    @FXML
    public void handleCard2Pressed(MouseEvent mouseEvent) {
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
     * Handles a mouse release event on the second hand card (`handCard2`).
     *
     * @param mouseEvent The MouseEvent object associated with the release.
     * @throws IOException  In case of potential I/O exceptions during error message display (implementation detail).
     */
    @FXML
    public void handleCard2Released(MouseEvent mouseEvent) throws IOException {
        // Stop the timer when the mouse is released
        clickTimer.cancel();

        // If the click is maintained, perform the desired action
        if (clickHeld) {
            if(super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                removeAddedImages();
                for (int i = 0; i < Constants.MATRIXDIM - 1; i++) {
                    for (int j = 0; j < Constants.MATRIXDIM - 1; j++) {
                        if (helper.checkPlacing(card_2_flip, client.getMiniModel().getCards_in_hand().get(1), client.getMiniModel().getMyGameField(), i, j)) {
                            addClickableCardImageToGrid(2, i, j, client.getMiniModel().getCards_in_hand().get(1), "Card/Bianco.png");
                            System.out.println(i + " " + j);
                        }

                    }
                }
            }
            else{
                if(super.client.getMiniModel().getState().equals("DRAW_CARD")) {
                    showError("DRAW A CARD !!! ");
                }
                else{
                    showError("IS NOT YOUR TURN, WAIT !!! ");
                }
            }
            /*if(super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                openPopup(2);
            }else{
                showError("IS NOT YOUR TURN, WAIT !!! ");
            }*/
        } else {
            // Otherwise, perform another action (if needed)
        }

        // Reset the flag for the next use
        clickHeld = false;

    }

    /**
     * Handles a mouse press event on the third hand card (`handCard3`).
     *
     * @param mouseEvent The MouseEvent object associated with the press.
     */
    public void handleCard3Pressed(MouseEvent mouseEvent) {
        // Start a timer when the mouse is pressed on the card
        clickTimer = new Timer();
        clickTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // When the timer fires, set the flag to indicate that the click is maintained
                clickHeld = true;
                just_pressed = true;
            }
        }, 500);// Set the delay in milliseconds (for example, 500 milliseconds)
    }

    /**
     * Handles a mouse release event on the third hand card (`handCard3`).
     *
     * @param mouseEvent The MouseEvent object associated with the release.
     * @throws IOException  In case of potential I/O exceptions during error message display (implementation detail).
     */
    public void handleCard3Released(MouseEvent mouseEvent) throws IOException {
        // Stop the timer when the mouse is released
        clickTimer.cancel();

        // If the click is maintained, perform the desired action
        if (clickHeld) {
            if(super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                removeAddedImages();
                for (int i = 0; i < Constants.MATRIXDIM - 1; i++) {
                    for (int j = 0; j < Constants.MATRIXDIM - 1; j++) {
                        if (helper.checkPlacing(card_3_flip,client.getMiniModel().getCards_in_hand().get(2), client.getMiniModel().getMyGameField(), i, j)) {
                            addClickableCardImageToGrid(3, i, j, client.getMiniModel().getCards_in_hand().get(2), "Card/Bianco.png");
                        }

                    }
                }
            }
            else{
                if(super.client.getMiniModel().getState().equals("DRAW_CARD")) {
                    showError("DRAW A CARD !!! ");
                }
                else{
                    showError("IS NOT YOUR TURN, WAIT !!! ");
                }
            }
            /*if(super.client.getMiniModel().getState().equals("PLACE_CARD")) {
                openPopup(3);
            }else{
                showError("IS NOT YOUR TURN, WAIT !!! ");
            }*/
        } else {
            // Otherwise, perform another action (if needed)
        }

        // Reset the flag for the next use
        clickHeld = false;

    }

    /**
     * Shows an error message to the user.
     *
     * @param message The message to display in the error dialog.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Shows a temporary loading popup to the user.
     */
    public void showLoadingPopup() {
        // Create an INFORMATION type alert for loading
        Alert loadingAlert = new Alert(Alert.AlertType.INFORMATION, "Loading...", ButtonType.OK);
        loadingAlert.setHeaderText(null);
        loadingAlert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false); // Nasconde il pulsante OK

        // show alert
        loadingAlert.show();

        // Create a timeline to close the alert after 1 second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            loadingAlert.close();
        }));

        // Start the timeline
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Sends a function object to the client.
     *
     * @param function The SendFunction object to be sent.
     * @throws IOException  In case of potential I/O exceptions during communication with the client.
     * @throws InterruptedException  In case the thread is interrupted while waiting for the client response.
     * @throws ClassNotFoundException  In case the client sends an unrecognized class type.
     */
    private void sendFunction(SendFunction function) throws IOException, InterruptedException, ClassNotFoundException {
        client.drawCard(function);
        showLoadingPopup();
        client.getTerminal_interface().manageGame();
    }

    /**
     * Handles a mouse click event on the gold deck UI element.
     *
     * @param mouseEvent The MouseEvent object associated with the click.
     * @throws IOException  In case of potential I/O exceptions during communication with the client.
     * @throws InterruptedException  In case the thread is interrupted while waiting for the client response.
     * @throws ClassNotFoundException  In case the client sends an unrecognized class type.
     */
    public void handleGoldDeckClick(MouseEvent mouseEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if(errorDrawState() == false){
            if(super.client.getMiniModel().getTop_gold() == null) {
                errorEmptyDeck();
            }else {
                function = new SendDrawGold(token_client);
                sendFunction(function);
                updateDecks();
            }
        }
    }

    /**
     * Handles a mouse click event on the resource deck UI element.
     *
     * @param mouseEvent The MouseEvent object associated with the click.
     * @throws IOException  In case of potential I/O exceptions during communication with the client.
     * @throws InterruptedException  In case the thread is interrupted while waiting for the client response.
     * @throws ClassNotFoundException  In case the client sends an unrecognized class type.
     */
    public void handleResourceDeckClick(MouseEvent mouseEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if(errorDrawState() == false){
            if(super.client.getMiniModel().getTop_resource() == null) {
                errorEmptyDeck();
            }else {
                function = new SendDrawResource(token_client);
                sendFunction(function);
                updateDecks();
            }
        }
    }

    /**
     * Handles a mouse click event on the center card at index 0.
     *
     * @param mouseEvent The MouseEvent object associated with the click.
     * @throws IOException  In case of potential I/O exceptions during communication with the client.
     * @throws InterruptedException  In case the thread is interrupted while waiting for the client response.
     * @throws ClassNotFoundException  In case the client sends an unrecognized class type.
     */
    public void handleCenterCard_0Click(MouseEvent mouseEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if(!errorDrawState()){
            function = new SendDrawCenter(token_client, 0);
            sendFunction(function);
            updateCardsCenter();
        }
    }

    /**
     * Handles a mouse click event on the center card at index 1.
     *
     * @param mouseEvent The MouseEvent object associated with the click.
     * @throws IOException  In case of potential I/O exceptions during communication with the client.
     * @throws InterruptedException  In case the thread is interrupted while waiting for the client response.
     * @throws ClassNotFoundException  In case the client sends an unrecognized class type.
     */
    public void handleCenterCard_1Click(MouseEvent mouseEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if(errorDrawState() == false){
            function = new SendDrawCenter(token_client, 1);
            sendFunction(function);
            updateCardsCenter();
        }
    }

    /**
     * Handles a mouse click event on the center card at index 2.
     *
     * @param mouseEvent The MouseEvent object associated with the click.
     * @throws IOException  In case of potential I/O exceptions during communication with the client.
     * @throws InterruptedException  In case the thread is interrupted while waiting for the client response.
     * @throws ClassNotFoundException  In case the client sends an unrecognized class type.
     */
    public void handleCenterCard_2Click(MouseEvent mouseEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if(errorDrawState() == false){
            function = new SendDrawCenter(token_client, 2);
            sendFunction(function);
            updateCardsCenter();
        }
    }

    /**
     * Handles a mouse click event on the center card at index 3.
     *
     * @param mouseEvent The MouseEvent object associated with the click.
     * @throws IOException  In case of potential I/O exceptions during communication with the client.
     * @throws InterruptedException  In case the thread is interrupted while waiting for the client response.
     * @throws ClassNotFoundException  In case the client sends an unrecognized class type.
     */
    public void handleCenterCard_3Click(MouseEvent mouseEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if(errorDrawState() == false){
            function = new SendDrawCenter(token_client, 3);
            sendFunction(function);
            updateCardsCenter();
        }
    }

    /**
     * Checks if the current game state allows drawing a card.
     *
     * @return True if drawing is not allowed, False otherwise.
     * @throws IOException  In case of potential I/O exceptions during error message display (implementation detail).
     */
    private boolean errorDrawState() throws IOException {
        if(! super.client.getMiniModel().getState().equals("DRAW_CARD")){
            showError("YOU CAN'T DRAW NOW !!! ");
            return true;
        }
        else return false;
    }

    private void errorEmptyDeck() throws IOException {
            showError("DECK IS EMPTY");
    }

    /**
     * Updates the visual representation of the resource and gold decks based on the game state.
     *
     * @throws IOException  In case of potential I/O exceptions during image loading.
     */
    private void updateDecks() throws IOException {
        if(super.client.getMiniModel().getTop_resource() == null){
            resourceStream = getClass().getClassLoader().getResourceAsStream("Card/Bianco.png");
            image = new Image(resourceStream);
            this.resurce_deck.setImage(image);
        }else{
            resourceStream = getClass().getClassLoader().getResourceAsStream(super.client.getMiniModel().getTop_resource().back_side_path);
            image = new Image(resourceStream);
            this.resurce_deck.setImage(image);
        }
        if(super.client.getMiniModel().getTop_gold() == null){
            resourceStream = getClass().getClassLoader().getResourceAsStream("Card/Bianco.png");
            image = new Image(resourceStream);
            this.gold_deck.setImage(image);
        }else{
            resourceStream = getClass().getClassLoader().getResourceAsStream(super.client.getMiniModel().getTop_gold().back_side_path);
            image = new Image(resourceStream);
            this.gold_deck.setImage(image);}
    }

    /**
     * Updates the visual representation of the center cards based on the game state.
     *
     * @throws IOException  In case of potential I/O exceptions during image loading.
     */
    private void updateCardsCenter() throws IOException {

        PlayCard card0 = super.client.getMiniModel().getCards_in_center().getGold_list().get(0);
        PlayCard card1 = super.client.getMiniModel().getCards_in_center().getGold_list().get(1);
        PlayCard card2 = super.client.getMiniModel().getCards_in_center().getResource_list().get(0);
        PlayCard card3 = super.client.getMiniModel().getCards_in_center().getResource_list().get(1);

        resourceStream = getClass().getClassLoader().getResourceAsStream(card0.front_side_path);
        image = new Image(resourceStream);
        this.center_card_0.setImage(image);

        resourceStream = getClass().getClassLoader().getResourceAsStream(card1.front_side_path);
        image = new Image(resourceStream);
        this.center_card_1.setImage(image);

        resourceStream = getClass().getClassLoader().getResourceAsStream(card2.front_side_path);
        image = new Image(resourceStream);
        this.center_card_2.setImage(image);

        resourceStream = getClass().getClassLoader().getResourceAsStream(card3.front_side_path);
        image = new Image(resourceStream);
        this.center_card_3.setImage(image);
    }

    /**
     * Updates the visual representation of the cards in the player's hand based on the game state.
     *
     * @throws IOException  In case of potential I/O exceptions during image loading.
     */
    private void updateCardsInHand() throws IOException {
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

    }

    /**
     * Adds a clickable card image to the game grid UI at a specific location.
     *
     * @param i  An index used for internal tracking.
     * @param row  The row position in the grid where the image will be placed.
     * @param col  The column position in the grid where the image will be placed.
     * @param card  The PlayCard data object associated with the image.
     * @param imagePath  The path to the image file representing the card.
     */
    private void addClickableCardImageToGrid(int i, int row, int col, PlayCard card, String imagePath) {
        resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
        Image image = new Image(resourceStream);

        ClickableCardImageView imageView = new ClickableCardImageView(image, card, row, col, i);
        imageView.setFitWidth(49.65 * 2);
        imageView.setFitHeight(33.1 * 2);

        StackPane stackPane = new StackPane();
        imageView.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10;");
        stackPane.getChildren().add(imageView);

        gameGrid.add(stackPane, col, row, 2, 2); // Occupa 2 colonne e 2 righe

        // Add the imageView to the list of added images
        addedImageViews.add(stackPane);

        // Add a mouse release event to handle the click
        imageView.setOnMouseReleased(event -> {
            try {
                handleCardImageClick(imageView);
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        imageView.setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

                event.consume();
            }
        });

        imageView.setOnDragExited(event -> {
            // Eventuale codice per gestire l'uscita del drag
            event.consume();
        });

        imageView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasImage()) {
                // Gestisci il rilascio del drag qui
                try {
                    handleCardImageClick(imageView);
                } catch (IOException | InterruptedException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        // fading animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), stackPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setCycleCount(1);
        fadeIn.play();
    }

    /**
     * Handles a click event on a ClickableCardImageView element in the game grid.
     *
     * @param imageView  The ClickableCardImageView object representing the clicked card.
     * @throws IOException  In case of potential I/O exceptions during communication with the client.
     * @throws InterruptedException  In case the thread is interrupted while waiting for the client response.
     * @throws ClassNotFoundException  In case the client sends an unrecognized class type.
     */
    private void handleCardImageClick(ClickableCardImageView imageView) throws IOException, InterruptedException, ClassNotFoundException {
        int row = imageView.getRow();
        int col = imageView.getCol();
        PlayCard card = imageView.getPlayCard();
        int i = imageView.getI();

        boolean flipped;
        if(i == 1){
            flipped = card_1_flip;
        } else
        if (i == 2 ) {
            flipped = card_2_flip;
        }else{
            flipped = card_3_flip;
        }

        client.selectAndInsertCard(i,row,col,flipped);
        showLoadingPopup();
        client.getTerminal_interface().manageGame();
        // Here you can handle the click event on the image, using the row and col coordinates.
        System.out.println("Clicked on image at row: " + row + ", col: " + col);
        // You can also access the PlayCard object associated with the clicked image.
        System.out.println("Associated PlayCard: " + card);
    }

    /**
     * Removes all previously added card images from the game grid with a fade-out animation.
     */
    private void removeAddedImages() {
        for (Node node : addedImageViews) {
            // Reverse fading animation before removing the image
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), node);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setCycleCount(1);
            fadeOut.setOnFinished(event -> gameGrid.getChildren().remove(node));
            fadeOut.play();
        }
        addedImageViews.clear(); // clear the list
    }

    @FXML
    private void handleCard1DragDetected(MouseEvent event) throws IOException {
        if (super.client.getMiniModel().getState().equals("PLACE_CARD")) {
            removeAddedImages();
            for (int i = 0; i < Constants.MATRIXDIM - 1; i++) {
                for (int j = 0; j < Constants.MATRIXDIM - 1; j++) {
                    if (helper.checkPlacing(card_1_flip, client.getMiniModel().getCards_in_hand().get(0), client.getMiniModel().getMyGameField(), i, j)) {
                        addClickableCardImageToGrid(1, i, j, client.getMiniModel().getCards_in_hand().get(0), "Card/Bianco.png");
                        System.out.println(i + " " + j);
                    }
                }
            }
        } else {
            if (super.client.getMiniModel().getState().equals("DRAW_CARD")) {
                showError("DRAW A CARD !!! ");
            } else {
                showError("IS NOT YOUR TURN, WAIT !!! ");
            }
        }

        Dragboard db = handCard1.startDragAndDrop(TransferMode.ANY);

        ClipboardContent content = new ClipboardContent();
        content.putImage(handCard1.getImage());
        db.setContent(content);

        Image originalImage = handCard1.getImage();
        handCard1image = originalImage;

        handCard1.setOpacity(1);

        // Imposta l'immagine di drag-and-drop con una dimensione specifica
        // Ottieni le dimensioni della handCard1
        double width = 100;
        double height = 75;
        Image resizedImage = new Image(originalImage.getUrl(), width, height, true, true);
        db.setDragView(resizedImage, 145, 60);


        // Imposta un'immagine bianca per la carta durante il drag
        /*File file = new File("Card/Bianco.png");
        Image blankImage = new Image(file.toURI().toString());*/
        handCard1.setImage(null);

        event.consume();
    }


    @FXML
    private void handleCard1MouseDragged(MouseEvent event) {
        // Sposta l'immagine della carta con il mouse
        handCard1.setLayoutX(event.getSceneX() - handCard1.getBoundsInLocal().getWidth() / 2);
        handCard1.setLayoutY(event.getSceneY() - handCard1.getBoundsInLocal().getHeight() / 2);
        event.consume();
    }

    @FXML
    private void handleCard1DragDone(DragEvent event) {
        // Reimposta la posizione dell'immagine della carta alla posizione originale
        handCard1.setLayoutX(145);
        handCard1.setLayoutY(60);

        // Reimposta l'immagine originale della carta
        handCard1.setImage(handCard1image);

        event.consume();
    }

    @FXML
    private void handleCard2DragDetected(MouseEvent event) throws IOException {
        if (super.client.getMiniModel().getState().equals("PLACE_CARD")) {
            removeAddedImages();
            for (int i = 0; i < Constants.MATRIXDIM - 1; i++) {
                for (int j = 0; j < Constants.MATRIXDIM - 1; j++) {
                    if (helper.checkPlacing(card_2_flip, client.getMiniModel().getCards_in_hand().get(1), client.getMiniModel().getMyGameField(), i, j)) {
                        addClickableCardImageToGrid(2, i, j, client.getMiniModel().getCards_in_hand().get(1), "Card/Bianco.png");
                        System.out.println(i + " " + j);
                    }
                }
            }
        } else {
            if (super.client.getMiniModel().getState().equals("DRAW_CARD")) {
                showError("DRAW A CARD !!! ");
            } else {
                showError("IS NOT YOUR TURN, WAIT !!! ");
            }
        }

        Dragboard db = handCard2.startDragAndDrop(TransferMode.ANY);

        ClipboardContent content = new ClipboardContent();
        content.putImage(handCard2.getImage());
        db.setContent(content);

        Image originalImage = handCard2.getImage();
        handCard2image = originalImage;

        handCard2.setOpacity(1);

        // Imposta l'immagine di drag-and-drop con una dimensione specifica
        // Ottieni le dimensioni della handCard1
        double width = 100;
        double height = 75;
        Image resizedImage = new Image(originalImage.getUrl(), width, height, true, true);
        db.setDragView(resizedImage, 144, 221);


        // Imposta un'immagine bianca per la carta durante il drag
        /*File file = new File("Card/Bianco.png");
        Image blankImage = new Image(file.toURI().toString());*/
        handCard2.setImage(null);

        event.consume();
    }


    @FXML
    private void handleCard2MouseDragged(MouseEvent event) {
        // Sposta l'immagine della carta con il mouse
        handCard2.setLayoutX(event.getSceneX() - handCard2.getBoundsInLocal().getWidth() / 2);
        handCard2.setLayoutY(event.getSceneY() - handCard2.getBoundsInLocal().getHeight() / 2);
        event.consume();
    }

    @FXML
    private void handleCard2DragDone(DragEvent event) {
        // Reimposta la posizione dell'immagine della carta alla posizione originale
        handCard2.setLayoutX(144);
        handCard2.setLayoutY(221);

        // Reimposta l'immagine originale della carta
        handCard2.setImage(handCard2image);

        event.consume();
    }

    @FXML
    private void handleCard3DragDetected(MouseEvent event) throws IOException {
        if (super.client.getMiniModel().getState().equals("PLACE_CARD")) {
            removeAddedImages();
            for (int i = 0; i < Constants.MATRIXDIM - 1; i++) {
                for (int j = 0; j < Constants.MATRIXDIM - 1; j++) {
                    if (helper.checkPlacing(card_3_flip, client.getMiniModel().getCards_in_hand().get(2), client.getMiniModel().getMyGameField(), i, j)) {
                        addClickableCardImageToGrid(3, i, j, client.getMiniModel().getCards_in_hand().get(2), "Card/Bianco.png");
                        System.out.println(i + " " + j);
                    }
                }
            }
        } else {
            if (super.client.getMiniModel().getState().equals("DRAW_CARD")) {
                showError("DRAW A CARD !!! ");
            } else {
                showError("IS NOT YOUR TURN, WAIT !!! ");
            }
        }

        Dragboard db = handCard3.startDragAndDrop(TransferMode.ANY);

        ClipboardContent content = new ClipboardContent();
        content.putImage(handCard3.getImage());
        db.setContent(content);

        Image originalImage = handCard3.getImage();
        handCard3image = originalImage;

        handCard3.setOpacity(1);

        // Imposta l'immagine di drag-and-drop con una dimensione specifica
        // Ottieni le dimensioni della handCard3
        double width = 100;
        double height = 75;
        Image resizedImage = new Image(originalImage.getUrl(), width, height, true, true);
        db.setDragView(resizedImage, 143, 382);

        // Imposta un'immagine bianca per la carta durante il drag
        /*File file = new File("Card/Bianco.png");
        Image blankImage = new Image(file.toURI().toString());*/
        handCard3.setImage(null);

        event.consume();
    }

    @FXML
    private void handleCard3MouseDragged(MouseEvent event) {
        // Sposta l'immagine della carta con il mouse
        handCard3.setLayoutX(event.getSceneX() - handCard3.getBoundsInLocal().getWidth() / 2);
        handCard3.setLayoutY(event.getSceneY() - handCard3.getBoundsInLocal().getHeight() / 2);
        event.consume();
    }

    @FXML
    private void handleCard3DragDone(DragEvent event) {
        // Reimposta la posizione dell'immagine della carta alla posizione originale
        handCard3.setLayoutX(143);
        handCard3.setLayoutY(382);

        // Reimposta l'immagine originale della carta
        handCard3.setImage(handCard3image);

        event.consume();
    }

    public void setPlayerName(String playerName) {
        playerNameLabel.setText(playerName);
    }

}