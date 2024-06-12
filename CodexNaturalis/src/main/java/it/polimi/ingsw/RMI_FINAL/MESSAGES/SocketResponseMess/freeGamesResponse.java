package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;

import java.util.List;

/**
 * Response message for listing free games.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a list of currently available games (lobbies) that the client can join.
 *
 */
public class freeGamesResponse extends ResponseMessage {
    List<SocketRmiControllerObject> free_games;

    public freeGamesResponse(List<SocketRmiControllerObject> free_games) {
        this.free_games = free_games;
    }

    /**
     * Updates the client's state with the list of free games.
     *
     * This method sets the `free_games` list in the client object with the received information
     * about currently available games. It also resets the `flag_check` flag.
     */
    @Override
    public void action() {
        super.client.free_games = this.free_games;
        super.client.flag_check = false;
    }
}
