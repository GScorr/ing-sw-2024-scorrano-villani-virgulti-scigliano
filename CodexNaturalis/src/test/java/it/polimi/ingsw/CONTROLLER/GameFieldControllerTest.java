package it.polimi.ingsw.CONTROLLER;

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

    @Test
    void checkPlacing() {
        cards = istanzia_carte();
        player.getGameField().insertCard(cards.get(1),0,0);
        Boolean bool= controller.checkPlacing(cards.get(0),1,1);
        assertTrue(bool);
        Boolean bool2= controller.checkPlacing(cards.get(2),0,2);
        assertTrue(bool2);
    }

    List<PlayCard> istanzia_carte(){
        List<PlayCard> carte = new ArrayList<>();
        for(int i=0; i<40; i++){
            carte.add(mazzo_risorse_list.get(i));
        }
        return carte;
    }

    @Test
    void checkGoldConstraints() {
    }

    @Test
    void goldPointsCount() {
    }

    @Test
    void resourcePointsCount() {
    }

    @Test
    void resourcePointsChange() {
    }

    @Test
    void getCurrent() {
    }
}