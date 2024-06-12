package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for inserting a player.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing information about the player object that the client represents in the game.
 *
 */
public class insertPlayerResponse extends ResponseMessage {

    Player player;

    public insertPlayerResponse(Player player) {
       this.player = player;
    }

    /**
     * Updates the client's model with the player information.
     *
     * This method delegates setting the player information to the client's miniModel object,
     */
    @Override
    public void action() {
        super.client.miniModel.setMy_player(player);
    }
}
