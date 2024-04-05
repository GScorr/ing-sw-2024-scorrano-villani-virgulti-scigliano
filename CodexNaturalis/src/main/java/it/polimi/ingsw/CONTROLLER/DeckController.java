package it.polimi.ingsw.CONTROLLER;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
public class DeckController {
    Deck gold_deck;
    Deck resource_deck;
    CenterCards center_cards;

    public DeckController(Deck gold_deck, Deck resource_deck, CenterCards center_cards) {
        this.gold_deck = gold_deck;
        this.resource_deck = resource_deck;
        this.center_cards = center_cards;
    }

    public boolean checkDeckNotEmpty(Deck d) {
        return d.getNumber() != 0;

        //supposed that in the message there is the index of the card and a Card c, obtained through the view
   /* public void replaceCard(Message m){
        int index;
        PlayCard c;
        if(c instanceof GoldCard){
            drawResourceCard(index);
        }
        else {
            drawGoldCard(index);
        }

    }
*/


    }
}
