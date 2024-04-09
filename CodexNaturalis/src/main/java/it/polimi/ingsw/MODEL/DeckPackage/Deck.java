package it.polimi.ingsw.MODEL.DeckPackage;
import it.polimi.ingsw.MODEL.Card.PlayCard;

import java.util.Deque;
import java.util.EmptyStackException;

/*
* @Francesco Virgulti
* TODO:
*  - getNumber()
*  - Implementa anche le sottoclassi
* */
public class Deck {
    public Deque<PlayCard> cards;

    //il deck
    public Deck(Deque<PlayCard> cards){
        this.cards = cards;
    }


    public PlayCard drawCard(){
        if(cards.size()>0){
            return cards.pop();
        }
        else throw new EmptyDeckException("errore mazzo vuoto");
    }
    public PlayCard seeFirstCard(){return cards.getFirst();}

    public int getNumber(){
        return cards.toArray().length;
    }

}
