package it.polimi.ingsw.MODEL.Goal;

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

class GoalDoubleGoldTest {
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
    GoalStrategy doppia = new GoalDoubleGold();
    Goal goalF = new Goal(doppia, 4, AnglesEnum.FEATHER, "aa");
    Goal goalPE = new Goal(doppia, 4, AnglesEnum.PEN, "aa");
    Goal goalPA = new Goal(doppia, 4, AnglesEnum.PAPER, "aa");
    @Test
    void totalPoints() {
        istanzia_celle();
        List<PlayCard> carta = istanzia_carte();

        // Inserisci le carte e aggiungi le risorse
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
 System.out.println("Valore di central_resource per carta 14: " + carta.get(14).getSide().getCentral_resource());
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


        gameField.insertCard(carta.get(25), 10, 10);
        gameField.addOne(carta.get(25).getSide().getCentral_resource());
// System.out.println("Valore di central_resource per carta 25: " + carta.get(25).getSide().getCentral_resource());
        gameField.addOne(carta.get(25).getSide().getAngleLeftUp());
// System.out.println("Valore di angleLeftUp per carta 25: " + carta.get(25).getSide().getAngleLeftUp());
        gameField.addOne(carta.get(25).getSide().getAngleLeftDown());
// System.out.println("Valore di angleLeftDown per carta 25: " + carta.get(25).getSide().getAngleLeftDown());
        gameField.addOne(carta.get(25).getSide().getAngleRightDown());
// System.out.println("Valore di angleRightDown per carta 25: " + carta.get(25).getSide().getAngleRightDown());
        gameField.addOne(carta.get(25).getSide().getAngleRightUp());
// System.out.println("Valore di angleRightUp per carta 25: " + carta.get(25).getSide().getAngleRightUp());

        gameField.insertCard(carta.get(30), 15, 5);
        gameField.addOne(carta.get(30).getSide().getCentral_resource());
// System.out.println("Valore di central_resource per carta 30: " + carta.get(30).getSide().getCentral_resource());
        gameField.addOne(carta.get(30).getSide().getAngleLeftUp());
// System.out.println("Valore di angleLeftUp per carta 30: " + carta.get(30).getSide().getAngleLeftUp());
        gameField.addOne(carta.get(30).getSide().getAngleLeftDown());
// System.out.println("Valore di angleLeftDown per carta 30: " + carta.get(30).getSide().getAngleLeftDown());
        gameField.addOne(carta.get(30).getSide().getAngleRightDown());
// System.out.println("Valore di angleRightDown per carta 30: " + carta.get(30).getSide().getAngleRightDown());
        gameField.addOne(carta.get(30).getSide().getAngleRightUp());
// System.out.println("Valore di angleRightUp per carta 30: " + carta.get(30).getSide().getAngleRightUp());

        gameField.insertCard(carta.get(35), 25, 25);
        gameField.addOne(carta.get(35).getSide().getCentral_resource());
// System.out.println("Valore di central_resource per carta 35: " + carta.get(35).getSide().getCentral_resource());
        gameField.addOne(carta.get(35).getSide().getAngleLeftUp());
// System.out.println("Valore di angleLeftUp per carta 35: " + carta.get(35).getSide().getAngleLeftUp());
        gameField.addOne(carta.get(35).getSide().getAngleLeftDown());
// System.out.println("Valore di angleLeftDown per carta 35: " + carta.get(35).getSide().getAngleLeftDown());
        gameField.addOne(carta.get(35).getSide().getAngleRightDown());
// System.out.println("Valore di angleRightDown per carta 35: " + carta.get(35).getSide().getAngleRightDown());
        gameField.addOne(carta.get(35).getSide().getAngleRightUp());
// System.out.println("Valore di angleRightUp per carta 35: " + carta.get(35).getSide().getAngleRightUp());




        int totalPointsF = goalF.getGoalType().totalPoints(gameField, 4, AnglesEnum.FEATHER);
        int totalPointsPE = goalF.getGoalType().totalPoints(gameField, 4, AnglesEnum.PEN);
        int totalPointsPA = goalF.getGoalType().totalPoints(gameField, 4, AnglesEnum.PAPER);
        int totalPoints = totalPointsF+totalPointsPA+totalPointsPE;

//        System.out.println("Total points for FEATHER: " + totalPointsF);
//        System.out.println("Total points for PEN: " + totalPointsPE);
//        System.out.println("Total points for PAPER: " + totalPointsPA);
//        System.out.println("Total points: " + totalPoints);
//          Verifica che i punti totali siano corretti
        assertEquals(4,totalPoints);
    }
}
