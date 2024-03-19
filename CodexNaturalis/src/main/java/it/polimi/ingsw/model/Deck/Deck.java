package it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Card.PlayCard;
import java.util.ArrayDeque;
import java.util.Deque;

/*
* @Francesco Virgulti
* TODO:
*  -getNumber()
*  -drawFromDeck()
*  - Implementa anche le sottoclassi
* */
abstract class Deck {
    Deque<PlayCard> cards;

    public Deck(Deque<PlayCard> cards){
        this.cards = cards;
    }

    public abstract PlayCard getStack();

}
