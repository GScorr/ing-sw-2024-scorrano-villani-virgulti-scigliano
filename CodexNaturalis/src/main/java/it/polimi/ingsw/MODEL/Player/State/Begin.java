package it.polimi.ingsw.MODEL.Player.State;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;
import java.util.List;

public class Begin implements PState, Serializable {
    Player player;

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

    public boolean selectSideCard(int index, boolean flip){
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
        return "BEGIN";
    }
}
