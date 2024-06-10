package it.polimi.ingsw.MODEL.DeckPackage;

/**
 * Thrown when attempting to draw a card from an empty deck.
 */
public class EmptyDeckException extends RuntimeException{
    public EmptyDeckException(String message){super(message);}
}