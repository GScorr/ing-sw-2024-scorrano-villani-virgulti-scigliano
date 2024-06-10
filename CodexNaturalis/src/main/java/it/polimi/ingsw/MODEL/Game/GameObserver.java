package it.polimi.ingsw.MODEL.Game;

/**
 * Interface for Observing Game State Changes.
 */
public interface GameObserver {
    void updateChooseGoal();
    void updateChooseStartingCard();
}
