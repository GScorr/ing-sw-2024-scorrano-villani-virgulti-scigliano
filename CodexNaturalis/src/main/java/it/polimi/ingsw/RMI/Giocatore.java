package it.polimi.ingsw.RMI;

import it.polimi.ingsw.MODEL.Game.Game;

import java.io.Serializable;
import java.util.ArrayList;

public class Giocatore implements Serializable {
    private final String name;

    private Integer[] campo;

    private GiocoController game;

    public Giocatore(String name) {
        this.name = name;
        this.campo = new Integer[10];
    }

    public String getName() {
        return name;
    }

    public Integer[] getCampo() {
        return campo;
    }

    public void putValue( int index, Integer value ){
        campo[index] = value;
    }

    public GiocoController getGame(){
        return this.game;
    }
    public void setGame(GiocoController game){this.game = game;}
}
