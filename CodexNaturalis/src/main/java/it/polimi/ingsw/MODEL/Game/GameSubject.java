package it.polimi.ingsw.MODEL.Game;

import it.polimi.ingsw.MODEL.Player.PlayerObserver;

/**
 * Interface for managing game observers.
 */
public interface GameSubject {
    void registerObserver(PlayerObserver playerObserver); // method to register a new observer
    void removeObserver(PlayerObserver playerObserver);  // method to delete an observer
    void notifyObservers();                  // method to notify all registered observer
}