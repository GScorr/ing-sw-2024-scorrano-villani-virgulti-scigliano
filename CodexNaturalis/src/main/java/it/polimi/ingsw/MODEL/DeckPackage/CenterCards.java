package it.polimi.ingsw.MODEL.DeckPackage;

import it.polimi.ingsw.MODEL.Card.PlayCard;

import java.util.List;

public class CenterCards {
    private List<PlayCard>  gold_list;
    private List<PlayCard> resource_list;

    public CenterCards(List<PlayCard> gold_list, List<PlayCard> resource_list) {
        this.gold_list = gold_list;
        this.resource_list = resource_list;
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
        PlayCard tmp = gold_list.get(index);
        gold_list.remove(index);
        return tmp;
    }

    public PlayCard drawResourceCard(int index){
        PlayCard tmp = resource_list.get(index);
        resource_list.remove(index);
        return tmp;
    }

    public void insertFromGoldDeck(PlayCard card){
        gold_list.add(card);
    }

    public void insertFromResourcesDeck(PlayCard card){
        resource_list.add(card);
    }
}
