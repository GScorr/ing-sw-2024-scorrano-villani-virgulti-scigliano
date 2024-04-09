package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.*;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.Game.DeckCreation;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.Assert.assertEquals;

class GoalDiagonalTest {

    DeckCreation creation = new DeckCreation(); //crea il deck
    List<ResourceCard> mazzo_risorse_list = creation.getDeck_resources(); //deck visto come lista

    PlayCard carta0 = mazzo_risorse_list.get(0); //prendo la carta in posizione indiex per le prove
    PlayCard carta1 = mazzo_risorse_list.get(1);
    PlayCard carta2 = mazzo_risorse_list.get(2);
    PlayCard carta3 = mazzo_risorse_list.get(3);
    PlayCard carta4 = mazzo_risorse_list.get(4);
    Deck resource_deck = new Deck(creation.getResourcesDeck()); //creo il deck delle carte risorce come una lista
    /*
    questa è la carta trasparente, potremmo anche pensare di metterla in un json a parte
     */
    public final Side tc_front_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final Side tc_back_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final PlayCard tc = new ResourceCard(tc_front_side, tc_back_side,false, 0);


/*
inizializzo tutte le celle del gamefield con carte trasparenti sia come carta up che come card_down
poi nel metodo totalPoint le riempio usando insert card <- penso che lo devo fare in ogni metodo in cui uso le carte, istanziandole ogni volta
 */
    GameFieldSingleCell cell1 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell2 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell3 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell4 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell5 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell6 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell7 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell8 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell9 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell10 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell11 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell12 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell13 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell14 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell15 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell16 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell17 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell18 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell19 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell20 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell21 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell22 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell23 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell24 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
    GameFieldSingleCell cell25 = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);

    GameFieldSingleCell[][] campo = new GameFieldSingleCell[5][5];
    GameField gameField = new GameField(campo);
    GoalStrategy colore_diagonale = new GoalDiagonal();
    Goal goal = new Goal(colore_diagonale, 4, AnglesEnum.MUSHROOMS);

    @Test
    void totalPoints() {
//così riesce ad andare perche non va a fare controlli su tutta da matrice da 45 elementi che sarebberro tutti null
        //inizializzo tutte le celle del campo
        campo[0][0] = cell1;
        campo[0][1] = cell2;
        campo[0][2] = cell3;
        campo[0][3] = cell4;
        campo[0][4] = cell5;
        campo[1][0] = cell6;
        campo[1][1] = cell7;
        campo[1][2] = cell8;
        campo[1][3] = cell9;
        campo[1][4] = cell10;
        campo[2][0] = cell11;
        campo[2][1] = cell12;
        campo[2][2] = cell13;
        campo[2][3] = cell14;
        campo[2][4] = cell15;
        campo[3][0] = cell16;
        campo[3][1] = cell17;
        campo[3][2] = cell18;
        campo[3][3] = cell19;
        campo[3][4] = cell20;
        campo[4][0] = cell21;
        campo[4][1] = cell22;
        campo[4][2] = cell23;
        campo[4][3] = cell24;
        campo[4][4] = cell25;
        /*
        stampa di prova

        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                System.out.println("stampo la cella "+i+" "+j+" = "+campo[i][j].getValue());
            }
        }
        */
        gameField.insertCard(carta0, 0, 3);
        //System.out.println(gameField.getCell(0,0,Constants.MATRIXDIM).getCard().getColore()+"sono la carta su");
        //System.out.println(gameField.getCell(0,0,Constants.MATRIXDIM).getCardDown().getColore()+"sono la carta giù");
        gameField.insertCard(carta1, 1, 2);
        gameField.insertCard(carta2, 2, 1);
        gameField.insertCard(carta3, 3, 0);
        //gameField.insertCard(carta4, 0, 0);
       /* if(gameField.insertCard(carta0, 0, 0)){
            //System.err.println("0 true = ha modificato la carta trasparente");
        } //inserisco la carta nel gamefield
        if(gameField.insertCard(carta1, 1, 1)){
           // System.err.println("1 true = ha modificato la carta trasparente");
        }
        if(gameField.insertCard(carta2, 2, 2)){
            //System.err.println("2 true = ha modificato la carta trasparente");
        }
        //inserisco altre due carte per il controllo lungo l'altra diagonale tenendo tutte le carte insieme
        if(gameField.insertCard(carta3, 2, 0)){
            //System.err.println("3 true = ha modificato la carta trasparente");
        }
        */

        /*
        stampa di prova

        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                System.out.println("stampo la cella "+i+" "+j+" = "+campo[i][j].getValue());
            }
        }
        if(gameField.insertCard(carta4, 0, 2)){
            //System.err.println("4 true = ha modificato la carta trasparente");
        }
        /*
        stampa di prova

        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                System.out.println("stampo la cella "+i+" "+j+" = "+campo[i][j].getValue());
            }
        }*/


        //stampo tutte le carte nel campo
        System.err.println("carta 0 pos 0,0, colore: "+gameField.getCell(0, 0, 4).getCard().getColore());

    /*    System.err.println(gameField.getCell(0, 0, 4).getCard().getSide().getAngleLeftUp());
        System.err.println(gameField.getCell(0, 0, 4).getCard().getSide().getAngleRightUp());
        System.err.println(gameField.getCell(0, 0, 4).getCard().getSide().getAngleLeftDown());
        System.err.println(gameField.getCell(0, 0, 4).getCard().getSide().getAngleRightDown());
*/
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
        //System.err.println(gameField.getCell(2, 0, 4).isEmpty());
        //System.err.println(gameField.getCell(2, 0, 4).isFilled());
*/
       // System.err.println("carta 4 pos: 0,2 colore: "+gameField.getCell(0, 2, 4).getCard().getColore());
       /* if(gameField.getCell(0,2, 4).getCard().getColore().equals(CentralEnum.MUSHROOMS)){
            System.out.println("colore giusto");
        }*/
 /*       //System.err.println(gameField.getCell(0, 2, 4).isEmpty()); //return false -> posizione non vuota probabile bug in insierimento carta
        System.err.println(gameField.getCell(0, 2, 4).getCard().getSide().getAngleLeftUp());
        System.err.println(gameField.getCell(0, 2, 4).getCard().getSide().getAngleRightUp());
        System.err.println(gameField.getCell(0, 2, 4).getCard().getSide().getAngleLeftDown());
        System.err.println(gameField.getCell(0, 2, 4).getCard().getSide().getAngleRightDown());
*/
        //GoalDiagonal goalDiagonal = new GoalDiagonal();

        //Goal g = new Goal(goalDiagonal, 4,AnglesEnum.MUSHROOMS);
        //int tot = g.numPoints(gameField);

        int tot = goal.getGoalType().totalPoints(gameField, 4, AnglesEnum.MUSHROOMS);
        System.err.println("risultato dell'operazione = "+tot);
        assertEquals(4, tot);



    }
}
