package it.polimi.ingsw.MODEL.Player.State;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;
import java.util.List;

/*
    todo
        Player player non è ne private ne public
 */

/**
 * Represents the beginning state of a player in the game.
 *
 * This class implements the `PState` interface to manage player actions during the game's initial state.
 * In this state, the player cannot perform most gameplay actions, such as placing cards, selecting
 * cards, or taking penalty actions. The available actions are limited to setting up the player's hand,
 * goal cards, and potentially the starting card.
 */
public class Begin implements PState, Serializable {

    /**
     * reference to the player
     */
    Player player;

    /**
     * Constructor to initialize the `Begin` state with the player object.
     *
     * @param player the player object representing the current player
     */
    public Begin(Player player) {
        this.player = player;
    }

    @Override
    public boolean setInitialCardsInHand(List<PlayCard> cards_in_hand) {
        this.player.setInitialCardsInHand(cards_in_hand);
        return true;
    }

    @Override
    public boolean setInitialGoalCards(List<Goal> initial_goal_cards) {
        this.player.setInitialGoalCards(initial_goal_cards);
        return true;
    }

    @Override
    public boolean setStartingCard(PlayCard starting_card) {
        this.player.setStartingCard(starting_card);
        return true;
    }

    /**
     * the followers method are not implemented in the begin state because no action will be acepted in this state
     */

    /**
     * @param index the index of the card to select
     * @param flip whether to flip the card (true) or keep it face down (false)
     * @return always returns `false` as card selection  is not allowed
     *
     */
    public boolean selectSideCard(int index, boolean flip){
        return false;
    }

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
     * @return always returns `false` as taking penalty actions from the Gold Deck is not allowed
     */
    @Override
    public boolean peachCardFromGoldDeck() {
        return false;
    }

    /**
     *
     * @return always returns `false` as taking penalty actions from the Resources Deck is not allowed
     */
    @Override
    public boolean peachFromResourcesDeck() {
        return false;
    }

    /**
     *
     * @param i the index of the card to take from the center cards
     * @return always returns `false` as taking penalty actions from the center cards is not allowed
     */
    @Override
    public boolean peachFromCardsInCenter(int i) {
        return false;
    }

    @Override
    public String getNameState() {
        return "BEGIN";
    }

}
