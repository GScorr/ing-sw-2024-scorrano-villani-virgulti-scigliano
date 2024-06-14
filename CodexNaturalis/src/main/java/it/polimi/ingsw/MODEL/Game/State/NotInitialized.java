package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;

/**
 * Represents the initial state of the game before setup.
 */
public class NotInitialized implements GameState, Serializable {

    Game game;

    public NotInitialized(Game game){
        this.game = game;
    }

    /**
     * Allows adding players to the game during the NotInitialized state.
     *
     * @param player The player to add.
     * @return True (player added successfully).
     */
    @Override
    public boolean insertPlayer(Player player) {
        game.insertPlayer(player);
        return true;
    }

    /**
     * The game is not yet initialized in this state.
     *
     * @return Always false.
     */
    @Override
    public boolean initializedGame() {
        return false;
    }

    @Override
    public String getNameState() {
        return "NOT_INITIALIZED";
    }

}
