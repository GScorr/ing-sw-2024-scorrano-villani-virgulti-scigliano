package it.polimi.ingsw.MODEL.DeckPackage;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Game.DeckCreation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/*
todo
    da riprender perche è stato interroto quando mancavano i goal
 */
class DeckGoalCardTest {

    DeckCreation creation = new DeckCreation();

    Deck resource_deck = new Deck(creation.getMixResourcesDeck());
    Deck gold_deck = new Deck(creation.getMixGoldDeck());

    List<PlayCard> card_in_hands = new ArrayList<>();
    List<PlayCard> carte_oro_in_mano = new ArrayList<>();
    /*
    in attesa che finisca la classe deckcreation con il deck goal
     */

    @Test
    void drawCard() {
    }

    @Test
    void seeFirstCard() {
    }

    @Test
    void getNumber() {
    }
}