package it.polimi.ingsw.MODEL.DeckPackage;

import it.polimi.ingsw.MODEL.Card.PlayCard;

import java.io.Serializable;
import java.util.List;

/**
 * Manages the 2 decks and the 4 central cards from which it is possible to draw Gold and Resource cards.
 */
public class CenterCards implements Serializable {
    /**
     * 2 face-up gold card
     */
    private List<PlayCard>  gold_list;
    /**
     * 2 face-up resource card
     */
    private List<PlayCard> resource_list;
    /**
     * Deck for drawing new Cards.
     */
    private Deck gold_deck;
    private Deck resources_deck;

    /**
     * Creates a new CenterCards instance.
     *
     * @param gold_list
     * @param resource_list
     * @param gold_deck Deck for drawing new Gold Cards.
     * @param resources_deck Deck for drawing new Resource Cards.
     */
    public CenterCards(List<PlayCard> gold_list, List<PlayCard> resource_list, Deck gold_deck, Deck resources_deck) {
        this.gold_list = gold_list;
        this.resource_list = resource_list;
        this.gold_deck = gold_deck;
        this.resources_deck = resources_deck;
    }

    public Deck getGold(){
        return gold_deck;
    }
    public Deck getResource(){
        return resources_deck;
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

    /**
     * Draws a Gold Card from the face-up, refilling it from the deck if empty.
     *
     * @param index Index of the card to draw.
     * @return The drawn Gold Card.
     * @throws EmptyDeckException If the Gold deck is empty.
     */
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

    /**
     * Draws a Resource Card from the face-up, refilling it from the deck if empty.
     *
     * @param index Index of the card to draw.
     * @return The drawn Resource Card.
     * @throws EmptyDeckException If the Resource deck is empty.
     */
    public PlayCard drawResourceCard(int index){
        if(!resource_list.isEmpty()){
            PlayCard tmp = resource_list.get(index);
            resource_list.remove(index);
            resource_list.add(insertFromResourcesDeck(index));
            return tmp;
        }
        else throw new EmptyDeckException("errore mazzo vuoto in drawResourceCard");

    }

    /**
     * If a GoldCard has been took from drawGoldCard a Card has to be drawed from the GoldDeck
     * @param index
     * @return
     */
    private PlayCard insertFromGoldDeck(int index){
        return gold_deck.drawCard();
    }

    /**
     * If a Resourced Card has been took from drawGoldCard a Card has to be drawed from the ResourceDeck
     * @param index
     * @return
     */
    private PlayCard insertFromResourcesDeck(int index){
        return resources_deck.drawCard();
    }
}
