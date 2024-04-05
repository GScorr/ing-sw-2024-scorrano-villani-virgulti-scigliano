package it.polimi.ingsw.MODEL.Player.State;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;

import java.util.List;

public class ChooseGoal implements PState{
    Player player;

    public ChooseGoal(Player player) {
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
        this.player.selectGoal(i);
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
        throw new InvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override
    public void peachFromResourcesDeck() {
        throw new InvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override
    public void peachFromCardsInCenter(int i) {
        throw new InvalidStateException("Impossibile chiamare il metodo in questo stato.");
    }

    @Override
    public String getNameState() {
        return "CHOOSE_GOAL";
    }
}
