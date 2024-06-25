package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.VIEW.GraficInterterface;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;

import it.polimi.ingsw.VIEW.GuiPackage.GUI;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import it.polimi.ingsw.VIEW.TUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.NotBoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents the client-side socket connection to the serverSocket.
 * It handles communication between the client and serverSocket, updates the local
 * game state, and interacts with the user interface (TUI or GUI).
 *
 */
public class clientSocket implements VirtualViewF, Serializable {

    public MiniModel miniModel = new MiniModel();

    /**
     * ServerSocket Proxy is used to sending message from client to serverSocket through the output socket
     */
    private ServerProxy server_proxy;

    ObjectInputStream input;
    ObjectOutputStream output;
    Socket socket;

    public boolean flag_check;
    public boolean check;
    public boolean starting_card_is_placed;
    public boolean place_flag_check;
    public int checkName;
    public List<SocketRmiControllerObject> free_games;
    public boolean checkSizeGoldDeck;
    public boolean checkSizeResourcesDeck;
    public int point;

    public boolean GoalCardisPresent;
    public List<Goal> goalsCard;

    public PlayCard startingCard;

    public Goal goal_choosed;

    GraficInterterface terminal_interface;
    public String token;
    private boolean flag_Server_Disconneted = false;

    private boolean isPlacingCard = false;

    public clientSocket(ObjectInputStream input, ObjectOutputStream output, Socket socket) throws IOException, ClassNotFoundException {
        this.server_proxy = new ServerProxy(output);
        this.input = input;
        this.output = output;
        this.socket = socket;
    }

    /**
     * Starts the TUI and runs the client in a separate thread
     * to receive messages from the serverSocket.
     *
     * @throws IOException If there is an IO error during communication.
     * @throws ClassNotFoundException If a class cannot be found.
     * @throws InterruptedException If the thread is interrupted.
     * @throws NotBoundException If the RMI registry cannot be found.
     */
    public void runTUI() throws IOException, ClassNotFoundException, InterruptedException, NotBoundException {
        new Thread(() -> {
            try {
                startCheckingMessagesSocket();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        terminal_interface = new TUI(this);
        terminal_interface.runCli();

    }

    /**
     * Starts the GUI and runs the client in a separate thread
     * to receive messages from the serverSocket.
     *
     * @param scene The scene controller for the GUI.
     * @throws IOException If there is an IO error during communication.
     * @throws ClassNotFoundException If a class cannot be found.
     * @throws InterruptedException If the thread is interrupted.
     * @throws NotBoundException If the RMI registry cannot be found.
     */
    public void runGUI(SceneController scene) throws IOException, ClassNotFoundException, InterruptedException, NotBoundException {

        terminal_interface = new GUI(scene);
        terminal_interface.runCli();
        new Thread(() -> {
            try {
                startCheckingMessagesSocket();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Continuously receives messages from the serverSocket in a separate thread and updates the
     * local game state accordingly.
     *
     * @throws IOException If there is an IO error during communication.
     * @throws ClassNotFoundException If a class cannot be found.
     */
    public void startCheckingMessagesSocket() throws IOException, ClassNotFoundException {
        new Thread(() -> {
            ResponseMessage s;
            Object p;
            while (true) {
                try {
                    Thread.sleep(500);
                    p =  input.readObject();
                    if ( p instanceof ResponseMessage )
                    {
                        s = (ResponseMessage) p;
                        s.setVirtual_view(this);
                        s.setClient(this);
                        s.action();
                    }else {
                        System.err.println("Unexpected object received: " + p.getClass().getName());
                    }
                } catch (IOException | InterruptedException e) {
                    if (!flag_Server_Disconneted) {
                        System.err.println("                 [SERVER ERROR]\n" +
                                "                 TRY NEW LOG IN   " + e);
                        flag_Server_Disconneted = true;
                    }
                } catch (ClassNotFoundException | ClassCastException e) {
                    System.out.println("Error startCheckingMessagesSocket ");
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    public GraficInterterface getTerminal_interface() throws IOException{
        return terminal_interface;
    }

    /**
     * Sends a request to the serverSocket to check if the provided player name is valid.
     *
     * @param playerName The name chosen by the player.
     * @return An integer representing the result of the name check
     * @throws IOException If there is an IO error during communication.
     * @throws NotBoundException If the RMI registry cannot be found.
     * @throws ClassNotFoundException If a class cannot be found.
     * @throws InterruptedException If the thread is interrupted while waiting for a response.
     */
    @Override
    public int checkName(String playerName) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        server_proxy.checkName(playerName);
        waitResponse();
        return checkName;
    }

    /**
     * Checks if there are any free games available on the serverSocket.
     *
     * @return True if there are free games, false otherwise.
     * @throws IOException If there is an IO error during communication.
     * @throws NotBoundException If the RMI registry cannot be found.
     * @throws ClassNotFoundException If a class cannot be found.
     * @throws InterruptedException If the thread is interrupted while waiting for a response.
     */
    @Override
    public boolean areThereFreeGames() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        this.getFreeGames();
        return free_games != null && !free_games.isEmpty();
    }

    @Override
    public List<SocketRmiControllerObject> getFreeGames() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.getFreeGame();
        waitResponse();
        return this.free_games;
    }

    /**
     * Attempts to create a new game on the serverSocket with the specified name, number of players,
     * and the player's name. This method waits until the player joins the game successfully.
     *
     *
     * @param gameName The desired name for the game.
     * @param numplayers The desired number of players in the game.
     * @param playerName The player's chosen name.
     * @throws IOException If there is an IO error during communication.
     * @throws NotBoundException If the RMI registry cannot be found.
     * @throws ClassNotFoundException If a class cannot be found.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    @Override
    public void createGame(String gameName, int numplayers, String playerName) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        server_proxy.createGame(gameName,numplayers,playerName);
        while (this.miniModel.getState().equals("NOT_IN_A_GAME")){
            buffering();
        }
    }

    /**
     * Attempts to find an RMI controller for a game with the given ID using the player's name.
     *
     * @param id The ID of the game to join.
     * @param playerName The player's chosen name.
     * @return True if the controller is found, false otherwise.
     * @throws IOException If there is an IO error during communication.
     * @throws ClassNotFoundException If a class cannot be found.
     * @throws InterruptedException If the thread is interrupted while waiting for a response.
     */
    @Override
    public boolean findRmiController(int id, String playerName) throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.findRmiController(id,playerName);
        waitResponse();
        return this.check;
    }

    /**
     * Attempts to connect to the game serverSocket. This method waits until the player joins a game successfully.
     *
     * @throws IOException If there is an IO error during communication.
     * @throws NotBoundException If the RMI registry cannot be found.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    @Override
    public void connectGameServer() throws IOException, NotBoundException, InterruptedException {
        server_proxy.connectGame();
        while(this.miniModel.getState().equals( "NOT_IN_A_GAME")){
            buffering();
        }
    }

    /**
     * Sends a request to the serverSocket to select and insert a card at a specified location on the game board.
     *
     * @param choice The index of the card to select from the player's hand.
     * @param x The x-coordinate of the target location on the board.
     * @param y The y-coordinate of the target location on the board.
     * @param flipped Whether the card should be placed face-up (true) or face-down (false).
     * @throws IOException If there is an IO error during communication.
     * @throws InterruptedException If the thread is interrupted while waiting for a response.
     * @throws ClassNotFoundException If a class cannot be found.
     */
    @Override
    public void selectAndInsertCard(int choice, int x, int y, boolean flipped) throws IOException, InterruptedException, ClassNotFoundException {
        server_proxy.placeCard(choice - 1,x,y,flipped);
        isPlacingCard = true;
        waitResponse();

       /* buffering();
        buffering();

        */
    }

    /**
     * Sends a request to the serverSocket to draw a card. This method waits until the player's
     * `miniModel` state changes from "DRAW_CARD".
     *
     * @param function The function to execute after drawing the card.
     * @throws IOException If there is an IO error during communication.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    @Override
    public void drawCard(SendFunction function) throws IOException, InterruptedException {
        server_proxy.drawCard(function);
        isPlacingCard = true;
        waitResponse();
    }

    /**
     * Sends a chat message to the serverSocket based on the current number of players and the provided decision.
     *
     * @param message The message content to be sent.
     * @param decision An integer representing the target recipient of the message.
     * @throws IOException If there is an IO error during communication.
     */
    @Override
    public void ChatChoice(String message, int decision) throws IOException {
        if(miniModel.getNum_players() != 2){
            if(decision==miniModel.getNum_players()+1){
                server_proxy.chattingGlobal(new ChatMessage(message, miniModel.getMy_player()));
            }
            else{
                server_proxy.chattingMoment(miniModel.getMy_index(), decision, new ChatMessage(message,miniModel.getMy_player()));
            }
        } else{
            server_proxy.chattingGlobal(new ChatMessage(message, miniModel.getMy_player()));
        }
    }

    @Override
    public VirtualGameServer getGameServer() throws IOException {
        return null;
    }

    /**
     * Checks if a goal card has been placed on the game board.
     *
     * @return True if no goal card is present, false otherwise.
     * @throws IOException If there is an IO error during communication.
     * @throws ClassNotFoundException If a class cannot be found.
     * @throws InterruptedException If the thread is interrupted while waiting for a response.
     */
    @Override
    public boolean isGoalCardPlaced() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.getGoalCard();
        waitResponse();
        return ! this.GoalCardisPresent;
    }

    @Override
    public String getGoalPlaced() {
        return goal_choosed.toString();
    }

    /**
     * Requests the starting card from the serverSocket and returns it.
     *
     * @return The starting PlayCard object.
     * @throws IOException If there is an IO error during communication.
     * @throws ClassNotFoundException If a class cannot be found.
     * @throws InterruptedException If the thread is interrupted while waiting for a response.
     */
    @Override
    public PlayCard showStartingCardGUI() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.getStartingCard();
        waitResponse();
        return startingCard;
    }

    @Override
    public String getFirstGoal() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.getListGoalCard();
        waitResponse();
        return goalsCard.get(0).toString();
    }

    @Override
    public String getSecondGoal() {
        return goalsCard.get(1).toString();
    }

    /**
     * Sets the chosen goal card and sends the selection to the serverSocket.
     *
     * @param i The index of the chosen goal card in the local list.
     * @throws IOException If there is an IO error during communication.
     */
    @Override
    public void chooseGoal(int i) throws IOException {
        this.goal_choosed = goalsCard.get(i);
        server_proxy.chooseGoal(i);
    }

    /**
     * Disconnects from the serverSocket and exits the application.
     *
     * @throws IOException If there is an IO error during communication.
     * @throws ClassNotFoundException If a class cannot be found.
     * @throws InterruptedException If the thread is interrupted while waiting for a response.
     * @throws NotBoundException If the RMI registry cannot be found.
     */
    public void disconect() throws IOException, ClassNotFoundException, InterruptedException, NotBoundException {
        try {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    @Override
    public void setLastTurn(boolean b) throws IOException {
        getMiniModel().setFinal_state(b);
    }

    @Override
    public Goal getFirstGoalCard() throws IOException, InterruptedException {
        server_proxy.getListGoalCard();
        waitResponse();
        return goalsCard.get(0);
    }

    @Override
    public Goal getSecondGoalCard() throws IOException {
        return goalsCard.get(1);
    }

    @Override
    public MiniModel getMiniModel() throws IOException {
        return this.miniModel;
    }

    /**
     * Requests the starting card from the serverSocket and displays it using an internal helper method.
     *
     * @throws IOException If there is an IO error during communication.
     * @throws InterruptedException If the thread is interrupted while waiting for a response.
     */
    @Override
    public void showStartingCard() throws IOException, InterruptedException {
        server_proxy.getStartingCard();
        waitResponse();
        showStartingCardHelper(this.startingCard);
    }

    /**
     * Sends the player's decision about the starting card to the serverSocket.
     *
     * @param b True if the player wants to keep the starting card, false otherwise.
     * @throws IOException If there is an IO error during communication.
     */
    @Override
    public void chooseStartingCard(boolean b) throws IOException {
        server_proxy.chooseStartingCard(b);
    }

    /**
     * Checks if the player has placed their starting card on the game board.
     *
     * @return True if the player's starting card has been placed, false otherwise.
     * @throws IOException If there is an IO error during communication.
     * @throws InterruptedException If the thread is interrupted while waiting for a response.
     */
    @Override
    public boolean isFirstPlaced() throws IOException, InterruptedException {
        server_proxy.startingCardIsPlaced();
        waitResponse();
        return this.starting_card_is_placed;
    }

    @Override
    public String getToken() throws InterruptedException, IOException {
        server_proxy.getToken();
        waitResponse();
        return this.token;
    }

    /**
     * Checks if the gold deck is still present on the serverSocket.
     *
     * @return True if the gold deck is present (not empty), false otherwise.
     * @throws IOException If there is an IO error during communication.
     * @throws ClassNotFoundException If a class cannot be found.
     * @throws InterruptedException If the thread is interrupted while waiting for a response.
     */
    @Override
    public boolean isGoldDeckPresent() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.isGoldDeckPresent();
        waitResponse();
        return this.checkSizeGoldDeck;
    }

    /**
     * Checks if the resource deck is still present on the serverSocket.
     *
     * @return True if the resource deck is present (not empty), false otherwise.
     * @throws IOException If there is an IO error during communication.
     * @throws ClassNotFoundException If a class cannot be found.
     * @throws InterruptedException If the thread is interrupted while waiting for a response.
     */
    @Override
    public boolean isResourceDeckPresent() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.isResourceDeckPresent();
        waitResponse();
        return this.checkSizeResourcesDeck;
    }

    /**
     * Requests information about the cards currently displayed in the center of the game board from the serverSocket.
     *
     * @throws IOException If there is an IO error during communication.
     * @throws ClassNotFoundException If a class cannot be found.
     * @throws InterruptedException If the thread is interrupted while waiting for a response.
     */
    @Override
    public void showCardsInCenter() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.getCardsInCenter();
        waitResponse();
    }

    /**
     * Displays a formatted representation of the given PlayCard object, likely for a TUI.
     *
     * @param card The PlayCard object to be displayed.
     * @throws IOException If there is an IO error during printing.
     */
    public void showStartingCardHelper(PlayCard card) throws IOException {
        Side back = card.getBackSide();
        Side front = card.getFrontSide();

        System.out.println("BACK SIDE\n----------------------------");
        System.out.println( " | " + back.getAngleLeftUp().toString().substring(0,2)  +   " |               "+ " | " + back.getAngleRightUp().toString().substring(0,2) + " |\n " );
        System.out.println( " |       | " + back.getCentral_resource().toString().substring(0,2) + back.getCentral_resource2().toString().substring(0,2) + back.getCentral_resource3().toString().substring(0,2) + " |         |\n " );
        System.out.println( " | " + back.getAngleLeftDown().toString().substring(0,2) +  " |               " + " | " + back.getAngleRightDown().toString().substring(0,2) + " |\n " );
        System.out.println("----------------------------\n\n");

        System.out.println("FRONT SIDE\n----------------------------");

        if(card instanceof ResourceCard) {
            System.out.println(" | " + card.getPoint() + " | ");
            if (card instanceof GoldCard) {
                System.out.println(" | " + ((GoldCard) card).getPointBonus().toString().substring(0, 2) + " | " + "             | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
            } else {
                System.out.println(" | " + front.getAngleLeftUp().toString().substring(0, 2) + " | " + "              | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
            }
        }
        else {
            System.out.println(" | " + front.getAngleLeftUp().toString().substring(0, 2) + " | " + "              | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
        }
        System.out.println( " |       | " + front.getCentral_resource().toString().substring(0,2) + front.getCentral_resource2().toString().substring(0,2) + front.getCentral_resource3().toString().substring(0,2) + " |        |\n " );
        if ( card instanceof GoldCard ){
            System.out.println( " | " + front.getAngleLeftDown().toString().substring(0,2) + " | " +
                    "  " + card.getCostraint().toString()  + " | " + front.getAngleRightDown().toString().substring(0,2) + " |\n ");
        }else{
            System.out.println( " | " + front.getAngleLeftDown().toString().substring(0,2) + " |              " + " | " + front.getAngleRightDown().toString().substring(0,2) + " |\n " );
        }
        System.out.println("----------------------------\n\n");
    }

    /**
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void buffering() throws IOException, InterruptedException{
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("/");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("|");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("\\");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("-");
    }

    /**
     * Waits for a signal indicating a serverSocket response has been received.
     *
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public void waitResponse() throws InterruptedException {
        flag_check = true;
        while(flag_check){
            Thread.sleep(400);
        }

        if (isPlacingCard) Thread.sleep(1500); isPlacingCard = false;
    }

    /**
     * Client Handler is responsible only for the methods called by the GameServer
     */

    @Override
    public void showUpdate(GameField game_field) throws IOException {

    }

    @Override
    public void showCard(PlayCard card) throws IOException {
    }

    @Override
    public void pushBack(ResponseMessage message) throws IOException {

    }

    @Override
    public void showField(GameField field) throws IOException {

    }

    @Override
    public void printString(String s) throws IOException {

    }

    @Override
    public void setGameField(List<GameField> games) throws IOException {

    }

    @Override
    public void setCards(List<PlayCard> cards) throws IOException {

    }

    @Override
    public void setNumToPlayer(HashMap<Integer, String> map) throws IOException {

    }

    @Override
    public void setState(String state) throws IOException {

    }

    @Override
    public void setCenterCards(CenterCards cards, PlayCard res, PlayCard gold) throws IOException {

    }

    @Override
    public void addChat(int idx, ChatMessage message) throws IOException {

    }

    @Override
    public void insertId(int id) throws IOException {

    }

    @Override
    public void insertNumPlayers(int numPlayersMatch) throws IOException {

    }

    @Override
    public void insertPlayer(Player player) throws IOException {

    }

    @Override
    public void manageGame(boolean endgame) throws IOException {

    }

    @Override
    public void setGameFieldMiniModel() throws IOException {

    }

    @Override
    public void reportError(String details) throws IOException {

    }

    @Override
    public void reportMessage(String details) throws IOException {
        System.out.println(details);
    }

}
