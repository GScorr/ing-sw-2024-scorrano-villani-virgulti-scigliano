package it.polimi.ingsw.MODEL.Player;

/**
 *  An exception class thrown when an index or value is out of bounds.
 *
 */
public class InvalidBoundException extends RuntimeException {

    /**
     *  Constructor to create an `InvalidBoundException` with a specific message.
     *
     *  @param message the message describing the reason for the exception
     */
    public InvalidBoundException(String message) {
        super(message);
    }

}