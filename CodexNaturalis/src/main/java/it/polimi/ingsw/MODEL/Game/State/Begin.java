package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;

public class Begin implements GameState, Serializable {
    Game game;
    public Begin(Game game){
        this.game = game;
    }

    @Override
    public boolean insertPlayer(Player player) {
        return false;
    }

    @Override
    public boolean initializedGame() {
        game.initializedGame();
        return true;
    }


    @Override
    public String getNameState() {
        return "BEGIN";
    }
}
