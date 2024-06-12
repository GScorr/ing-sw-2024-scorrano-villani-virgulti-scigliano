package it.polimi.ingsw.MODEL.Player.State;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;
import java.util.List;

/**
 * This class rappresent the state in wich the player is waiting and no action are allowed
 */
public class WaitTurn implements PState, Serializable {

    Player player;

    public WaitTurn(Player player) {
        this.player = player;
    }

    @Override
    public boolean setInitialCardsInHand(List<PlayCard> cards_in_hand) {
        return false;
    }

    @Override
    public boolean setInitialGoalCards(List<Goal> initial_goal_cards) {
        return false;
    }

    @Override
    public boolean setStartingCard(PlayCard starting_card) {
        return false;
    }

    /**
     * the followers method are not implemented in the begin state because no action will be acepted in this state
     */

    @Override
    public boolean selectGoal(int i) {
        return false;
    }

    @Override
    public boolean selectStartingCard(boolean flipped) {
        return false;
    }

    @Override
    public boolean placeCard(int index, boolean flipped, int x, int y) {
        return false;
    }

    @Override
    public boolean peachCardFromGoldDeck() {
        return false;
    }

    @Override
    public boolean peachFromResourcesDeck() {
        return false;
    }

    @Override
    public boolean peachFromCardsInCenter(int i) {
        return false;
    }

    /**
     * Select the side of the card.
     *
     * @param index the index of the card to place in player's hand.
     * @param flip whether to flip the card (true) or keep it face down (false)
     * @return always returns true after calling the player object's method
     */
    public boolean selectSideCard(int index, boolean flip){
        player.selectSideCard(index,flip);
        return true;
    }

    @Override
    public String getNameState() {
        return "WAIT_TURN";
    }
}
