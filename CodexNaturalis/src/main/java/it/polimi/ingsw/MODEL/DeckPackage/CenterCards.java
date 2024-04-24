package it.polimi.ingsw.MODEL.DeckPackage;

import it.polimi.ingsw.MODEL.Card.PlayCard;

import java.io.Serializable;
import java.util.List;

public class CenterCards implements Serializable {
    private List<PlayCard>  gold_list;
    private List<PlayCard> resource_list;
    private Deck gold_deck;
    private Deck resources_deck;

    public CenterCards(List<PlayCard> gold_list, List<PlayCard> resource_list, Deck gold_deck, Deck resources_deck) {
        this.gold_list = gold_list;
        this.resource_list = resource_list;
        this.gold_deck = gold_deck;
        this.resources_deck = resources_deck;
    }

    public void setGold_list(List<PlayCard> gold_list) {
        this.gold_list = gold_list;
    }

    public void setResource_list(List<PlayCard> resource_list) {
        this.resource_list = resource_list;
    }


    public List<PlayCard> getGold_list() {
        return gold_list;
    }

    public List<PlayCard> getResource_list() {
        return resource_list;
    }

    public PlayCard drawGoldCard(int index){
        if(gold_deck.cards.size() > 0){
            PlayCard tmp = gold_list.get(index);
            gold_list.remove(index);
            gold_list.add(insertFromGoldDeck(index));
            return tmp;
        }
        else {
            PlayCard tmp = gold_list.get(index);
            gold_list.remove(index);
            return tmp;
        }

    }

    public PlayCard drawResourceCard(int index){
        if(resource_list.size()>0){
            PlayCard tmp = resource_list.get(index);
            resource_list.remove(index);
            resource_list.add(insertFromResourcesDeck(index));
            return tmp;
        }
        else throw new EmptyDeckException("errore mazzo vuoto in drawResourceCard");

    }

    //If a GoldCard has been took from drawGoldCard a Card has to be drawed from the GoldDeck
    private PlayCard insertFromGoldDeck(int index){
        return gold_deck.drawCard();
    }

    //If a Resourced Card has been took from drawGoldCard a Card has to be drawed from the ResourceDeck
    private PlayCard insertFromResourcesDeck(int index){
        return resources_deck.drawCard();
    }
}
