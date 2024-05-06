package it.polimi.ingsw.SOCKET_FINAL;

import java.io.IOException;

public interface VirtualServer {
    public void inserisciGiocatore(String nome) throws IOException;
    public void lunchMessage(String message) throws IOException;
    public void getMessage();
    public void getPartita();
}
