package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * This interface defines methods for the virtual view component of the game.
 * The virtual view interacts with the client to display information and receive user input.
 *
 */
public interface VirtualView extends Serializable {

    /**
     * Displays a text message to the client.
     *
     * @param message The text message to be displayed.
     * @throws IOException If there's an error writing to the output stream.
     */
    public void showValue(String message);

    /**
     * Updates the client's view of the game field.
     *
     * @param game_field The updated game field information.
     * @throws IOException If there's an error writing to the output stream.
     */
    public void showUpdate(GameField game_field) throws IOException;

    /**
     * Reports an error message to the client.
     *
     * @param details The details of the error.
     * @throws IOException If there's an error writing to the output stream.
     */
    public void reportError(String details) throws IOException;

    /**
     * Reports a general message to the client.
     *
     * @param details The details of the message.
     * @throws IOException If there's an error writing to the output stream.
     */
    public void reportMessage(String details) throws  IOException;

    /**
     * Displays a specific playing card to the client.
     *
     * @param card The playing card to be displayed.
     * @throws IOException If there's an error writing to the output stream.
     */
    public void showCard(PlayCard card) throws IOException;

    /**
     * Pushes a response message back to the client or serverSocket.
     *
     * @param message The response message to be sent.
     * @throws IOException If there's an error writing to the output stream.
     */
    public void pushBack(ResponseMessage message) throws IOException;

    /**
     * Updates the client's view of the entire game field, potentially including multiple games.
     *
     * @param field The updated game field(s) information.
     * @throws IOException If there's an error writing to the output stream.
     */
    public void showField(GameField field) throws IOException;

    /**
     * Prints a string message to the client.
     *
     * @param s The string message to be printed.
     * @throws IOException If there's an error writing to the output stream.
     */
    public void printString(String s) throws IOException;

    /**
     * Sets the game field information for the client view.
     *
     * @param games The list of game field information.
     * @throws IOException If there's an error writing to the output stream.
     */
    public void setGameField(List<GameField> games) throws IOException;

    /**
     * Retrieves a reference to the internal MiniModel object.
     *
     * @return The MiniModel object associated with this view.
     * @throws IOException If there's an error during communication.
     */
    public MiniModel getMiniModel() throws IOException;

    /**
     * Sets the list of playable cards for the client view.
     *
     * @param cards The list of playable cards.
     * @throws IOException If there's an error writing to the output stream.
     */
    public void setCards(List<PlayCard> cards) throws IOException;

    /**
     * Sets a mapping of player IDs to their usernames for the client view.
     *
     * @param map The mapping of player IDs to usernames.
     * @throws IOException If there's an error writing to the output stream.
     */
    public void setNumToPlayer(HashMap<Integer, String> map) throws IOException;

    /**
     * Sets the current game state for the client view.
     *
     * @param state The string representation of the current game state.
     * @throws IOException If there's an error writing to the output stream.
     */
    public void setState(String state) throws IOException;

}
