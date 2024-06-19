package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.RMI_FINAL.ChatIndexManager;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract base class for scene controllers in the graphical user interface (GUI)
 * with chat and opponent field functionalities.
 *
 */
public class HeaderController extends GenericSceneController {

    @FXML
    private MenuBar menuBar;
    @FXML
    private VBox chatBox;

    private boolean chatOpen = false;

    private Stage opponentFieldStage;
    @FXML
    private Menu chatsMenu;
    private ChatIndexManager chat_manager = new ChatIndexManager();
    private VirtualViewF the_client;
    GenericSceneController upper_controller ;
    SceneController scene_controller;
    ColorCoordinatesHelper helper;

    private Stage scoreboardStage;

    public void setUpper_controller(GenericSceneController upper_controller) {
        this.upper_controller = upper_controller;
    }

    public void setThe_client(VirtualViewF the_client) {
        this.the_client = the_client;
    }

    /**
     * Starts a thread that updates the chat menu header.
     *
     * @throws IOException If an I/O error occurs while fetching data.
     */
    public void startInitializeHeader() throws IOException {
        Thread menuUpdater = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    chatsMenu.getItems().clear();
                    int i = 1;
                    try {
                        if (the_client.getMiniModel().getNum_players() > 2) {
                            for (i = 1; i <= the_client.getMiniModel().getNum_players(); ++i) {
                                if (i != the_client.getMiniModel().getMy_index()) {
                                    addChatItem("-CHAT WITH PLAYER " + the_client.getMiniModel().getNum_to_player().get(i) + " - New messages (" + the_client.getMiniModel().getNot_read().get(chat_manager.getChatIndex(the_client.getMiniModel().getMy_index(), i)) + ")", i);
                                }
                            }
                            addChatItem("PUBLIC CHAT" + " - New messages (" + the_client.getMiniModel().getNot_read().get(6) + ")", i);
                        } else {
                            addChatItem("PUBLIC CHAT" + " - New messages (" + the_client.getMiniModel().getNot_read().get(6) + ")", i);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        menuUpdater.start();
        if(
            the_client.getMiniModel().getState().equals("PLACE_CARD")||
            the_client.getMiniModel().getState().equals("DRAW_CARD") ||
            the_client.getMiniModel().getState().equals("WAIT_TURN")
        ){
            addGoalMenu();
        }
    }

    private void addGoalMenu() {
        // Create the GoalMenu and its items
        Menu goalMenu = new Menu("View GOAL");
        goalMenu.setId("GoalMenu");

        MenuItem personalGoal = new MenuItem("Personal Goal");
        personalGoal.setId("PersonalGoal");
        personalGoal.setOnAction(event -> {
            try {
                showPersonalGoal(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        MenuItem commonGoal = new MenuItem("Common Goal");
        commonGoal.setId("CommonGoal");
        commonGoal.setOnAction(event -> {
            try {
                showCommonGoal(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Add items to GoalMenu
        goalMenu.getItems().addAll(personalGoal, commonGoal);

        // Add GoalMenu to the MenuBar
        menuBar.getMenus().add(goalMenu);
    }
    /**
     * Adds a new message to the chat box.
     *
     * @param message The message content to be displayed.
     */
    private void addMessageToChatBox(String message) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        chatBox.getChildren().add(messageLabel);
    }

    /**
     * Adds a chat item to the chat menu.
     *
     * @param chatName The name of the chat to be displayed.
     * @param chatId The ID of the chat.
     */
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

    /**
     * Shows the chat window for a specific chat.
     *
     * @param chatName The name of the chat to be displayed.
     * @param chatId The ID of the chat.
     * @throws IOException If an I/O error occurs while showing the chat.
     */
    private void showChat(String chatName, int chatId) throws IOException {
        if(!isChatOpen()){
            chatOpen=true;
            if (this.the_client.getMiniModel().getNum_players() != 2) {
                if (chatId == this.the_client.getMiniModel().getNum_players() + 1) {
                    this.the_client.getMiniModel().getNot_read().set(6, 0);
                    updateUnreadTotal();
                    scene_controller.showChat("Chat", 6, this.the_client, chatId, this);
                } else {
                    this.the_client.getMiniModel().getNot_read().set(chat_manager.getChatIndex(this.the_client.getMiniModel().getMy_index(), chatId), 0);
                    updateUnreadTotal();
                    scene_controller.showChat("Chat", chat_manager.getChatIndex(this.the_client.getMiniModel().getMy_index(), chatId), this.the_client, chatId, this);
                }
            } else {
                this.the_client.getMiniModel().getNot_read().set(6, 0);
                updateUnreadTotal();
                scene_controller.showChat("Chat", 6, this.the_client, chatId, this);

            }
        }
        /*chatBox.getChildren().clear();  //
        chatBox.setVisible(true);
        System.out.println("chat is now visible: " + chatBox.isVisible());*/
    }

    /**
     * Calculates and sets the total unread message count for the client.
     *
     * @throws IOException If an I/O error occurs while fetching data from the client model.
     */
    private void updateUnreadTotal() throws IOException {
        this.the_client.getMiniModel().setUnread_total(0);
        for (Integer i : this.the_client.getMiniModel().getNot_read()) {
            this.the_client.getMiniModel().setUnread_total(this.the_client.getMiniModel().getUnread_total() + i);
        }
    }

    public void setScene(SceneController sceneController) {
        this.scene_controller=sceneController;
    }

    public void setChatOpen(boolean open) {
        this.chatOpen = open;
    }

    // Aggiorna il flag e restituisce il suo stato

    /**
     * Checks if a chat window is currently open.
     *
     * @return True if a chat window is open, false otherwise.
     */
    public boolean isChatOpen() {
        return chatOpen;
    }

    /**
     * Displays a pop-up window showing the game scoreboard.
     *
     * @throws IOException If an I/O error occurs while loading the scoreboard image.
     */
    @FXML
    private void showScoreboard() throws IOException {
        // Check if the scoreboard popup is already open
        if (scoreboardStage != null && scoreboardStage.isShowing()) {
            return;
        }

        scoreboardStage = new Stage();
        scoreboardStage.initModality(Modality.NONE); // Make the window non-blocking
        scoreboardStage.setTitle("Scoreboard");
        scoreboardStage.setResizable(false); // Make the window non-resizable

        // Set the owner of the popup stage to the main stage
        scoreboardStage.initOwner(scene_controller.getActiveScene().getWindow()); // Adjust according to how you access the main stage

        File file = new File("src/resources/imgMirk/scoreTable.png");
        Image image = new Image(file.toURI().toString());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(315); // Max width
        imageView.setFitHeight(630); // Max height
        imageView.setPreserveRatio(true);

        ColorCoordinatesHelper helper = new ColorCoordinatesHelper();
        List<CircleData> circles = new ArrayList<>();
        for (int i = 0; i < the_client.getMiniModel().getNum_players(); i++) {
            GameField g = the_client.getMiniModel().getGame_fields().get(i);
            System.out.println(helper.fromEnumtoColor(g.getPlayer().getColor()) + " " + helper.fromPointstoX(g.getPlayer(), g.getPlayer().getColor()) + " " + helper.fromPointstoY(g.getPlayer(), g.getPlayer().getColor()));
            circles.add(new CircleData(
                    helper.fromPointstoX(g.getPlayer(), g.getPlayer().getColor()),
                    helper.fromPointstoY(g.getPlayer(), g.getPlayer().getColor()),
                    10,
                    helper.fromEnumtoColor(g.getPlayer().getColor())
            ));
        }

        Pane circlesPane = new Pane();
        for (CircleData data : circles) {
            Circle circle = new Circle(data.x, data.y, data.radius);
            circle.setFill(data.color);
            circlesPane.getChildren().add(circle);
        }

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(imageView, circlesPane);

        VBox vbox = new VBox(stackPane);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Scene scene = new Scene(vbox, 315, 630); // Set the scene size to match the max dimensions of the image
        scoreboardStage.setScene(scene);
        scoreboardStage.show();

        // Set the event handler to nullify the stage reference when the popup is closed
        scoreboardStage.setOnHidden(event -> scoreboardStage = null);
    }

    /**
     * Disconnects the client from the server when the disconnect button is pressed.
     *
     * @param actionEvent The event triggered by the disconnect button.
     * @throws IOException If an I/O error occurs during communication with the server.
     * @throws NotBoundException If the client is not properly connected to the server.
     * @throws ClassNotFoundException If a class used during communication cannot be found.
     * @throws InterruptedException If the communication thread is interrupted.
     */
    public void handleDisconnect(ActionEvent actionEvent) throws NotBoundException, IOException, ClassNotFoundException, InterruptedException {
        the_client.disconect();
    }

    public void showPersonalGoal(ActionEvent actionEvent) throws IOException {
        // Create a new stage for the pop-up
        Stage stage = new Stage();
        stage.setTitle("Deck Cards");

        // Create a VBox
        VBox handBox = new VBox();
        handBox.setAlignment(javafx.geometry.Pos.CENTER);
        handBox.setMaxWidth(600);
        handBox.setSpacing(20);

        ImageView cardImageView = new ImageView();
        cardImageView.setFitHeight(400);
        cardImageView.setFitWidth(300);
        cardImageView.setPreserveRatio(true);


        Goal personalGoal_1 = the_client.getMiniModel().getMyGameField().getPlayer().getGoalCard();


        // Load and set the image for the ImageView
        File file = new File(personalGoal_1.front_side_path);
        Image image = new Image(file.toURI().toString());
        cardImageView.setImage(image);

        // Add the label and image view to the VBox
        handBox.getChildren().add(cardImageView);


        // Set the scene for the new stage
        Scene scene = new Scene(handBox);
        stage.setScene(scene);

        // Optionally, explicitly set the popup to be non-modal (if needed)
        stage.initModality(Modality.NONE); // This line ensures the stage is non-modal

        // Show the new stage without waiting
        stage.show();


    }

    public void showCommonGoal(ActionEvent actionEvent) throws IOException {
        // Create a new stage for the pop-up
        Stage stage = new Stage();
        stage.setTitle("Deck Cards");

        // Create a VBox
        VBox handBox = new VBox();
        handBox.setAlignment(javafx.geometry.Pos.CENTER);
        handBox.setMaxWidth(600);
        handBox.setSpacing(20);

        ImageView cardImageView = new ImageView();
        cardImageView.setFitHeight(400);
        cardImageView.setFitWidth(300);
        cardImageView.setPreserveRatio(true);

        Goal personalGoal_1 = the_client.getMiniModel().getMyGameField().getGlobal_goal1();

        // Load and set the image for the ImageView
        File file = new File(personalGoal_1.front_side_path);
        Image image = new Image(file.toURI().toString());
        cardImageView.setImage(image);

        // Add the label and image view to the VBox
        handBox.getChildren().add(cardImageView);

        ImageView cardImageView_2 = new ImageView();
        cardImageView_2.setFitHeight(400);
        cardImageView_2.setFitWidth(300);
        cardImageView_2.setPreserveRatio(true);

        Goal personalGoal_2 = the_client.getMiniModel().getMyGameField().getGlobal_goal2();


        // Load and set the image for the ImageView
        file = new File(personalGoal_2.front_side_path);
        image = new Image(file.toURI().toString());
        cardImageView_2.setImage(image);

        // Add the label and image view to the VBox
        handBox.getChildren().add(cardImageView_2);

        // Set the scene for the new stage
        Scene scene = new Scene(handBox);
        stage.setScene(scene);

        // Optionally, explicitly set the popup to be non-modal (if needed)
        stage.initModality(Modality.NONE); // This line ensures the stage is non-modal

        // Show the new stage without waiting
        stage.show();
    }

    /**
     * A simple data class representing information for a circle on the scoreboard.
     */
    private static class CircleData {
        double x, y, radius;
        Color color;

        CircleData(double x, double y, double radius, Color color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }
    }

    /**
     * Allows the user to select an opponent's field and displays it in a separate window.
     *
     * @throws IOException If an I/O error occurs while fetching data or displaying the field.
     */
    @FXML
    private void handleViewOpponentFields() throws IOException {

        if (opponentFieldStage != null && opponentFieldStage.isShowing()) {
            return;
        }

        // Create a list of player descriptions from the map
        List<String> playerDescriptions = the_client.getMiniModel().getNum_to_player().entrySet().stream()
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
     * Displays a pop-up window showing the selected opponent's game field.
     *
     * @param playerNumber The ID of the opponent player.
     * @throws IOException If an I/O error occurs while fetching data or loading images.
     */
    @FXML
    private void showOpponentField(int playerNumber) throws IOException {
        // Check if the opponent field popup is already open


        // Create a new stage for the pop-up
        opponentFieldStage = new Stage();
        opponentFieldStage.setTitle("Campo da gioco di Player " + playerNumber + " - " + the_client.getMiniModel().getNum_to_player().get(playerNumber));
        opponentFieldStage.setResizable(false); // Make the window non-resizable

        // Set the owner of the popup stage to the main stage
        opponentFieldStage.initOwner(scene_controller.getActiveScene().getWindow()); // Adjust according to how you access the main stage

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

        // Populate the GridPane with game data
        Set<Integer> visibleRows = new HashSet<>();
        Set<Integer> visibleCols = new HashSet<>();

        int count = 1;
        int tmp = 0;
        while (count <= the_client.getMiniModel().game_fields.get(playerNumber - 1).card_inserted) {
            for (int i = 0; i < Constants.MATRIXDIM; i++) {
                for (int j = 0; j < Constants.MATRIXDIM; j++) {
                    if (the_client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getOrder_above() == count) {
                        tmp = count;
                        count = 1500;
                        if (the_client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().flipped) {
                            addImageToGrid(gameGrid, i, j, the_client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().back_side_path);
                        } else {
                            addImageToGrid(gameGrid, i, j, the_client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().front_side_path);
                        }
                        updateVisibleIndices(visibleRows, visibleCols, i, j);
                    } else if (the_client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getOrder_below() == count) {
                        tmp = count;
                        count = 1500;
                        if (the_client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().flipped) {
                            addImageToGrid(gameGrid, i, j, the_client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().back_side_path);
                        } else {
                            addImageToGrid(gameGrid, i, j, the_client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().front_side_path);
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
        opponentFieldStage.setScene(scene);

        // Optionally, explicitly set the popup to be non-modal (if needed)
        opponentFieldStage.initModality(Modality.NONE); // This line ensures the stage is non-modal

        // Show the new stage without waiting
        opponentFieldStage.show();

        // Set the event handler to nullify the stage reference when the popup is closed
        opponentFieldStage.setOnHidden(event -> opponentFieldStage = null);
    }

    /**
     * Adds an image representation of a card to the opponent's game field grid.
     *
     * @param grid The GridPane representing the opponent's game field.
     * @param row The row index in the grid.
     * @param col The column index in the grid.
     * @param imagePath The path to the card image.
     */
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

    /**
     * Updates the sets of visible rows and columns based on a cell's position on the game field.
     *
     * @param visibleRows The set containing currently visible rows.
     * @param visibleCols The set containing currently visible columns.
     * @param row The row index of the cell.
     * @param col The column index of the cell.
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
     * Hides rows and columns of the opponent's game field grid that are not
     * needed to display the visible cards.
     *
     * @param grid The GridPane representing the opponent's game field.
     * @param visibleRows The set containing currently visible rows.
     * @param visibleCols The set containing currently visible columns.
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
}

