package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;

public class TurnEndGame implements GameState{
    Game game;
    public TurnEndGame(Game game){
        this.game = game;
    }
    @Override
    public void insertPlayer(Player player) {
        throw new GameInvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override
    public void initializedGame() {
        throw new GameInvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override


    public String getNameState() {
        return "TURN";
    }
}
