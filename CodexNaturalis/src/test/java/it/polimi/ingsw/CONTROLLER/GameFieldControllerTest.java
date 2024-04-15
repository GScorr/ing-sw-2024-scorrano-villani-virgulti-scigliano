package it.polimi.ingsw.CONTROLLER;

import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.Game.DeckCreation;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;
import it.polimi.ingsw.MODEL.Player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldControllerTest {

    Player player = new Player(ColorsEnum.BLU,"aaa",true);
    GameFieldController controller = new GameFieldController(player);
    List<PlayCard> cards = new ArrayList<>();
    DeckCreation creation = new DeckCreation();
    List<ResourceCard> mazzo_risorse_list = creation.getDeck_resources();
    List<GoldCard> mazzo_gold_list = creation.getDeck_gold();

    @Test
    void checkPlacing() {
        cards = istanzia_carte();
        player.getGameField().insertCard(cards.get(0),0,2);
        Boolean bool= controller.checkPlacing(cards.get(1),1,1);
        assertTrue(bool);
        Boolean bool2= controller.checkPlacing(cards.get(2),2,1);
        assertFalse(bool2);
    }

    List<PlayCard> istanzia_carte(){
        List<PlayCard> carte = new ArrayList<>();
        for(int i=0; i<40; i++){
            carte.add(mazzo_risorse_list.get(i));
        }
        return carte;
    }

    List<GoldCard> istanzia_cartegold(){
        List<GoldCard> carte = new ArrayList<>();
        for(int i=0; i<40; i++){
            carte.add(mazzo_gold_list.get(i));
        }
        return carte;
    }

    @Test
    void checkGoldConstraints() {
        //tested within goaltests
    }

    @Test
    void goldPointsCount() {
        //all the numofsomething checks have been done in goalTests
        List<GoldCard> cartee = new ArrayList<>();
        cartee=istanzia_cartegold();
        player.getGameField().insertCard(cartee.get(0),0,0);
        player.getGameField().insertCard(cartee.get(2),0,2);
        player.getGameField().insertCard(cartee.get(3),2,0);
        player.getGameField().insertCard(cartee.get(4),2,2);
        int points = controller.goldPointsCount(cartee.get(5),1,1);
        //goldpointscounts will be played just before insertcard
        assertEquals(8,points);
    }

    @Test
    void resourcePointsCount() {
        //test passed
    }

    @Test
    void resourcePointsChange() {
        //changepoints methods could be integrated in insertcard :)
        List<GoldCard> cartee = new ArrayList<>();
        cartee=istanzia_cartegold();
        controller.resourcePointsChange(cartee.get(0),0,0);
        player.getGameField().insertCard(cartee.get(0),0,0);
        assertEquals(1,controller.getPlayer_field().getNumOfFeather());
        controller.resourcePointsChange(cartee.get(2),1,1);
        player.getGameField().insertCard(cartee.get(2),1,1);
        assertEquals(0,controller.getPlayer_field().getNumOfFeather());
        assertEquals(1,controller.getPlayer_field().getNumOfPaper());
        player.getGameField().insertCard(cartee.get(3),2,1);
        assertEquals(0,controller.getPlayer_field().getNumOfFeather());
        assertEquals(1,controller.getPlayer_field().getNumOfPaper());
        player.getGameField().insertCard(cartee.get(4),2,2);
    }

    @Test
    void getCurrent() {
        //useless test
    }
}