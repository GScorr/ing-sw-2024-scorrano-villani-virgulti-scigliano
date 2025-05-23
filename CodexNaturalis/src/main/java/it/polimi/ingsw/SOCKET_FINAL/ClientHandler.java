package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.*;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess.*;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.Message.*;
import it.polimi.ingsw.VIEW.GraficInterterface;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;


import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;

import java.util.HashMap;
import java.util.List;

/**
 * A class that handles communication between a client and the serverSocket in the SOCKET_FINAL package.
 *
 */
public class ClientHandler  implements VirtualViewF {

    private MiniModel miniModel =  new MiniModel();
    final ServerSocket serverSocket;
    final ObjectInputStream input;
    final ObjectOutputStream output;

    public Common_Server common;
    public String token;
    public String name;
    private Socket client_socket;
    public VirtualGameServer rmi_controller;
    public boolean client_is_connected = true;

    public ClientHandler(ServerSocket serverSocket, ObjectInputStream input, ObjectOutputStream output, Common_Server common , Socket clientsocket) throws IOException, NotBoundException {
        this.serverSocket = serverSocket;
        this.input = input;
        this.output = output;
        this.common = common;
        this.client_socket = clientsocket;
    }

    /**
     * Starts a thread to periodically send heartbeats to the serverSocket.
     */
    public void startSendingHeartbeats() {
        new Thread(() -> {
            while (client_is_connected) {
                try {
                    common.receiveHeartbeat(token);
                    Thread.sleep(150);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Shows a playing card on the center of the game screen.
     *
     * @param card the card to be displayed
     * @throws IOException if there is an error writing to the output stream
     */
    @Override
    public void showCard(PlayCard card) throws IOException {
        ResponseMessage s = new showCenterCardsResponse(card);
        sendMessage(s);
    }

    /**
     * Pushes a response message back to the client or serverSocket.
     *
     * @param message The message to be pushed back.
     * @throws IOException if there is an error writing to the output stream.
     */
    @Override
    public void pushBack(ResponseMessage message) throws IOException {
        miniModel.pushBack(message);
    }

    /**
     * Prints a string message to the client or serverSocket.
     *
     * @param string The string to be printed.
     * @throws IOException if there is an error writing to the output stream.
     */
    @Override
    public void printString(String string) throws IOException {
        ResponseMessage s = new StringResponse(string);
        sendMessage(s);
    }

    /**
     *
     * setter
     *
     */

    @Override
    public void setGameField(List<GameField> games) throws IOException {
        ResponseMessage s = new setGameFieldResponse(games);
        sendMessage(s);
    }

    @Override
    public void setCards(List<PlayCard> cards)throws IOException {
        ResponseMessage s = new setCardsResponse(cards);
        sendMessage(s);
    }

    @Override
    public void setNumToPlayer(HashMap<Integer, String> map) throws IOException {
        ResponseMessage s = new NumToPlayerResponse(map);
        sendMessage(s);
    }

    @Override
    public void setState(String state) throws IOException {
        ResponseMessage s = new setStateMessage(state);
        sendMessage(s);
    }

    @Override
    public void setCenterCards(CenterCards cards, PlayCard res, PlayCard gold) throws IOException {
        ResponseMessage s = new setCenterCardsResponde(cards, res,gold);
        sendMessage(s);
    }

    /**
     * Adds a chat message to a specific chat window.
     *
     * @param idx The index of the chat window to add the message to.
     * @param message The chat message to be added.
     * @throws IOException if there is an error writing to the output stream.
     */
    @Override
    public void addChat(int idx, ChatMessage message) throws IOException {
        ResponseMessage s = new addChatResponse(idx,message);
        sendMessage(s);
    }

    /**
     * Sends an insert ID response message containing a specific integer ID.
     *
     * @param id The integer ID to be inserted.
     * @throws IOException if there is an error writing to the output stream.
     */
    @Override
    public void insertId(int id) throws IOException {
        ResponseMessage s = new insertIdResponse(id);
        sendMessage(s);
    }

    /**
     * Sends an insert player response message containing information about a player.
     *
     * @param player The player object to be inserted.
     * @throws IOException if there is an error writing to the output stream.
     */
    @Override
    public void insertPlayer(Player player) throws IOException {
        ResponseMessage s = new insertPlayerResponse(player);
        sendMessage(s);
    }

    /**
     * Sends a response message containing the number of players in a match.
     *
     * @param numPlayersMatch The total number of players in the match.
     * @throws IOException if there is an error writing to the output stream.
     */
    @Override
    public void insertNumPlayers(int numPlayersMatch) throws IOException {
        ResponseMessage s = new NumPlayerResponse(numPlayersMatch);
        sendMessage(s);
    }

    /**
     * Starts a background thread to continuously check for incoming messages from the miniModel.
     * If a message is available, it sends the message to the output stream and resets it.
     *
     * This method loops indefinitely and catches potential exceptions like interruptions
     * and I/O errors.
     */
    public void startCheckingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    ResponseMessage s = miniModel.popOut();
                    if(s!=null){
                        sendMessage(s);
                    }
                } catch (IOException e) {

                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Runs the main loop of the virtual view, handling incoming messages from the client.
     *
     * This method continuously reads messages from the input stream and performs actions based on the message type.
     * Some of the supported messages include:
     *  - Check name: validates username and creates a token if successful.
     *  - Create game: starts a new game and connects to the RMI controller.
     *  - Find RMI controller: checks for the existence of an RMI controller.
     *  - Connect game: connects to the existing game using the token.
     *  - Get goal card: checks if a goal card is present.
     *  - Get list goal card: retrieves a list of available goal cards.
     *  - Get starting card: retrieves the starting card for the game.
     *  - First card placed: checks if the first card has been placed.
     *
     *
     * @throws IOException If there's an error reading from the input stream.
     * @throws ClassNotFoundException If the received message cannot be deserialized.
     */
    public void runVirtualView() throws IOException, ClassNotFoundException, NotBoundException, InterruptedException {
        synchronized (this) {
            try {
                Message DP_message = null;

                while ((DP_message = (Message) input.readObject()) != null) {
                    if (token != null) {
                        DP_message.setToken(token);
                    }
                    if (rmi_controller != null) {
                        DP_message.setRmiController(this.rmi_controller);
                    }
                    DP_message.setServer(serverSocket);
                    DP_message.setOutput(output);
                    DP_message.setCommonServer(this.common);
                    DP_message.setClientHandler(this);
                    DP_message.action();

                }
            } catch (EOFException e) {
                disconect();
                client_is_connected = false;
            } catch ( IOException e) {
                disconect();
                // Gestione generica delle eccezioni durante la deserializzazione
                client_is_connected = false;
                //e.printStackTrace();
            } catch (NotBoundException e) {
                client_is_connected = false;
                //disconect();
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                //disconect();
                throw new RuntimeException(e);
            }catch (ClassNotFoundException e){
                //disconect();
                client_is_connected = false;
            }
        }
    }

    @Override
    public void disconect() throws IOException, ClassNotFoundException, InterruptedException, NotBoundException {
        try {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public MiniModel getMiniModel() throws IOException {
        return null;
    }

    @Override
    public GraficInterterface getTerminal_interface() throws IOException{
        return null;
    }

    /**
     * Client Handler is responsible only for the methods called by the GameServer
     */

    @Override
    public boolean findRmiController(int id, String player_name) throws IOException, ClassNotFoundException, InterruptedException {
        return false;
    }

    @Override
    public boolean isGoalCardPlaced() throws IOException, ClassNotFoundException, InterruptedException {
        return false;
    }

    @Override
    public String getGoalPlaced() throws IOException {
        return null;
    }

    @Override
    public PlayCard showStartingCardGUI() throws IOException {
        return null;
    }

    @Override
    public String getFirstGoal() throws IOException, ClassNotFoundException, InterruptedException {
        return null;
    }

    @Override
    public String getSecondGoal() throws IOException {
        return null;
    }

    @Override
    public void chooseGoal(int i) throws IOException, InterruptedException {

    }

    @Override
    public Goal getFirstGoalCard() throws IOException, InterruptedException {
        return null;
    }

    @Override
    public Goal getSecondGoalCard() throws IOException {
        return null;
    }

    @Override
    public boolean isFirstPlaced() throws IOException, InterruptedException {
        return false;
    }

    @Override
    public String getToken() throws InterruptedException {
        return null;
    }

    @Override
    public int checkName(String playerName) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        return 0;
    }

    @Override
    public boolean areThereFreeGames() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        return false;
    }

    @Override
    public List<SocketRmiControllerObject> getFreeGames() throws IOException, ClassNotFoundException, InterruptedException {
        return null;
    }

    @Override
    public VirtualGameServer getGameServer() throws IOException {
        return null;
    }

    @Override
    public boolean isGoldDeckPresent() throws IOException, ClassNotFoundException, InterruptedException {
        return false;
    }

    @Override
    public boolean isResourceDeckPresent() throws IOException, ClassNotFoundException, InterruptedException {
        return false;
    }

    @Override
    public void setGameFieldMiniModel() throws IOException {

    }

    public void showValue(String message) {

    }

    @Override
    public void showUpdate(GameField game_field) throws IOException {

    }

    @Override
    public void reportError(String details) throws IOException {

    }

    @Override
    public void reportMessage(String details) throws IOException {

    }

    @Override
    public void showField(GameField field) throws IOException {

    }

    @Override
    public void setLastTurn(boolean b) throws IOException {

    }

    @Override
    public void connectGameServer() throws IOException, NotBoundException, InterruptedException {

    }



    @Override
    public void showStartingCard() throws IOException, InterruptedException {

    }

    @Override
    public void chooseStartingCard(boolean b) throws IOException {

    }

    @Override
    public void createGame(String gameName, int numplayers, String playerName) throws IOException, NotBoundException, ClassNotFoundException {

    }

    @Override
    public void manageGame(boolean endgame) throws IOException {

    }

    @Override
    public void selectAndInsertCard(int choice, int x, int y, boolean flipped) throws IOException, InterruptedException, ClassNotFoundException {

    }

    @Override
    public void drawCard(SendFunction function) throws IOException, InterruptedException {

    }

    @Override
    public void ChatChoice(String message, int decision) throws IOException {

    }

    @Override
    public void showCardsInCenter() throws IOException, ClassNotFoundException, InterruptedException {

    }

    @Override
    public void runGUI(SceneController scene) throws IOException, ClassNotFoundException, InterruptedException, NotBoundException {

    }

    public void sendMessage(ResponseMessage message) throws IOException {
        synchronized (output) {
            output.writeObject(message);
            output.flush();
            output.reset();
        }
    }

}
