package it.polimi.ingsw.RMI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Gioco implements Serializable {
    final private String name;
    private int index_game;
    private int numplayers;
    private List<Giocatore> players= new ArrayList<>();


    public Gioco(String name, int numplayers, Giocatore player1) {
        this.name = name;
        players.add(player1);
        this.numplayers = numplayers;
        this.index_game = IndexManager.getNextIndex();
    }

    public String getName() {
        return name;
    }
    public void getPlayersName(){
        //System.out.println("\nPartita-> "+ name + "Giocatore 1-> " + player1+ "Giocatore 2-> " + player2);
    }
    public synchronized  Giocatore getPlayer1() {
        return players.get(1);
    }
    public synchronized Giocatore getPlayer2() {
        return players.get(2);
    }

    public synchronized boolean insertPlayer(Giocatore player){
        if(players.size()<numplayers) {
            players.add(player);
            return true;
        }
        else return false;
    }

    public List<Giocatore> getPlayers() {
        return players;
    }

    public int getIndex_game() {
        return index_game;
    }

    public int getNumplayers() {
        return numplayers;
    }
}
