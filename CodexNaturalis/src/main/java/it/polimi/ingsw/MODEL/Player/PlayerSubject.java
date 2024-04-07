package it.polimi.ingsw.MODEL.Player;

import it.polimi.ingsw.MODEL.Game.GameObserver;

public interface PlayerSubject {
    void registerObserver(GameObserver gameObserver); // Metodo per registrare un nuovo osservatore
    void removeObserver(GameObserver gameObserver);  // Metodo per rimuovere un osservatore
    void notifyObservers();
}
