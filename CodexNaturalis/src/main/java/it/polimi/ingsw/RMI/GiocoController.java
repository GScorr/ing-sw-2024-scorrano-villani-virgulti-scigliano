package it.polimi.ingsw.RMI;

import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class GiocoController implements Serializable {
     private Gioco game;

    public GiocoController(Gioco game) {
        this.game = game;
    }

    public GiocoController(String name, Giocatore player1) {
        this.game = new Gioco(name, player1);
    }

    public Giocatore getStatus1(){
        return game.getPlayer1();
    }
    public Giocatore getStatus2(){
        return game.getPlayer2();
    }

    public boolean putInArray(int index, Integer value, Giocatore player){
        if( (index < 0 || index > 10) || value < 0 ) return false;
        player.putValue(index, value);
         return true;
    }

    public Giocatore createPlayer(String name){
        Giocatore player;
        return  player = new Giocatore(name);
    }

    public Gioco getGame() {
        return game;
    }





}
