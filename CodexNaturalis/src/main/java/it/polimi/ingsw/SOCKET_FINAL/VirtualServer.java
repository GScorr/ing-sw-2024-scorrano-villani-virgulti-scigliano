package it.polimi.ingsw.SOCKET_FINAL;

import java.io.IOException;

public interface VirtualServer {
    public void inserisciGiocatore(String nome, String token) throws IOException;
    public void lunchMessage(String message, String token) throws IOException;
    public void getMessage();
    public void getPartita();
}
