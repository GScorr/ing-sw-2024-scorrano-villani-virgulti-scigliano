package it.polimi.ingsw.MODEL.Game.State;

public class GameInvalidStateException extends RuntimeException {

    public GameInvalidStateException(String message) {
        super(message);
    }
}