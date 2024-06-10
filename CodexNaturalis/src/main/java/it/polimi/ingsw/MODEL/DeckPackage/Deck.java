package it.polimi.ingsw.MODEL.DeckPackage;
import it.polimi.ingsw.MODEL.Card.PlayCard;

import java.io.Serializable;
import java.util.Deque;

/**
 * Represents a deck of palycards in the game.
 */
public class Deck implements Serializable {
    /**
     * the cards in the deck
     */
    public Deque<PlayCard> cards;

    /**
     * creates a new deck istance
     * @param cards the inidial deck of cards
     */
    public Deck(Deque<PlayCard> cards){
        this.cards = cards;
    }

    /**
     * Draws a card from the deck.
     *
     * @return The drawn card.
     * @throws EmptyDeckException If the deck is empty.
     */
    public PlayCard drawCard(){
        if(!cards.isEmpty()){
            return cards.removeFirst();
        }
        else throw new EmptyDeckException("EMPTY DECK");
    }

    /**
     * Peeks at the top card of the deck without removing it.
     *
     * @return The top card, or null if the deck is empty.
     */
    public PlayCard seeFirstCard(){return cards.getFirst();}

    public int getNumber(){
        return cards.toArray().length;
    }

}
