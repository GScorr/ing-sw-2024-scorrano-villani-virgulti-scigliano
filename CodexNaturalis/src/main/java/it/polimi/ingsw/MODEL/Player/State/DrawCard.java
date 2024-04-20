package it.polimi.ingsw.MODEL.Player.State;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;

import java.util.List;

public class DrawCard implements PState{
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
        this.player.peachCardFromGoldDeck();
        return true;
    }

    @Override
    public boolean peachFromResourcesDeck() {
        this.player.peachFromResourcesDeck();
        return true;
    }

    @Override
    public boolean peachFromCardsInCenter(int i) {
        this.player.peachFromCardsInCenter(i);
        return true;
    }
    public boolean selectSideCard(int index, boolean flip){
        player.side_card_in_hand.put(index,flip);
        return true;
    }

    @Override
    public String getNameState() {
        return "DRAW_CARD";
    }
}
