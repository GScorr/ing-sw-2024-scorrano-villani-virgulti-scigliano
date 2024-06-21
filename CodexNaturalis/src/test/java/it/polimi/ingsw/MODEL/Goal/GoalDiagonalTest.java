package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.*;
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

import static org.junit.Assert.assertEquals;

/*
todo
    probabilmente giocando le partita intera testa tutto il codice
 */

class GoalDiagonalTest {


    DeckCreation creation = new DeckCreation(); //crea il deck
    List<ResourceCard> mazzo_risorse_list = creation.getDeck_resources(); //deck visto come lista

    Player p1=new Player(ColorsEnum.BLU, "nomeProva", false);

    Deck resource_deck = new Deck(creation.getResourcesDeck()); // creo il deck delle carte risorce come una lista

    public final Side tc_front_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final Side tc_back_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final PlayCard tc = new ResourceCard(tc_front_side, tc_back_side,false, 0);

    GameFieldSingleCell[][] campo = new GameFieldSingleCell[45][45];
    GameField gameField = new GameField(campo, p1);

    void istanzia_celle(){
        for (int i = 0; i < 45; i++) {
            for (int j = 0; j < 45; j++) {
                campo[i][j] = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
            }
        }
    }
    public List<PlayCard> carte = new ArrayList<>();
    List<PlayCard> istanzia_carte(){

        for(int i=0; i<40; i++){
            carte.add(mazzo_risorse_list.get(i));
        }
        return carte;
    }

    GoalStrategy colore_diagonale = new GoalDiagonal();
    Goal goal = new Goal(colore_diagonale, 4, AnglesEnum.MUSHROOMS, "aa");

    @Test
    void totalPoints() {

        istanzia_celle();
        List<PlayCard> carta = istanzia_carte();

//così riesce ad andare perche non va a fare controlli su tutta da matrice da 45 elementi che sarebberro tutti null
        //inizializzo tutte le celle del campo

        /*
        stampa di prova

        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                System.out.println("stampo la cella "+i+" "+j+" = "+campo[i][j].getValue());
            }
        }
        */
        gameField.insertCard(resource_deck.drawCard(), 0, 3);
        //System.out.println(gameField.getCell(0,0,Constants.MATRIXDIM).getCard().getColore()+"sono la carta su");
        //System.out.println(gameField.getCell(0,0,Constants.MATRIXDIM).getCardDown().getColore()+"sono la carta giù");
        gameField.insertCard(resource_deck.drawCard(), 1, 2);
        gameField.insertCard(resource_deck.drawCard(), 2, 1);
        gameField.insertCard(resource_deck.drawCard(), 3, 0);


        System.err.println("carta 1 pos 1,1 colore: "+gameField.getCell(1, 1, 4).getCard().getColore());
/*
        System.err.println(gameField.getCell(1, 1, 4).getCard().getSide().getAngleLeftUp());
        System.err.println(gameField.getCell(1, 1, 4).getCard().getSide().getAngleRightUp());
        System.err.println(gameField.getCell(1, 1, 4).getCard().getSide().getAngleLeftDown());
        System.err.println(gameField.getCell(1, 1, 4).getCard().getSide().getAngleRightDown());
*/
        System.err.println("carta 2 pos 2,2 colore: "+gameField.getCell(2, 2, 4).getCard().getColore());
/*
        System.err.println(gameField.getCell(2, 2, 4).getCard().getSide().getAngleLeftUp());
        System.err.println(gameField.getCell(2, 2, 4).getCard().getSide().getAngleRightUp());
        System.err.println(gameField.getCell(2, 2, 4).getCard().getSide().getAngleLeftDown());
        System.err.println(gameField.getCell(2, 2, 4).getCard().getSide().getAngleRightDown());
*/
        System.err.println("carta 3 pos 2,0 colore: "+gameField.getCell(2, 0, 4).getCard().getColore());
/*
        System.err.println(gameField.getCell(2, 0, 4).getCard().getSide().getAngleLeftUp());
        System.err.println(gameField.getCell(2, 0, 4).getCard().getSide().getAngleRightUp());
        System.err.println(gameField.getCell(2, 0, 4).getCard().getSide().getAngleLeftDown());
        System.err.println(gameField.getCell(2, 0, 4).getCard().getSide().getAngleRightDown());
        System.err.println(gameField.getCell(2, 0, 4).isEmpty());
        System.err.println(gameField.getCell(2, 0, 4).isFilled());
*/
        System.err.println("carta 4 pos: 0,2 colore: "+gameField.getCell(0, 2, 4).getCard().getColore());
       /* if(gameField.getCell(0,2, 4).getCard().getColore().equals(CentralEnum.MUSHROOMS)){
            System.out.println("colore giusto");
        }*/
 /*       System.err.println(gameField.getCell(0, 2, 4).isEmpty()); return false -> posizione non vuota probabile bug in insierimento carta
        System.err.println(gameField.getCell(0, 2, 4).getCard().getSide().getAngleLeftUp());
        System.err.println(gameField.getCell(0, 2, 4).getCard().getSide().getAngleRightUp());
        System.err.println(gameField.getCell(0, 2, 4).getCard().getSide().getAngleLeftDown());
        System.err.println(gameField.getCell(0, 2, 4).getCard().getSide().getAngleRightDown());
*/
        GoalDiagonal goalDiagonal = new GoalDiagonal();

        Goal g = new Goal(goalDiagonal, 4,AnglesEnum.MUSHROOMS, "aa");
        int tot = g.numPoints(gameField);

        tot = goal.getGoalType().totalPoints(gameField, 4, AnglesEnum.MUSHROOMS);
        System.err.println("risultato dell'operazione = "+tot);
        //assertEquals(8, tot); //non posso usare questo assert perche il
         //punteggio può cambiare essendo che inserisco sempre delle carte a caso

        gameField.insertCard(carte.get(11), 0, 1);
        gameField.insertCard(carta.get(12), 1, 2);
        gameField.insertCard(carta.get(13), 2, 3);

        tot = goal.getGoalType().totalPoints(gameField, 4, AnglesEnum.PLANT);
        System.err.println("risultato dell'operazione = "+tot);
        //assertEquals(8, tot);

        gameField.insertCard(carte.get(21), 0, 3);
        gameField.insertCard(carta.get(22), 1, 2);
        gameField.insertCard(carta.get(23), 2, 1);

        tot = goal.getGoalType().totalPoints(gameField, 4, AnglesEnum.ANIMAL);
        System.err.println("risultato dell'operazione = "+tot);
        //assertEquals(8, tot);

        gameField.insertCard(carte.get(31), 0, 1);
        gameField.insertCard(carta.get(32), 1, 2);
        gameField.insertCard(carta.get(33), 2, 3);

        tot = goal.getGoalType().totalPoints(gameField, 4, AnglesEnum.INSECTS);
        System.err.println("risultato dell'operazione = "+tot);
        assertEquals(8, tot);


    }
}

