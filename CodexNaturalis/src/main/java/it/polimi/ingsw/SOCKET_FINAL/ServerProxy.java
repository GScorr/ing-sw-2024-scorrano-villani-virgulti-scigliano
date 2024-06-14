package it.polimi.ingsw.SOCKET_FINAL;




import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.SOCKET_FINAL.Message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A class for interacting with the server through messages.
 *
 */
public class ServerProxy implements  Serializable {

    public ObjectOutputStream output;
    public ObjectInputStream input;

    public ServerProxy(ObjectOutputStream output) throws IOException {
        this.output = output;
    }

    // this function send the Message Object to the server

    /**
     * Sends a message object to the server.
     *
     * @param DP_Message the message to send
     * @throws IOException if an I/O error occurs
     */
    private void sendMessage(Message DP_Message) throws IOException {
        output.writeObject(DP_Message);
        output.flush();
        output.reset();
    }

    /**
     * Sends a message to the server to check if the provided name is available for a new player.
     *
     * @param name the name to check for availability
     * @throws IOException if an I/O error occurs while sending the message
     * @throws ClassNotFoundException if a received message object cannot be found (unexpected)
     */
    public void checkName(String name) throws IOException, ClassNotFoundException {
        Message DP_message = new CheckNameMessage(name);
        sendMessage(DP_message);
    }


    public void getFreeGame() throws IOException, ClassNotFoundException {
        Message DP_message = new GetFreeGames();
        sendMessage(DP_message);
    }

    /**
     * Sends a message to the server to create a new game with the specified details.
     *
     * @param game_name the name of the game to create
     * @param num_players the desired number of players in the game
     * @param nome the player's name who wants to create the game
     * @throws IOException if an I/O error occurs while sending the message
     * @throws ClassNotFoundException if a received message object cannot be found (unexpected)
     * @throws SocketException if a socket error occurs (e.g., connection reset)
     */
    public  void createGame(String game_name, int num_players, String nome  ) throws IOException, ClassNotFoundException, SocketException {
        Message DP_message = new CreateGame(game_name,num_players,nome);
        sendMessage(DP_message);
    }

    /**
     * Sends a message to the server to retrieve the RMI controller for the game with the given ID.
     *
     * @param id the ID of the game to join
     * @param player_name the player's name who wants to join the game
     * @throws IOException if an I/O error occurs while sending the message
     * @throws ClassNotFoundException if a received message object cannot be found (unexpected)
     */
    public void findRmiController(Integer id, String player_name) throws IOException, ClassNotFoundException {
        Message DP_message = new FindRMIControllerMessage(id, player_name);
        sendMessage(DP_message);
    }

    public void getGoalCard() throws IOException, ClassNotFoundException {
        Message DP_message = new getGoalCard();
        sendMessage(DP_message);
    }

    public void getListGoalCard() throws IOException {
        Message DP_message = new getListGoalCard();
        sendMessage(DP_message);
    }

    /**
     * Sends a message to the server indicating the chosen goal card by its index.
     *
     * @param index the zero-based index of the chosen goal card
     * @throws IOException if an I/O error occurs while sending the message
     */
    public void chooseGoal(int index) throws IOException {
        Message DP_message = new chooseGoalMessage( index);
        sendMessage(DP_message);
    }

    public void getStartingCard() throws IOException {
        Message DP_message = new getStartingCard();
        sendMessage(DP_message);
    }

    /**
     * Sends a message to the server indicating that the starting card has been placed.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    void startingCardIsPlaced() throws IOException {
        Message DP_message = new firstCardIsPlaced();
        sendMessage(DP_message);
    }

    /**
     * Sends a message to the server indicating whether the client wants to choose
     * a starting card or keep the random selection.
     *
     * @param check a flag indicating the player's choice
     * @throws IOException if an I/O error occurs while sending the message
     */
    public void chooseStartingCard(boolean check) throws IOException {
        Message DP_message = new chooseStartingCardMessage(check);
        sendMessage(DP_message);
    }

    /**
     * Sends a message to the server indicating the placement of a card.
     *
     * @param index the index of the card to place
     * @param x the x-coordinate of the desired placement location on the board
     * @param y the y-coordinate of the desired placement location on the board
     * @param flipped a flag indicating whether to place the card face-up (true) or face-down (false)
     * @throws IOException if an I/O error occurs while sending the message
     * @throws ClassNotFoundException if a received message object cannot be found (unexpected)
     */
    public void placeCard(int index, int x, int y, boolean flipped) throws IOException, ClassNotFoundException {
        Message DP_message = new placeCard(index,x,y,flipped);
        sendMessage(DP_message);
    }

    /**
     * Sends a message to the server to inquire about the presence of the gold card deck.
     *
     * @throws IOException if an I/O error occurs while sending the message
     * @throws ClassNotFoundException if a received message object cannot be found (unexpected)
     */
    public void isGoldDeckPresent() throws IOException, ClassNotFoundException {
        Message DP_message = new getGoldDeckSize();
        sendMessage(DP_message);
    }

    /**
     * Sends a message to the server to inquire about the presence of the resource card deck.
     *
     * @throws IOException if an I/O error occurs while sending the message
     * @throws ClassNotFoundException if a received message object cannot be found
     */
    public void isResourceDeckPresent() throws IOException, ClassNotFoundException {
        Message DP_message = new getResourcesDeckSize();
        sendMessage(DP_message);
    }

    public void getCardsInCenter() throws IOException, ClassNotFoundException {
        Message DP_message = new getCardsInCenter();
        sendMessage(DP_message);

    }

    /**
     * Sends a message to the server requesting to draw a card.
     *
     * @param function a callback function to handle the server's response about the drawn card
     * @throws IOException if an I/O error occurs while sending the message
     */
    public void drawCard(SendFunction function) throws IOException {
        Message DP_message = new drawCard(function);
        sendMessage(DP_message);
    }

    /**
     * Sends a message to the server indicating the client's intent to connect to the game.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    public void connectGame() throws IOException {
        Message DP_message = new connectGame();
        sendMessage(DP_message);
    }

    public void getToken() throws IOException {
        Message DP_message = new getToken();
        sendMessage(DP_message);
    }

    /**
     * Sends a global chat message to the server. This message will be broadcast to all players in the game.
     *
     * @param chatMessage the chat message to send
     * @throws IOException if an I/O error occurs while sending the message
     */
    public void chattingGlobal(ChatMessage chatMessage) throws IOException {
        Message DP_message = new chatGlobal(chatMessage);
        sendMessage(DP_message);
    }

    /**
     * Sends a chat message to the server related to a specific moment in the game.
     *
     * @param myIndex the zero-based index of the worker card associated with the message (optional)
     * @param decision the game decision this message is related to (optional, meaning depends on the game)
     * @param chatMessage the chat message to send
     * @throws IOException if an I/O error occurs while sending the message
     */
    public void chattingMoment(int myIndex, int decision, ChatMessage chatMessage) throws IOException {
        Message DP_message = new chatMoment(myIndex,decision,chatMessage);
        sendMessage(DP_message);
    }

}
