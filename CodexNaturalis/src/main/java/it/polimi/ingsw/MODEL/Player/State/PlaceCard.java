package it.polimi.ingsw.MODEL.Player.State;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;
import java.util.List;

/**
 * This class rappresent the state in wich the player can place a card in the game field
 */
public class PlaceCard implements PState, Serializable {
    Player player;

    public PlaceCard(Player player) {
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

    @Override
    public boolean selectGoal(int i) {
        return false;
    }

    @Override
    public boolean selectStartingCard(boolean flipped) {
        return false;
    }

    /**
     * Select the card to play
     * @param index the index of the card to place in player's hand
     * @param flipped whether to flip the card (true) or keep it face down (false)
     * @param x x-coordinate in game filed
     * @param y y-coordinate in game field
     * @return
     */
    @Override
    public boolean placeCard(int index, boolean flipped, int x, int y) {
        this.player.placeCard(index,flipped,x,y);
        return true;
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

    /**
     * the followers method are not implemented in the begin state because no action will be acepted in this state
     */

    /**
     * @return always returns `false` as taking a card from the Gold Deck is not allowed
     */
    @Override
    public boolean peachCardFromGoldDeck() {
        return false;
    }

    /**
     *
     * @return always returns `false` as a card from the Resources Deck is not allowed
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
        return "PLACE_CARD";
    }
}
