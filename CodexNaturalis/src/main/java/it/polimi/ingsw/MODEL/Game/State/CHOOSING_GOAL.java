package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;

/**
 * Represents the game state where players are choosing their secret goals.
 */
public class CHOOSING_GOAL implements GameState, Serializable {

    Game game;

    public CHOOSING_GOAL(Game game){
        this.game = game;
    }

    /**
     * Players cannot be added during the ChoosingGoal state.
     *
     * @param player The player to add.
     * @return Always false.
     */
    @Override
    public boolean insertPlayer(Player player) {
        return false;
    }

    /**
     * The game is already initialized in this state.
     *
     * @return Always false.
     */
    @Override
    public boolean initializedGame() {
        return false;
    }

    @Override
    public String getNameState() {
        return "CHOOSING_GOAL";
    }

}
