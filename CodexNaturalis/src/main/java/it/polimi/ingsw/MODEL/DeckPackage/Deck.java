package it.polimi.ingsw.MODEL.DeckPackage;
import it.polimi.ingsw.MODEL.Card.PlayCard;

import java.io.Serializable;
import java.util.Deque;
import java.util.EmptyStackException;

/*
* @Francesco Virgulti
*/
public class Deck implements Serializable {
    public Deque<PlayCard> cards;

    //il deck
    public Deck(Deque<PlayCard> cards){
        this.cards = cards;
    }


    public PlayCard drawCard(){
        if(!cards.isEmpty()){
            return cards.removeFirst();
        }
        else throw new EmptyDeckException("EMPTY DECK");
    }
    public PlayCard seeFirstCard(){return cards.getFirst();}

    public int getNumber(){
        return cards.toArray().length;
    }

}
