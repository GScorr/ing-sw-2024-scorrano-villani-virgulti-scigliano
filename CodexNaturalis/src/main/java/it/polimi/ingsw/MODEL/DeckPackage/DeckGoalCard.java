package it.polimi.ingsw.MODEL.DeckPackage;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;

import java.util.Deque;

public class DeckGoalCard {

    private Deque<Goal> cards;

    //il deck
    public DeckGoalCard(Deque<Goal> cards){
        this.cards = cards;
    }


    public Goal drawCard(){
        return cards.pop();
    };
    public Goal seeFirstCard(){return cards.getFirst();}

    public int getNumber(){
        return cards.toArray().length;
    }

}
