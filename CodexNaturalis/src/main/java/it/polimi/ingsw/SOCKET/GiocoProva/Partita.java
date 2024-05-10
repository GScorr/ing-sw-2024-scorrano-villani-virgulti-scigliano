package it.polimi.ingsw.SOCKET.GiocoProva;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Partita implements Serializable {
    /**
     * partita semplificata

     */
    List<Giocatore> giocatori = new ArrayList<>();
    int num_player=0;
    String Message;

    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }
    public boolean inserisciGiocatore(Giocatore g){
        if(giocatori.size()==4){
            return false;
        }
        else{
            giocatori.add(g);
            num_player++;
            return true;
        }
    }

}
