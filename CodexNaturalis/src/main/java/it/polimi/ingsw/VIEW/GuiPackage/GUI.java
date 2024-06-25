package it.polimi.ingsw.VIEW.GuiPackage;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawCenter;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawGold;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawResource;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.SOCKET_FINAL.clientSocket;
import it.polimi.ingsw.VIEW.GraficInterterface;
import javafx.application.Platform;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

/**
 * This class implements the GraficInterterface interface and acts as the bridge between the serverSocket communication logic and the JavaFX scene controller.
 * It handles user interactions, displays messages and updates the scene based on game state and serverSocket responses.
 */
public class GUI implements GraficInterterface {

    private boolean isAlone = false;

    public boolean newClient;
    SceneController scene;
    String username;
    private String token;
    private boolean flag_0 = false;

    public GUI(SceneController scene) {
        this.scene = scene;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Displays an error message on the scene using the scene controller.
     *
     * @param a The error message to be displayed.
     */
    @Override
    public void printError(String a) {
        scene.showAlert("ERROR!",a);
    }

    @Override
    public void printUpdateMessage(String message) {

    }

    /**
     * Starts the command-line interface for user interaction.
     * This method is currently unused as the application utilizes a graphical user interface.
     *
     * @throws IOException If an I/O error occurs during communication.
     * @throws InterruptedException If the connection thread is interrupted.
     * @throws NotBoundException If the RMI registry is not found.
     * @throws ClassNotFoundException If a class used during communication cannot be found.
     */
    @Override
    public void runCli() throws IOException, InterruptedException, NotBoundException, ClassNotFoundException {
        Platform.runLater(() -> scene.changeRootPane("IntroductionPage.fxml"));

    }

    public void setNewClient(boolean newClient) {
        this.newClient = newClient;
    }

    @Override
    public boolean getIsAlone() {
        return this.isAlone;
    }

    /**
     * Prompts the user to enter a username and initiates the login process.
     * This method utilizes Platform.runLater to run UI updates on the JavaFX thread.
     *
     * @throws IOException If an I/O error occurs during FXML loading.
     * @throws NotBoundException If the RMI registry is not found (relevant for RMI communication).
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the connection thread is interrupted.
     * @return always null
     */
    @Override
    public String selectNamePlayer() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        Platform.runLater(() -> scene.changeRootPane("login_scene.fxml"));
        return null;
    }

    /**
     * Handles user access to the game after successful login.
     *
     * @param player_name The username of the player.
     * @throws IOException If an I/O error occurs during communication.
     * @throws NotBoundException If the RMI registry is not found (relevant for RMI communication).
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the connection thread is interrupted.
     */
    @Override
    public void gameAccess(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        username = player_name;
        if(newClient) {
            makeChoice(player_name);
        }else{
            this.waitFullGame();
        }
    }

    /**
     * Prompts the user (for new clients only) to choose between creating a new game or joining an existing one.
     * This method utilizes Platform.runLater to run UI updates on the JavaFX thread.
     *
     * @param player_name The username of the player.
     * @throws IOException If an I/O error occurs during FXML loading.
     * @throws NotBoundException If the RMI registry is not found (relevant for RMI communication).
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the connection thread is interrupted.
     */
    private void makeChoice(String player_name) throws NotBoundException, IOException, ClassNotFoundException, InterruptedException {
        if (!scene.getClient().areThereFreeGames()) {
            newGame(player_name,false);
            return;
        }
        int done=0;
        Platform.runLater(() -> scene.changeRootPane("choose_new_or_old.fxml"));
    }

    /**
     * Handles creating a new game.
     *
     * @param player_name The username of the player.
     * @param empty A flag indicating if there are no existing games available (passed from makeChoice).
     * @throws IOException If an I/O error occurs during communication.
     * @throws NotBoundException If the RMI registry is not found (relevant for RMI communication).
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the connection thread is interrupted.
     */
    public void newGame(String player_name, boolean empty) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        if( !empty ) scene.showMessage("Message","There aren't existing games!");
        Platform.runLater(() -> scene.changeRootPane("game_access.fxml"));
    }

    /**
     * Prompts the user to choose a match to join.
     * This method utilizes Platform.runLater to run UI updates on the JavaFX thread.
     *
     * @param player_name The username of the player.
     * @throws IOException If an I/O error occurs during FXML loading.
     * @throws NotBoundException If the RMI registry is not found (relevant for RMI communication).
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the connection thread is interrupted.
     */
    @Override
    public void chooseMatch(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        Platform.runLater(() -> scene.changeRootPane("game_list_scene.fxml"));
    }

    @Override
    public boolean getInGame() {
        return false;
    }

    @Override
    public void endGame() {
        Platform.runLater(() -> scene.changeRootPane("end_game.fxml"));
    }

    /**
     * Displays a waiting scene while waiting for enough players to join the game.
     *
     * @throws IOException If an I/O error occurs during FXML loading.
     * @throws InterruptedException If the UI thread is interrupted.
     */
    @Override
    public void waitFullGame() throws IOException, InterruptedException {
        if(isAlone) return;
        Platform.runLater(() -> scene.changeRootPane("waiting_player.fxml"));
    }

    /**
     * Handles the "choose goal" state in the game.
     *
     * If a goal card has not yet been placed by the serverSocket (checkGoal is false), it delegates to `chooseStartingCardState`.
     * Otherwise, it calls `chooseGoal` to prompt the user (or handle UI updates) to select a goal card.
     *
     * @throws IOException If an I/O error occurs during communication or FXML loading.
     * @throws InterruptedException If the connection or UI thread is interrupted.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     */
    @Override
    public void chooseGoalState() throws IOException, InterruptedException, ClassNotFoundException {
        if(isAlone) return;
        if(scene.getClient().getMiniModel().getState().equals("CHOOSE_GOAL")) {
            boolean checkGoal = scene.getClient().isGoalCardPlaced();
                chooseGoal();
        }else{
            this.chooseStartingCardState();
        }
    }

    /**
     * Displays the scene for choosing a goal card.
     *
     * @throws IOException If an I/O error occurs during FXML loading.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the UI thread is interrupted.
     */
    private void chooseGoal() throws IOException, ClassNotFoundException, InterruptedException {
        if(isAlone) return;
        Platform.runLater(() -> scene.changeRootPane("choose_goal_state.fxml"));
    }

    /**
     * Handles the "choose starting card" state in the game.
     *
     * @throws IOException If an I/O error occurs during communication or FXML loading.
     * @throws InterruptedException If the connection or UI thread is interrupted.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     */
    @Override
    public void chooseStartingCardState() throws IOException, InterruptedException, ClassNotFoundException {
        if(isAlone) return;
        if(scene.getClient().getMiniModel().getState().equals("CHOOSE_SIDE_FIRST_CARD")) {
            chooseStartingCard();
        }else{
            this.manageGame();
        }
    }

    /**
     * Displays the scene for choosing a starting card.
     *
     * @throws IOException If an I/O error occurs during FXML loading.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the UI thread is interrupted.
     */
    private void chooseStartingCard() throws IOException, ClassNotFoundException, InterruptedException {
        if(isAlone) return;
        Platform.runLater(() -> scene.changeRootPane("starting_card.fxml"));
    }

    /**
     * Displays the scene for placing a card in the game.
     * This method is called when the game state is "PLACE_CARD" or "DRAW_CARD".
     */
    public void place_card(){
        Platform.runLater(() -> scene.changeRootPane("game2.fxml"));
    }

    /**
     * Displays the waiting scene while it's another player's turn.
     *
     */
    public void wait_turn(){
        Platform.runLater(() -> scene.changeRootPane("game_wait.fxml"));
    }

    /**
     * Manages the main game loop based on the current game state.
     *
     * @throws IOException If an I/O error occurs during communication or FXML loading.
     * @throws InterruptedException If the connection or UI thread is interrupted.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     */
    @Override
    public void manageGame() throws IOException, InterruptedException, ClassNotFoundException {
        if(isAlone) return;
        if(!scene.getClient().getMiniModel().getState().equals("END_GAME") ){
           if( scene.getClient().getMiniModel().getState().equals("PLACE_CARD")  || scene.getClient().getMiniModel().getState().equals("DRAW_CARD") ) this.place_card();
           else this.wait_turn();
        }else{
            endGame();
        }
    }

    @Override
    public void startCountdown(String message, boolean still_alone, boolean win) throws InterruptedException, NotBoundException, IOException, ClassNotFoundException {

            if (!flag_0) {
                if (!isAlone) {
                    isAlone = true;
                    Platform.runLater(() -> scene.changeRootPane("alone.fxml"));
                    Platform.runLater(() -> scene.getActiveController().updateMessageServer(message));
                } else {
                    if (still_alone) {
                        if (win) {
                            this.flag_0 = true;
                        }
                        Platform.runLater(() -> scene.getActiveController().updateMessageServer(message));
                    } else {
                        this.isAlone = false;
                        Platform.runLater(() -> scene.getActiveController().updateMessageServer(message));
                        Thread.sleep(1000);
                        newClient = false;
                        System.out.println("ciao");
                        this.gameAccess(null);
                    }
                }
            }

    }


    @Override
    public String getName() {
        return this.username;
    }

    public void setToken(String token){this.token =token;}

    /**
     *
     * @throws InterruptedException  If the thread is interrupted while waiting.
     */
    public void buffering() throws  InterruptedException{}

}
