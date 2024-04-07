package it.polimi.ingsw.CONTROLLER;

import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;

public class GameController {
    private Game game;


    public GameController(){
        this.game = new Game();
    }

    public Player createPlayer(String nome){
        Player player = new Player(ColorsEnum.GREEN, nome);
        this.game.insertPlayer(player);
        return player;
    }



}
