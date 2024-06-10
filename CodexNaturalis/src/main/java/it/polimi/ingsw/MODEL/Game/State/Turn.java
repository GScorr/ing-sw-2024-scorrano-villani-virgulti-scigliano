package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;

/**
 * Represents the state of the game where a player is taking their turn.
 */
public class Turn implements GameState, Serializable {

    Game game;

    public Turn(Game game){
        this.game = game;
    }

    /**
     * Players cannot be added during a player's turn.
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
        return "TURN";
    }

}
