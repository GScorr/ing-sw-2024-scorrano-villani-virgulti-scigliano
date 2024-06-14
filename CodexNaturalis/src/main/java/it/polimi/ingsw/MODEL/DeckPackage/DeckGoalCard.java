package it.polimi.ingsw.MODEL.DeckPackage;

import it.polimi.ingsw.MODEL.Goal.Goal;

import java.io.Serializable;
import java.util.Deque;

/*
 *  todo:
 *   eliminare i metodi non usati
 */

/**
 * Represents a deck of Goal cards in the game.
 */
public class DeckGoalCard implements Serializable {
    /**
     * The cards in the deck.
     */
    private Deque<Goal> cards;

    /**
     * Creates a new DeckGoalCard instance.
     *
     * @param cards The initial deck of Goal cards.
     */
    public DeckGoalCard(Deque<Goal> cards){
        this.cards = cards;
    }

    /**
     * Draws a Goal card from the deck.
     *
     * @return The drawn Goal card.
     * @throws EmptyDeckException If the deck is empty.
     */
    public Goal drawCard(){
        if(!cards.isEmpty()){
            return cards.removeFirst();
        }
        else throw new EmptyDeckException("GOAL DECK IS EMPTY");
    }

    public Deque<Goal> getCards(){
        return cards;
    }

    //da eliminare
    public Goal seeFirstCard(){return cards.getFirst();}
    //da eliminare
    public int getNumber(){
        return cards.toArray().length;
    }

}
