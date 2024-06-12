package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Player.Player;

public interface GameState {

    boolean insertPlayer(Player player);
    boolean initializedGame();

    String getNameState();

}