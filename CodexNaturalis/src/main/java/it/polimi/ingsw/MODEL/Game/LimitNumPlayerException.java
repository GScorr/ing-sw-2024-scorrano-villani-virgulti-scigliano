package it.polimi.ingsw.MODEL.Game;



public class LimitNumPlayerException extends RuntimeException {
    public LimitNumPlayerException(String message) {
        super(message);
    }
}