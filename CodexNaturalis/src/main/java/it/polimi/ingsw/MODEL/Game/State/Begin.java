package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;

public class Begin implements GameState{
    Game game;
    public Begin(Game game){
        this.game = game;
    }

    @Override
    public void insertPlayer(Player player) {
        throw new GameInvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override
    public void initializedGame() {
        game.initializedGame();
    }


    @Override
    public String getNameState() {
        return "BEGIN";
    }
}
