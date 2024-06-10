package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;

/**
 * Represents the final state of the game where the winner is determined.
 */
public class EndGame implements GameState, Serializable {

    Game game;

    public EndGame(Game game){
        this.game = game;
    }

    /**
     * Players cannot be added during the EndGame state.
     *
     * @param player The player to add.
     * @return Always false.
     */
    @Override
    public boolean insertPlayer(Player player) {
        return false;

    }

    /**
     * The game is already initialized and has ended.
     *
     * @return Always false.
     */
    @Override
    public boolean initializedGame() {
        return false;
    }

    @Override
    public String getNameState() {
        return "ENDGAME";
    }

}
