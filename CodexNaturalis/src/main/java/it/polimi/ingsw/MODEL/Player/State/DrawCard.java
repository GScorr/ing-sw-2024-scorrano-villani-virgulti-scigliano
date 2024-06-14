package it.polimi.ingsw.MODEL.Player.State;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a state where the player draws a card.
 *
 * In this state, the player can:
 *   - Draw cards from the Gold Deck or Resources Deck.
 *   - Take cards from the card in center.
 *
 */
public class DrawCard implements PState, Serializable {

    /**
     * reference to the player
     */
    Player player;

    public DrawCard(Player player) {
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

    /**
     * @param i the index of the goal card to select
     * @return always returns `false` as goal card selection is not allowed
     */
    @Override
    public boolean selectGoal(int i) {
        return false;
    }

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
     * Initiates drawiing a card from the Gold Deck through the player object.
     *
     * @return always returns true after calling the player object's method
     */
    @Override
    public boolean peachCardFromGoldDeck() {
        this.player.peachCardFromGoldDeck();
        return true;
    }

    /**
     * Initiates drawing a card from the Resources Deck through the player object.
     *
     * @return always returns true after calling the player object's method
     */
    @Override
    public boolean peachFromResourcesDeck() {
        this.player.peachFromResourcesDeck();
        return true;
    }

    /**
     * Initiates drawing a card from the card in center.
     *
     * @return always returns true after calling the player object's method
     */
    @Override
    public boolean peachFromCardsInCenter(int i) {
        this.player.peachFromCardsInCenter(i);
        return true;
    }

    /**
     *  Select the side of the card.
     * @param index the index of the card
     * @param flip whether to flip the card (true) or keep it face down (false)
     * @return always returns true after calling the player object's method
     */
    public boolean selectSideCard(int index, boolean flip){
        player.selectSideCard(index,flip);
        return true;
    }

    @Override
    public String getNameState() {
        return "DRAW_CARD";
    }
}
