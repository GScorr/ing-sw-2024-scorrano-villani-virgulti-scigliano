package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MODEL.Player.State.InvalidStateException;

public class NotInitialized implements GameState{
    Game game;
    public NotInitialized(Game game){
        this.game = game;
    }
    @Override
    public void insertPlayer(Player player) {
        game.insertPlayer(player);
    }

    @Override
    public void initializedGame() {
        throw new GameInvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override

    public String getNameState() {
        return "NOT_INITIALIZED";
    }
}
