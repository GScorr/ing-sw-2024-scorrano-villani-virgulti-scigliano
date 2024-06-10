package it.polimi.ingsw.MODEL.DeckPackage;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;

import java.io.Serializable;
import java.util.Deque;

public class DeckGoalCard implements Serializable {

    private Deque<Goal> cards;

    public DeckGoalCard(Deque<Goal> cards){
        this.cards = cards;
    }


    public Goal drawCard(){
        if(!cards.isEmpty()){
            return cards.removeFirst();
        }
        else throw new EmptyDeckException("GOAL DECK IS EMPTY");
    }

    public Deque<Goal> getCards(){
        return cards;
    }

    public Goal seeFirstCard(){return cards.getFirst();}

    public int getNumber(){
        return cards.toArray().length;
    }

}
