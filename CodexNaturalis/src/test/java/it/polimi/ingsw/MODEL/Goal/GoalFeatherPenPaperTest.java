package it.polimi.ingsw.MODEL.Goal;

 import it.polimi.ingsw.CONSTANTS.Constants;
 import it.polimi.ingsw.MODEL.Card.PlayCard;
 import it.polimi.ingsw.MODEL.Card.ResourceCard;
 import it.polimi.ingsw.MODEL.Card.Side;
 import it.polimi.ingsw.MODEL.DeckPackage.Deck;
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

class GoalFeatherPenPaperTest {
    DeckCreation creation = new DeckCreation(); //crea il deck
    List<ResourceCard> mazzo_risorse_list = creation.getDeck_resources(); //deck visto come lista

    Deck resource_deck = new Deck(creation.getResourcesDeck()); //creo il deck delle carte risorce come una lista

    public final Side tc_front_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final Side tc_back_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final PlayCard tc = new ResourceCard(tc_front_side, tc_back_side,false, 0);
    GameFieldSingleCell[][] campo = new GameFieldSingleCell[45][45];
    Player p1=new Player(ColorsEnum.BLU, "nomeProva", false);
    GameField gameField = new GameField(campo, p1);
    void istanzia_celle(){
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
    GoalStrategy triplarisorsa = new GoalFeatherPenPaper();
    Goal goal = new Goal(triplarisorsa, 4, AnglesEnum.MIX, "a");
    @Test
    void totalPoints() {

        istanzia_celle();
        List<PlayCard> carta = istanzia_carte();

        gameField.insertCard(carta.get(5), 0,0); //pen
        gameField.addOne( carta.get(5).getSide().getCentral_resource() );
        gameField.addOne( carta.get(5).getSide().getAngleLeftUp() );
        gameField.addOne( carta.get(5).getSide().getAngleLeftDown() );
        gameField.addOne( carta.get(5).getSide().getAngleRightDown() );
        gameField.addOne( carta.get(5).getSide().getAngleRightUp() );
        gameField.insertCard(carta.get(14), 20,20);
        gameField.addOne(carta.get(14).getSide().getCentral_resource());
        gameField.addOne(carta.get(14).getSide().getAngleLeftUp());
        gameField.addOne(carta.get(14).getSide().getAngleLeftDown());
        gameField.addOne(carta.get(14).getSide().getAngleRightDown());
        gameField.addOne(carta.get(14).getSide().getAngleRightUp());
        gameField.insertCard(carta.get(16), 30,30);
        gameField.addOne(carta.get(16).getSide().getCentral_resource());
        gameField.addOne(carta.get(16).getSide().getAngleLeftUp());
        gameField.addOne(carta.get(16).getSide().getAngleLeftDown());
        gameField.addOne(carta.get(16).getSide().getAngleRightDown());
        gameField.addOne(carta.get(16).getSide().getAngleRightUp());
        int tot=goal.getGoalType().totalPoints(gameField,4,AnglesEnum.MIX);
        System.out.println("riosultato è "+ tot);
        assertEquals(4,tot);
    }
}