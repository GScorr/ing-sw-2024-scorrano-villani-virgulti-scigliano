package it.polimi.ingsw.MODEL.Player;

/**
 * Interface for objects that observe player state changes.
 */
public interface PlayerObserver {

    /**
     * Called when the player's state changes.
     */
    void update();
}
