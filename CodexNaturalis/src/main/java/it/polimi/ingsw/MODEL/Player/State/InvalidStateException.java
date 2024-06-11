package it.polimi.ingsw.MODEL.Player.State;

/**
 *  An exception class thrown when an invalid state operation is attempted.
 *
 */
public class InvalidStateException extends RuntimeException {

    /**
     *  Constructor to create an `InvalidStateException` with a specific message.
     *
     *  @param message the message describing the reason for the exception
     */
    public InvalidStateException(String message) {
        super(message);
    }

}