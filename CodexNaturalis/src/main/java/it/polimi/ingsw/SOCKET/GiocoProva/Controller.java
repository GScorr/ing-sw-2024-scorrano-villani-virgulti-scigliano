package it.polimi.ingsw.SOCKET.GiocoProva;

import it.polimi.ingsw.CONTROLLER.ControllerException;

public class Controller {
    private Partita partita = new Partita();

    public Controller() {

    }
    public Giocatore inserisciGiocatore(String nome){
        Giocatore tmp = new Giocatore(nome);
        if(partita.inserisciGiocatore(tmp)){
            return tmp;
        }
        else{
            throw new ControllerException(0,"errore creazione giocatore");
        }
    }

    public void lunchMessage(String message){
        partita.setMessage(message);
    }
    public String getMessage(){
        return partita.getMessage();
    }

    public Partita getPartita() {
        return partita;
    }
}
