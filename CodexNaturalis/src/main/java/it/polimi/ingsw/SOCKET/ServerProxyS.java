package it.polimi.ingsw.SOCKET;

import it.polimi.ingsw.SOCKET.GiocoProva.Partita;

import java.io.PrintWriter;

import java.io.BufferedWriter;
import java.io.PrintWriter;

public class ServerProxyS implements VirtualServerS{

    final PrintWriter output;

    public ServerProxyS(BufferedWriter output) {
        this.output = new PrintWriter(output);
    }


    public void inserisciGiocatore(String nome, String token){
        output.println("Inserisci_Giocatore");
        output.println(nome);
        output.println(token);
        output.flush();
    }
    public void lunchMessage(String message, String token){
        output.println("lunch_message");
        output.println(message);
        output.println(token);
        output.flush();
    }

    @Override
    public void getMessage() {

    }

    public void getMessage(String token){
        output.println("getMessage");
        output.println(token);
        output.flush();
    }

    /**
     * da togliere
     */
    public void getPartita(){

    }
}
