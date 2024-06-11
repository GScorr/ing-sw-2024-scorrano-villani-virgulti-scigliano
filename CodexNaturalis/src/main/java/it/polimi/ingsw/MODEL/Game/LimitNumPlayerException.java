package it.polimi.ingsw.MODEL.Game;

/**
 * Exception thrown when the number of players exceeds the maximum allowed limit.
 *
 * This exception is thrown by methods that manage the number of players in a game
 * if an attempt is made to add more players than the supported limit
 */
public class LimitNumPlayerException extends RuntimeException {

    public LimitNumPlayerException(String message) {
        super(message);
    }

}