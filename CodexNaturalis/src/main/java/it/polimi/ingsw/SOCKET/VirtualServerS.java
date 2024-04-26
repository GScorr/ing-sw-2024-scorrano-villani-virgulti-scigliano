package it.polimi.ingsw.SOCKET;

import it.polimi.ingsw.SOCKET.GiocoProva.Partita;

public interface VirtualServerS {
    public void inserisciGiocatore(String nome, String token);
    public void lunchMessage(String message, String token);
    public void getMessage();
    public void getPartita();
}
