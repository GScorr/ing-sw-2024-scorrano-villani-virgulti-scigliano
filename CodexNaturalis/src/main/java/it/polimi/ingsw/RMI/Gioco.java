package it.polimi.ingsw.RMI;

import java.io.Serializable;
import java.util.ArrayList;

public class Gioco implements Serializable {
    final private String name;

    final private Giocatore player1;

    private Giocatore player2;


    public Gioco(String name, Giocatore player1) {
        this.name = name;
        this.player1 = player1;
    }

    public String getName() {
        return name;
    }

    public Giocatore getPlayer1() {
        return player1;
    }
    public Giocatore getPlayer2() {
        return player2;
    }

}
