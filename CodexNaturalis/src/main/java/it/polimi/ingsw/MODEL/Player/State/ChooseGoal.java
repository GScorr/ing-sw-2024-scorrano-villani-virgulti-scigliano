package it.polimi.ingsw.MODEL.Player.State;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;
import java.util.List;

/**
 * The state where the player chooses a goal.
 *
 */
public class ChooseGoal implements PState, Serializable {

    /**
     * reference to the player
     */
    Player player;

    /**
     *
     * @param player
     */
    public ChooseGoal(Player player) {
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
     * Selects a goal card
     *
     * @param i the index of the goal card to select
     * @return `true` if the goal selection was successful, otherwise `false`
     */
    @Override
    public boolean selectGoal(int i) {
        this.player.selectGoal(i);
        return true;
    }

    /**
     * Selects a the side of the card
     *
     * @param index the index of the card to select
     * @param flip whether to flip the card (true) or keep it face down (false)
     * @return `true` if the card selection was successful, otherwise `false`
     */
    public boolean selectSideCard(int index, boolean flip){
        player.selectSideCard(index,flip);
        return true;
    }

    /**
     * the followers method are not implemented in the begin state because no action will be acepted in this state
     */

    /**
     * @param flipped whether to flip the starting card (true) or keep it face down (false)
     * @return always returns `false` as starting card selection orientation is not allowed
     */
    @Override
    public boolean selectStartingCard(boolean flipped) {
        return false;
    }

    /**
     * @param index the index of the card to place in the player's hand
     * @param flipped whether to flip the card (true) or keep it face down (false)
     * @param x the x-coordinate of the placement location
     * @param y the y-coordinate of the placement location
     * @return always returns `false` as card placement is not allowed
     */
    @Override
    public boolean placeCard(int index, boolean flipped, int x, int y) {
        return false;
    }

    /**
     * @return always returns `false` as taking a card from the Gold Deck is not allowed
     */
    @Override
    public boolean peachCardFromGoldDeck() {
        return false;
    }

    /**
     *
     * @return always returns `false` as taking a card from the Resources Deck is not allowed
     */
    @Override
    public boolean peachFromResourcesDeck() {
        return false;
    }

    /**
     *
     * @param i the index of the card to take from the center cards
     * @return always returns `false` as taking a card from the center cards is not allowed
     */
    @Override
    public boolean peachFromCardsInCenter(int i) {
        return false;
    }

    @Override
    public String getNameState() {
        return "CHOOSE_GOAL";
    }

}
