package it.polimi.ingsw.RMI_FINAL.MESSAGES;

/**
 * Response message for sending a player's point information to the client.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing an integer representing the current point total for a player.
 *
 */
public class PointResponse extends ResponseMessage{

    public int player_point;

    public PointResponse(int point) {
        this.player_point = point;
    }

}
