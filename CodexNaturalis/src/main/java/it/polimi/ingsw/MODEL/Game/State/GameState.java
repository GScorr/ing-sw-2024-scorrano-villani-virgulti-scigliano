package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Player.Player;

public interface GameState {

    void insertPlayer(Player player);
    void initializedGame();

    String getNameState();


}