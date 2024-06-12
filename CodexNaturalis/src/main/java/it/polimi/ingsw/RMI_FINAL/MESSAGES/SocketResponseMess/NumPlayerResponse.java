package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for the number of players.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing the number of players in the current game.
 *
 */
public class NumPlayerResponse extends ResponseMessage {

    int num_player;

    public NumPlayerResponse(int num_player) {
        this.num_player = num_player;
    }

    /**
     * Updates the client's model with the number of players.
     *
     * This method delegates setting the number of players to the client's miniModel object,
     */
    @Override
    public void action() {
        super.client.miniModel.setNum_players(num_player);
    }
}
