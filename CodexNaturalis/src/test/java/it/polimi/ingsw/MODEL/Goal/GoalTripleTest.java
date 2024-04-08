package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.Game.DeckCreation;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoalTripleTest {
    DeckCreation creation = new DeckCreation(); //crea il deck
    List<ResourceCard> mazzo_risorse_list = creation.getDeck_resources(); //deck visto come lista

    Deck resource_deck = new Deck(creation.getResourcesDeck()); //creo il deck delle carte risorce come una lista

    public final Side tc_front_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final Side tc_back_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final PlayCard tc = new ResourceCard(tc_front_side, tc_back_side,false, 0);
    GameFieldSingleCell[][] campo = new GameFieldSingleCell[45][45];
    GameField gameField = new GameField(campo);
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
    GoalStrategy triplarisorsa = new GoalTriple();
    Goal goalM = new Goal(triplarisorsa, 4, AnglesEnum.MUSHROOMS);
    Goal goalI = new Goal(triplarisorsa, 4, AnglesEnum.INSECTS);
    Goal goalA = new Goal(triplarisorsa, 4, AnglesEnum.ANIMAL);
    Goal goalP = new Goal(triplarisorsa, 4, AnglesEnum.PLANT);
    @Test
    void totalPoints() {

        istanzia_celle();
        List<PlayCard> carta = istanzia_carte();

        gameField.insertCard(carta.get(5), 0, 0);
        gameField.addOne(carta.get(5).getSide().getCentral_resource());
// System.out.println("Valore di central_resource per carta 5: " + carta.get(5).getSide().getCentral_resource());
        gameField.addOne(carta.get(5).getSide().getAngleLeftUp());
// System.out.println("Valore di angleLeftUp per carta 5: " + carta.get(5).getSide().getAngleLeftUp());
        gameField.addOne(carta.get(5).getSide().getAngleLeftDown());
// System.out.println("Valore di angleLeftDown per carta 5: " + carta.get(5).getSide().getAngleLeftDown());
        gameField.addOne(carta.get(5).getSide().getAngleRightDown());
// System.out.println("Valore di angleRightDown per carta 5: " + carta.get(5).getSide().getAngleRightDown());
        gameField.addOne(carta.get(5).getSide().getAngleRightUp());
// System.out.println("Valore di angleRightUp per carta 5: " + carta.get(5).getSide().getAngleRightUp());

        gameField.insertCard(carta.get(14), 20, 20);
        gameField.addOne(carta.get(14).getSide().getCentral_resource());
// System.out.println("Valore di central_resource per carta 14: " + carta.get(14).getSide().getCentral_resource());
        gameField.addOne(carta.get(14).getSide().getAngleLeftUp());
// System.out.println("Valore di angleLeftUp per carta 14: " + carta.get(14).getSide().getAngleLeftUp());
        gameField.addOne(carta.get(14).getSide().getAngleLeftDown());
// System.out.println("Valore di angleLeftDown per carta 14: " + carta.get(14).getSide().getAngleLeftDown());
        gameField.addOne(carta.get(14).getSide().getAngleRightDown());
// System.out.println("Valore di angleRightDown per carta 14: " + carta.get(14).getSide().getAngleRightDown());
        gameField.addOne(carta.get(14).getSide().getAngleRightUp());
// System.out.println("Valore di angleRightUp per carta 14: " + carta.get(14).getSide().getAngleRightUp());

        gameField.insertCard(carta.get(16), 30, 30);
        gameField.addOne(carta.get(16).getSide().getCentral_resource());
// System.out.println("Valore di central_resource per carta 16: " + carta.get(16).getSide().getCentral_resource());
        gameField.addOne(carta.get(16).getSide().getAngleLeftUp());
// System.out.println("Valore di angleLeftUp per carta 16: " + carta.get(16).getSide().getAngleLeftUp());
        gameField.addOne(carta.get(16).getSide().getAngleLeftDown());
// System.out.println("Valore di angleLeftDown per carta 16: " + carta.get(16).getSide().getAngleLeftDown());
        gameField.addOne(carta.get(16).getSide().getAngleRightDown());
// System.out.println("Valore di angleRightDown per carta 16: " + carta.get(16).getSide().getAngleRightDown());
        gameField.addOne(carta.get(16).getSide().getAngleRightUp());
// System.out.println("Valore di angleRightUp per carta 16: " + carta.get(16).getSide().getAngleRightUp());

        gameField.insertCard(carta.get(0), 5, 5);
        gameField.addOne(carta.get(0).getSide().getCentral_resource());
// System.out.println("Valore di central_resource per carta 0: " + carta.get(0).getSide().getCentral_resource());
        gameField.addOne(carta.get(0).getSide().getAngleLeftUp());
// System.out.println("Valore di angleLeftUp per carta 0: " + carta.get(0).getSide().getAngleLeftUp());
        gameField.addOne(carta.get(0).getSide().getAngleLeftDown());
// System.out.println("Valore di angleLeftDown per carta 0: " + carta.get(0).getSide().getAngleLeftDown());
        gameField.addOne(carta.get(0).getSide().getAngleRightDown());
// System.out.println("Valore di angleRightDown per carta 0: " + carta.get(0).getSide().getAngleRightDown());
        gameField.addOne(carta.get(0).getSide().getAngleRightUp());
// System.out.println("Valore di angleRightUp per carta 0: " + carta.get(0).getSide().getAngleRightUp());

        gameField.insertCard(carta.get(10), 15, 15);
        gameField.addOne(carta.get(10).getSide().getCentral_resource());
// System.out.println("Valore di central_resource per carta 10: " + carta.get(10).getSide().getCentral_resource());
        gameField.addOne(carta.get(10).getSide().getAngleLeftUp());
// System.out.println("Valore di angleLeftUp per carta 10: " + carta.get(10).getSide().getAngleLeftUp());
        gameField.addOne(carta.get(10).getSide().getAngleLeftDown());
// System.out.println("Valore di angleLeftDown per carta 10: " + carta.get(10).getSide().getAngleLeftDown());
        gameField.addOne(carta.get(10).getSide().getAngleRightDown());
// System.out.println("Valore di angleRightDown per carta 10: " + carta.get(10).getSide().getAngleRightDown());
        gameField.addOne(carta.get(10).getSide().getAngleRightUp());
// System.out.println("Valore di angleRightUp per carta 10: " + carta.get(10).getSide().getAngleRightUp());


        int totM = goalM.getGoalType().totalPoints(gameField, 4, AnglesEnum.MUSHROOMS);
        int totP = goalP.getGoalType().totalPoints(gameField, 4, AnglesEnum.PLANT);
        int totA = goalA.getGoalType().totalPoints(gameField, 4, AnglesEnum.ANIMAL);
        int totI = goalI.getGoalType().totalPoints(gameField, 4, AnglesEnum.INSECTS);
        int tot = totM + totP + totA + totI;
        System.out.println("Totale per goalM: " + totM);
        System.out.println("Totale per goalP: " + totP);
        System.out.println("Totale per goalA: " + totA);
        System.out.println("Totale per goalI: " + totI);
        System.out.println("Totale complessivo: " + tot);
        assertEquals(8,tot);
    }
}