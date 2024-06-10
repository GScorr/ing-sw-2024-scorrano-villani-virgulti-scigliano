package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;

/**
 * Represents the initial state of the game where setup occurs.
 */
public class Begin implements GameState, Serializable {

    Game game;

    public Begin(Game game){
        this.game = game;
    }

    /**
     * Players cannot be added during the Begin state.
     *
     * @param player The player to add.
     * @return Always false.
     */
    @Override
    public boolean insertPlayer(Player player) {
        return false;
    }

    /**
     * Transitions the game state to the initialized state.
     *
     * @return True (game initialization successful).
     */
    @Override
    public boolean initializedGame() {
        game.initializedGame();
        return true;
    }

    @Override
    public String getNameState() {
        return "BEGIN";
    }

}
