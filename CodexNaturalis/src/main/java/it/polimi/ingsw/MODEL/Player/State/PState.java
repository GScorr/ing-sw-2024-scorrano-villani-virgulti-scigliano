package it.polimi.ingsw.MODEL.Player.State;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;

import java.util.List;

public interface PState {

    void setInitialCardsInHand(List<PlayCard> cards_in_hand);
    void setInitialGoalCards(List<Goal> initial_goal_cards);
    void setStartingCard(PlayCard starting_card);
    void selectGoal(int i);
    void selectStartingCard(boolean flipped);
    void placeCard(int index, boolean flipped, int x, int y);

    void peachCardFromGoldDeck();
    void peachFromResourcesDeck();
    void peachFromCardsInCenter(int i);
    String getNameState();

}
