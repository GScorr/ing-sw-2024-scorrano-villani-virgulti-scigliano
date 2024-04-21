package it.polimi.ingsw.MODEL.Player.State;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;
import java.util.List;

public class ChooseGoal implements PState, Serializable {
    Player player;

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

    @Override
    public boolean selectGoal(int i) {
        this.player.selectGoal(i);
        return true;
    }

    public boolean selectSideCard(int index, boolean flip){
        player.side_card_in_hand.put(index,flip);
        return true;
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

    @Override
    public String getNameState() {
        return "CHOOSE_GOAL";
    }
}
