package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MODEL.Player.State.InvalidStateException;

import java.io.Serializable;

public class NotInitialized implements GameState, Serializable {
    Game game;
    public NotInitialized(Game game){
        this.game = game;
    }
    @Override
    public boolean insertPlayer(Player player) {
        game.insertPlayer(player);
        return true;
    }

    @Override
    public boolean initializedGame() {
        return false;
    }

    @Override

    public String getNameState() {
        return "NOT_INITIALIZED";
    }
}
