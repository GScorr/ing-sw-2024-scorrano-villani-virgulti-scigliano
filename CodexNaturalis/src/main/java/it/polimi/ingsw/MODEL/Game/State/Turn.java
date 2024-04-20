package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;

public class Turn implements GameState{
    Game game;
    public Turn(Game game){
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
        return "TURN";
    }
}
