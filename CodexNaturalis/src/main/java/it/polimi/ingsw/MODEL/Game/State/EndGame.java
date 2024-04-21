package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;

public class EndGame implements GameState, Serializable {
    Game game;
    public EndGame(Game game){
        this.game = game;
    }
    @Override
    public boolean insertPlayer(Player player) {
        return false;

    }

    @Override
    public boolean initializedGame() {
        return false;
    }

    @Override


    public String getNameState() {
        return "ENDGAME";
    }
}
