package it.polimi.ingsw.MODEL.Game;

import it.polimi.ingsw.MODEL.Player.PlayerObserver;

public interface GameSubject {
    void registerObserver(PlayerObserver playerObserver); // Metodo per registrare un nuovo osservatore
    void removeObserver(PlayerObserver playerObserver);  // Metodo per rimuovere un osservatore
    void notifyObservers();                  // Metodo per notificare tutti gli osservatori registrati
}
