package it.polimi.ingsw.MODEL.DeckPackage;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;

import java.io.Serializable;
import java.util.Deque;

public class DeckGoalCard implements Serializable {

    private Deque<Goal> cards;

    //il deck
    public DeckGoalCard(Deque<Goal> cards){
        this.cards = cards;
    }


    public Goal drawCard(){
        if(cards.size()>0){
            return cards.pop();
        }
        else throw new EmptyDeckException("errore mazzo vuoto in drawCard in DeckGoalCard");
    }

    public Goal seeFirstCard(){return cards.getFirst();}

    public int getNumber(){
        return cards.toArray().length;
    }

}
