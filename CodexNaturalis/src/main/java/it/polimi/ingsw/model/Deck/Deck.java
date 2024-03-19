package it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Card.PlayCard;
import java.util.ArrayDeque;
import java.util.Deque;

/*
* @Francesco Virgulti
* TODO:
*  -getNumber()
*  - Implementa anche le sottoclassi
* */
abstract class Deck {
    Deque<PlayCard> cards;

    //il deck
    public Deck(Deque<PlayCard> cards){
        this.cards = cards;
    }


    public PlayCard drawCard(){
        return cards.pop();
    };

    public int getNumber(){
        return cards.toArray().length;
    }


}
