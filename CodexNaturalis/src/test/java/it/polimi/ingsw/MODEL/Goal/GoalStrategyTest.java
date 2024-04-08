package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.CONSTANTS.Constants;
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

class GoalStrategyTest {

    DeckCreation creation = new DeckCreation(); //crea il deck
    List<ResourceCard> mazzo_risorse_list = creation.getDeck_resources(); //deck visto come lista

    Deck resource_deck = new Deck(creation.getResourcesDeck()); //creo il deck delle carte risorce come una lista

    public final Side tc_front_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final Side tc_back_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final PlayCard tc = new ResourceCard(tc_front_side, tc_back_side,false, 0);

    GameFieldSingleCell[][] campo = new GameFieldSingleCell[45][45];
    GameField gameField = new GameField(campo);

    GoalStrategy FiguraLA = new GoalLFigure();
    GoalStrategy FiguraLM = new GoalLFigure();
    GoalStrategy FiguraLP = new GoalLFigure();
    GoalStrategy FiguraLI = new GoalLFigure();
    Goal goalA = new Goal(FiguraLA, 4, AnglesEnum.ANIMAL);
    Goal goalM = new Goal(FiguraLM, 4, AnglesEnum.MUSHROOMS);
    Goal goalP = new Goal(FiguraLP, 4, AnglesEnum.PLANT);
    Goal goalI = new Goal(FiguraLI, 4, AnglesEnum.INSECTS);

    GoalStrategy diagonaleA = new GoalDiagonal();
    GoalStrategy diagonaleM = new GoalDiagonal();
    GoalStrategy diagonaleP = new GoalDiagonal();
    GoalStrategy diagonaleI = new GoalDiagonal();
    Goal goalDiagonaleA = new Goal(diagonaleA, 4, AnglesEnum.ANIMAL);
    Goal goalDiagonaleM = new Goal(diagonaleM, 4, AnglesEnum.MUSHROOMS);
    Goal goalDiagonaleP = new Goal(diagonaleP, 4, AnglesEnum.PLANT);
    Goal goalDiagonaleI = new Goal(diagonaleI, 4, AnglesEnum.INSECTS);

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

    @Test
    void totalPoints(){
        /*
        creo la matrice 45x45 e tutte le carte
         */
        istanzia_celle();
        List<PlayCard> carta = istanzia_carte();
        /*
        assegno 4 punti per ogni carta
         */
        gameField.insertCard(carta.get(10), 43,43); //pianta
        gameField.insertCard(carta.get(0), 42,42); //mush
        gameField.insertCard(carta.get(1), 40,42); //mush
        gameField.insertCard(carta.get(11), 41,43); //pianta
        gameField.insertCard(carta.get(2), 38,42); //mush
        gameField.insertCard(carta.get(24), 39,41); //animal
        gameField.insertCard(carta.get(25), 41,41); //animal tot=12


        gameField.insertCard(carta.get(20), 43,0); //animal
        gameField.insertCard(carta.get(21), 42,1); //animal
        gameField.insertCard(carta.get(22), 41,2); //animal
        gameField.insertCard(carta.get(23), 40,3); //animal tot=8
        //System.out.println(goalDiagonaleM.getResource());
        int tot = goalA.getGoalType().totalPoints(gameField, 4, goalA.getResource()) +
                goalM.getGoalType().totalPoints(gameField, 4, goalM.getResource()) +
                goalP.getGoalType().totalPoints(gameField, 4, goalP.getResource()) +
                goalI.getGoalType().totalPoints(gameField, 4, goalI.getResource()) +
                goalDiagonaleA.getGoalType().totalPoints(gameField, 4, goalDiagonaleA.getResource()) +
                goalDiagonaleM.getGoalType().totalPoints(gameField, 4, goalDiagonaleM.getResource()) +
                goalDiagonaleP.getGoalType().totalPoints(gameField, 4, goalDiagonaleP.getResource()) +
                goalDiagonaleI.getGoalType().totalPoints(gameField, 4, goalDiagonaleI.getResource())
                ;
        System.err.println("risultato = "+tot);
        assertEquals(20, tot);
    }

}