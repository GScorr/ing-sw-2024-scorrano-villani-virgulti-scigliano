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
    public void setInitialCardsInHand(List<PlayCard> cards_in_hand) {
        throw new InvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override
    public void setInitialGoalCards(List<Goal> initial_goal_cards) {
        throw new InvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override
    public void setStartingCard(PlayCard starting_card) {
        throw new InvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override
    public void selectGoal(int i) {
        throw new InvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override
    public void selectStartingCard(boolean flipped) {
        throw new InvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override
    public void placeCard(int index, boolean flipped, int x, int y) {
        throw new InvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override
    public void peachCardFromGoldDeck() {
        this.player.peachCardFromGoldDeck();
    }

    @Override
    public void peachFromResourcesDeck() {
        this.player.peachFromResourcesDeck();
    }

    @Override
    public void peachFromCardsInCenter(int i) {
        this.player.peachFromCardsInCenter(i);
    }

    @Override
    public String getNameState() {
        return "DRAW_CARD";
    }
}
