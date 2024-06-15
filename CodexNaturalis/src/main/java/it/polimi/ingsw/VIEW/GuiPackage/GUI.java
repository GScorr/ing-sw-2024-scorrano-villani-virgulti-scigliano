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
 * This class implements the GraficInterterface interface and acts as the bridge between the server communication logic and the JavaFX scene controller.
 * It handles user interactions, displays messages and updates the scene based on game state and server responses.
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
        selectNamePlayer();
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

        /*while(done==0) {
            System.out.println("\n-'new' CREATE NEW GAME \n-'old' JOIN EXISTING GAME");
            String decision = scan.nextLine();
            if (decision.equalsIgnoreCase("old")) {
                done = 1;
                chooseMatch(player_name);
            } else if (decision.equalsIgnoreCase("new")) {
                done=1;
                newGame(player_name,true);
            } else {
                System.out.println("\n[ERROR] WRONG INSERT!");
            }
        }*/
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
     * If a goal card has not yet been placed by the server (checkGoal is false), it delegates to `chooseStartingCardState`.
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

    /**
     * This method is currently unused due to the graphical user interface implementation.
     *
     * It previously handled in-game menu choices for text-based clients.
     *
     * @param message The message to be displayed in the menu.
     * @param current_state The current state of the game.
     * @throws IOException If an I/O error occurs during communication.
     */
    private void menuChoice(String message, String current_state) throws IOException {
        Scanner scan = new Scanner(System.in);
        scene.getClient().getMiniModel().printMenu(message);
        int choice = scan.nextInt();
        switch (choice) {
            case (0):
                scene.getClient().getMiniModel().printNumToField();
                int i = scan.nextInt();
                scene.getClient().getMiniModel().showGameField(i);
                break;
            case (1):
                scene.getClient().getMiniModel().showCards();
                break;
            case (2):
                scene.getClient().getMiniModel().uploadChat();
                int decision;
                do {
                    decision = scan.nextInt();
                    scan.nextLine();
                } while (!chatChoice(decision));
                break;
            case (3):
                return;
            default:
                System.err.println("[ERROR] WRONG INSERT");
        }
        menuChoice(message, current_state);
    }

    /**
     *
     * handle chat interaction within the game for text-based clients.
     *
     * @param decision An integer representing the chosen chat channel.
     * @return Originally indicated successful chat interaction.
     * @throws IOException If an I/O error occurs during communication.
     */
    private boolean chatChoice(int decision) throws IOException {
        scene.getClient().getMiniModel().setStop_chat(false);
        Scanner scan = new Scanner(System.in);
        if(!scene.getClient().getMiniModel().showchat(decision)){
            return false;
        }
        int choice = 0;
        while(true) {
            while (choice < 1 || choice > 2) {
                System.out.println("DO YOU WANT TO SEND A MESSAGE?     1- YES      2- NO [CLOSE CHAT] ");
                choice = scan.nextInt();
                scan.nextLine();
            }
            if (choice == 1) {
                System.out.println("INSERT MESSAGE: ");
                String message = scan.nextLine();
                scene.getClient().ChatChoice(message, decision);
                System.out.println("[SUCCESS] MESSAGE SENT!");
                choice = 0;
            } else {
                scene.getClient().getMiniModel().setStop_chat(true);
                return true;}
        }

    }

    /**
     *
     * It  handles in-game card selection and placement for text-based clients.
     *
     * @throws IOException If an I/O error occurs during communication.
     * @throws InterruptedException If the connection thread is interrupted.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     */
    private void selectAndInsertCard() throws IOException, InterruptedException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        int choice=-1,x,y;
        String flip;
        boolean flipped;
        while ( scene.getClient().getMiniModel().getState().equals("PLACE_CARD") ){
            menuChoice("PLACE CARD",scene.getClient().getMiniModel().getState());
            do{
                if(choice != -1) System.err.println("[ERROR] INCORRECT INSERT");
                System.out.println("\nCHOOSE CARD FROM YOUR DECK (1,2,3): ");
                String choicestring = scan.nextLine();
                choice = Integer.parseInt(choicestring);
                System.out.println("\nCHOOSE SIDE (B,F): ");
                flip = scan.nextLine();
                flipped = (flip.equals("B") || flip.equals("b"));
                System.out.println("\nCHOOSE COORDINATES: ");
                x = scan.nextInt();
                y = scan.nextInt();
                scan.nextLine();
            }while( !(choice>=1 && choice<=3) ||
                    !(flip.equals("B") || flip.equals("F")||flip.equals("b")||flip.equals("f") ) ||
                    !(x>=0 && x< Constants.MATRIXDIM && y>=0 && y<Constants.MATRIXDIM ));
            scene.getClient().selectAndInsertCard(choice,x,y,flipped);
        }
    }

    /**
     * This method is currently unused due to the graphical user interface implementation.
     *
     * It previously handled in-game card drawing selection for text-based clients.
     *
     * @throws IOException If an I/O error occurs during communication.
     * @throws InterruptedException If the connection thread is interrupted.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     */
    private void drawCard() throws IOException, InterruptedException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n DRAW A CARD FROM: ");
        boolean goldDeck_present = scene.getClient().isGoldDeckPresent();
        boolean resourceDeck_present = scene.getClient().isResourceDeckPresent();

        if(goldDeck_present){System.out.println("1. GOLD DECK");}
        if (resourceDeck_present){System.out.println("2. RESOURCE DECK");}
        System.out.println("3. CENTRAL CARDS DECK");
        int num = -1;
        SendFunction function = null;
        String token_client;
        do{
            if(num != -1) System.err.println("[ERROR] OUT OF BOUND");
            String numstring = scan.nextLine();
            num = Integer.parseInt(numstring);
            if(scene.getClient() instanceof clientSocket) token_client = scene.getClient().getToken();
            else token_client = token;
            switch (num) {
                case (1):
                    function = new SendDrawGold(token_client);
                    break;
                case (2):
                    function = new SendDrawResource(token_client);
                    break;
                case (3):
                    showCardsInCenter();
                    System.out.println("CHOSE CARD FROM CENTER (1/2/3 ) : ");
                    String choicestr = scan.nextLine();
                    int index = Integer.parseInt(choicestr);
                    function = new SendDrawCenter(token_client, index - 1);
                    break;
            }

        }while ( num < 0 || num > 3);
        scene.getClient().drawCard(function);
    }

    /**
     * Shows the cards available in the center deck.
     *
     * This method might have different implementations depending on the client type (socket or RMI).
     *
     * @throws IOException If an I/O error occurs during communication.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the connection thread is interrupted.
     */
    private void showCardsInCenter() throws IOException, ClassNotFoundException, InterruptedException {
        if(scene.getClient() instanceof clientSocket){
            scene.getClient().showCardsInCenter();
        }
        else{
            scene.getClient().getGameServer().showCardsInCenter(token);}
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
