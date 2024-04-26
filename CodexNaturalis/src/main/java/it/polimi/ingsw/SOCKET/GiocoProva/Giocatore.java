package it.polimi.ingsw.SOCKET.GiocoProva;

import java.io.Serializable;

public class Giocatore implements Serializable {
    /**
     * Player semplificato

     */
    private String Name;

    public Giocatore(String name) {
        Name = name;
    }
    public String getName() {
        return Name;
    }
}
