package it.polimi.ingsw.MODEL.Game;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.Goal.Goal;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckCreationTestGoal {
    DeckCreation deck = new DeckCreation();
    Deque<Goal> goals;
    Deque<PlayCard> gold;
    @Test
    void creteGoalDeck() {
        goals=deck.getGoalDeck();
        for(Goal g:goals){
            System.out.println(g.getGoalType() + "e" + g.getResource());
        }
    }
}