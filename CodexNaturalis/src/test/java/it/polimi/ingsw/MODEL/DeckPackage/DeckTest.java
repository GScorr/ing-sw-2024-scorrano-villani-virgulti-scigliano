package it.polimi.ingsw.MODEL.DeckPackage;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Game.DeckCreation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    DeckCreation creation = new DeckCreation();

    Deck resource_deck = new Deck(creation.getMixResourcesDeck());
    Deck gold_deck = new Deck(creation.getMixGoldDeck());

    List<PlayCard> card_in_hands = new ArrayList<>();
    List<PlayCard> carte_oro_in_mano = new ArrayList<>();

    @Test
    void drawCard() {
        /*
        primo test di pescata
        System.err.println(resource_deck.getNumber());
        card_in_hands.add(resource_deck.drawCard());
        System.err.println(card_in_hands.get(0).getSide().getAngleLeftUp()+ " " + card_in_hands.get(0).getSide().getAngleRightUp());
        System.err.println("    " + card_in_hands.get(0).getSide().getCentral_resource());
        System.err.println("    " + card_in_hands.get(0).getSide().getCentral_resource2());
        System.err.println("    " + card_in_hands.get(0).getSide().getCentral_resource3());
        System.err.println(card_in_hands.get(0).getSide().getAngleLeftDown() + " " + card_in_hands.get(0).getSide().getAngleRightDown());
        System.err.println(resource_deck.getNumber());
        card_in_hands.clear();
         */

        /*
            test effettivo draw_card, pesco 3 carte e controllo che abbia in mano tre carte e che nel resource_deck ci siano 37 carte
         */
        //System.err.println(resource_deck.getNumber());
       card_in_hands.add(resource_deck.drawCard()); //pesco
        System.out.println("carta 1 ");
        System.err.println(card_in_hands.get(0).getSide().getAngleLeftUp()+ " " + card_in_hands.get(0).getSide().getAngleRightUp());
        System.err.println("    " + card_in_hands.get(0).getSide().getCentral_resource());
        System.err.println("    " + card_in_hands.get(0).getSide().getCentral_resource2());
        System.err.println("    " + card_in_hands.get(0).getSide().getCentral_resource3());
        System.err.println(card_in_hands.get(0).getSide().getAngleLeftDown() + " " + card_in_hands.get(0).getSide().getAngleRightDown());
        //System.err.println(resource_deck.getNumber());
        card_in_hands.add(resource_deck.drawCard());
        System.out.println("carta 2 ");
        System.err.println(card_in_hands.get(1).getSide().getAngleLeftUp()+ " " + card_in_hands.get(1).getSide().getAngleRightUp());
        System.err.println("    " + card_in_hands.get(1).getSide().getCentral_resource());
        System.err.println("    " + card_in_hands.get(1).getSide().getCentral_resource2());
        System.err.println("    " + card_in_hands.get(1).getSide().getCentral_resource3());
        System.err.println(card_in_hands.get(1).getSide().getAngleLeftDown() + " " + card_in_hands.get(1).getSide().getAngleRightDown());
        //System.err.println(resource_deck.getNumber());
        card_in_hands.add(resource_deck.drawCard());
        System.out.println("carta 3 ");
        System.err.println(card_in_hands.get(2).getSide().getAngleLeftUp()+ " " + card_in_hands.get(2).getSide().getAngleRightUp());
        System.err.println("    " + card_in_hands.get(2).getSide().getCentral_resource());
        System.err.println("    " + card_in_hands.get(2).getSide().getCentral_resource2());
        System.err.println("    " + card_in_hands.get(2).getSide().getCentral_resource3());
        System.err.println(card_in_hands.get(2).getSide().getAngleLeftDown() + " " + card_in_hands.get(2).getSide().getAngleRightDown());
        //System.err.println(resource_deck.getNumber());

        carte_oro_in_mano.add(gold_deck.drawCard());
        carte_oro_in_mano.add(gold_deck.drawCard());
        carte_oro_in_mano.add(gold_deck.drawCard());
        System.out.println("stampo la mano di carte oro:");
        for(int i=0; i<carte_oro_in_mano.size(); i++){
            System.out.println("carta "+(i+1));
            System.out.println(card_in_hands.get(1));
            System.err.println(card_in_hands.get(i).getSide().getAngleLeftUp()+ " " + card_in_hands.get(i).getSide().getAngleRightUp());
            System.err.println("    " + card_in_hands.get(i).getSide().getCentral_resource());
            System.err.println("    " + card_in_hands.get(i).getSide().getCentral_resource2());
            System.err.println("    " + card_in_hands.get(i).getSide().getCentral_resource3());
            System.err.println(card_in_hands.get(i).getSide().getAngleLeftDown() + " " + card_in_hands.get(i).getSide().getAngleRightDown());
        }

 /*
    test sull'errore di deck vuoto

        for(int i=0; i<= 45; i++){
            carte_oro_in_mano.add(gold_deck.drawCard());
        }
*/
        assertEquals(3, card_in_hands.size());
        assertEquals(37, resource_deck.getNumber());
        assertEquals(3, carte_oro_in_mano.size());
        assertEquals(37, gold_deck.getNumber());

    }
/*
non le testo perche la get number l'ho usata e funziona (poi si tratta di una getter) mentre la seeFirstCard non serve
 */
    @Test
    void seeFirstCard() {

    }

    @Test
    void getNumber() {
    }
}