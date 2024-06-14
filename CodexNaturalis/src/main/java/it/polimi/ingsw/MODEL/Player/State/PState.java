package it.polimi.ingsw.MODEL.Player.State;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;

import java.util.List;


/**
 * Interface representing a player state in the game.
 *
 * Defines methods for managing player actions in different game phases.
 */
public interface PState {

    boolean setInitialCardsInHand(List<PlayCard> cards_in_hand);
    boolean setInitialGoalCards(List<Goal> initial_goal_cards);
    boolean setStartingCard(PlayCard starting_card);

    boolean selectGoal(int i);

    boolean selectStartingCard(boolean flipped);

    boolean placeCard(int index, boolean flipped, int x, int y);

    boolean peachCardFromGoldDeck();

    boolean peachFromResourcesDeck();

    boolean peachFromCardsInCenter(int i);

    boolean selectSideCard(int index, boolean flip);

    String getNameState();

}
