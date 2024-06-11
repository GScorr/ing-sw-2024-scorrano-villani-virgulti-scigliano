package it.polimi.ingsw.MODEL.Game.State;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;

/**
 * Represents the game state where players are choosing their starting cards.
 */
public class CHOOSING_STARTING_CARD implements GameState, Serializable {

    Game game;

    public CHOOSING_STARTING_CARD(Game game){
        this.game = game;
    }

    /**
     * Players cannot be added during the ChoosingStartingCard state.
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
        return "CHOOSING_STARTING_CARD";
    }

}
