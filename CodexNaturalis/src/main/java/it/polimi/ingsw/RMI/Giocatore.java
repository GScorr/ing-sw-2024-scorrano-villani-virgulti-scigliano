package it.polimi.ingsw.RMI;

import java.util.ArrayList;

public class Giocatore {
    private final String name;

    private Integer[] campo;

    public Giocatore(String name) {
        this.name = name;
        this.campo = new Integer[9];
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


}
