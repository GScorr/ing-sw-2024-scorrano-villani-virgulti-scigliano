package it.polimi.ingsw.MODEL.DeckPackage;
import it.polimi.ingsw.MODEL.Card.PlayCard;

import java.util.Deque;

/*
* @Francesco Virgulti
* TODO:
*  -getNumber()
*  - Implementa anche le sottoclassi
* */
public class Deck {
    public Deque<PlayCard> cards;

    //il deck
    public Deck(Deque<PlayCard> cards){
        this.cards = cards;
    }


    public PlayCard drawCard(){
        return cards.pop();
    };
    public PlayCard seeFirstCard(){return cards.getFirst();}

    public int getNumber(){
        return cards.toArray().length;
    }

}
