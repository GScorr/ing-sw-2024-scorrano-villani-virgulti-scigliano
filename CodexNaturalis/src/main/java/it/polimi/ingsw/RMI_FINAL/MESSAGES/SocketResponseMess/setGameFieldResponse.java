package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;


import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Response message for setting the game field.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a list of `GameField` objects representing the current state of the game board.
 *
 */
public class setGameFieldResponse extends ResponseMessage {

    public List<GameField> games = new ArrayList<>();

    public setGameFieldResponse(List<GameField> games) {
        this.games.addAll(games);
    }

    /**
     * Updates the client's model with the game field information.
     *
     * This method sets the `gameField` list in the client's miniModel object with the received information
     * about the game board state.
     */
    @Override
    public void action()  {
        super.client.miniModel.setGameField(this.games);
    };

}
