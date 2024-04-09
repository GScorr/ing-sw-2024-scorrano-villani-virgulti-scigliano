package it.polimi.ingsw.CONTROLLER;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.Game.DeckCreation;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldControllerTest {

    DeckCreation creation = new DeckCreation();
    List<ResourceCard> mazzo_risorse_list = creation.getDeck_resources();
    public final Side tc_front_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final Side tc_back_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final PlayCard tc = new ResourceCard(tc_front_side, tc_back_side,false, 0);
    GameFieldController controller = new GameFieldController();
    @Test
    void checkPlacing() {
        istanzia_celle(controller.getField_cell());
        List<PlayCard> carte = istanzia_carte();
        controller.getPlayer_field().insertCard(carte.get(1),0,0);
        System.out.println(controller.getPlayer_field().getField()[1][1].getValue());
        Boolean bool = controller.checkPlacing(carte.get(0),1,1);
        assertTrue(bool);
    }
    void istanzia_celle(GameFieldSingleCell[][] campo){
        for (int i = 0; i < 45; i++) {
            for (int j = 0; j < 45; j++) {
                campo[i][j] = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
            }
        }
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