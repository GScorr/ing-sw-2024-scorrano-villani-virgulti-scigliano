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

import static org.junit.Assert.assertEquals;
/*

istanziazione uguale a tutti i test
 */

class GoalLFigureTest {
    DeckCreation creation = new DeckCreation(); //crea il deck
    List<ResourceCard> mazzo_risorse_list = creation.getDeck_resources(); //deck visto come lista

    Deck resource_deck = new Deck(creation.getResourcesDeck()); //creo il deck delle carte risorce come una lista

    public final Side tc_front_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final Side tc_back_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    public final PlayCard tc = new ResourceCard(tc_front_side, tc_back_side,false, 0);
/*
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

 */

    GameFieldSingleCell[][] campo = new GameFieldSingleCell[45][45];
    Player p1=new Player(ColorsEnum.BLU, "nomeProva", false);
    GameField gameField = new GameField(campo, p1);
    GoalStrategy figura_strana = new GoalLFigure();
    Goal goal = new Goal(figura_strana, 4, AnglesEnum.MUSHROOMS, "a");



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
    /*
    @Test
    void totalPoints(){
        /*
        creo la matrice 45x45 e tutte le carte
         */
    /*
        Constants.MATRIXDIM=45;
        istanzia_celle();
        List<PlayCard> carta = istanzia_carte();
        */

        /*
        assegno 4 punti per ogni carta
         */
    /*
        gameField.insertCard(carta.get(10), 43,43); pianta
        gameField.insertCard(carta.get(0), 42,42); mush
        gameField.insertCard(carta.get(1), 40,42); mush
        gameField.insertCard(carta.get(11), 41,43); pianta
        gameField.insertCard(carta.get(2), 38,42); mush tot=8

        gameField.insertCard(carta.get(20), 43,0); animal
        gameField.insertCard(carta.get(21), 42,1); animal
        gameField.insertCard(carta.get(22), 41,2); animal
        gameField.insertCard(carta.get(23), 40,3); animal tot=8

        gameField.insertCard(carta.get(10), 43,43);

    }
    */

    @Test
    void totalPointsAnimal() {
//così riesce ad andare perche non va a fare controlli su tutta da matrice da 45 elementi che sarebberro tutti null
      //  inizializzo tutte le celle del campo
/*
        PlayCard carta0 = mazzo_risorse_list.get(20); prendo la carta in posizione indiex per le prove
        PlayCard carta1 = mazzo_risorse_list.get(21);
        PlayCard carta2 = mazzo_risorse_list.get(22);
        PlayCard carta3 = mazzo_risorse_list.get(23);
        PlayCard carta4 = mazzo_risorse_list.get(0);

 */
        istanzia_celle();
        List<PlayCard> carta = istanzia_carte();

        gameField.insertCard(carta.get(20), 1, 0);
        System.err.println("carta 0 pos 1,0 colore: "+gameField.getCell(1, 0, 5).getCard().getColore());
        gameField.insertCard(carta.get(21), 3, 0);
        System.err.println("carta 1 pos 3,0 colore: "+gameField.getCell(3, 0, 5).getCard().getColore());
        //gameField.insertCard(carta2, 1, 2);
        gameField.insertCard(carta.get(0), 0, 1);
        System.err.println("carta 1 pos 0,1 colore: "+gameField.getCell(0, 1, 5).getCard().getColore());

        int tot = goal.getGoalType().totalPoints(gameField, 4, AnglesEnum.ANIMAL);
        System.err.println("risultato dell'operazione = "+tot);
        assertEquals(4, tot);
    }

    @Test
    void totalPointsMushroom() {
 //così riesce ad andare perche non va a fare controlli su tutta da matrice da 45 elementi che sarebberro tutti null
   //     inizializzo tutte le celle del campo
  /*      campo[0][0] = cell1;
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

   */   istanzia_celle();

        PlayCard carta0 = mazzo_risorse_list.get(20); //prendo la carta in posizione indiex per le prove
        PlayCard carta1 = mazzo_risorse_list.get(21);
        PlayCard carta2 = mazzo_risorse_list.get(10);
        PlayCard carta3 = mazzo_risorse_list.get(1);
        PlayCard carta4 = mazzo_risorse_list.get(0);

        gameField.insertCard(carta3, 0, 0);
        System.err.println("carta 0 pos 1,0 colore: "+gameField.getCell(1, 0, 5).getCard().getColore());
        gameField.insertCard(carta4, 2, 0);
        System.err.println("carta 1 pos 3,0 colore: "+gameField.getCell(3, 0, 5).getCard().getColore());
        gameField.insertCard(carta2, 1, 2);
        gameField.insertCard(carta2, 3, 1);
        System.err.println("carta 1 pos 0,1 colore: "+gameField.getCell(0, 1, 5).getCard().getColore());

        int tot = goal.getGoalType().totalPoints(gameField, 4, AnglesEnum.MUSHROOMS);
        System.err.println("risultato dell'operazione = "+tot);
        assertEquals(4, tot);
    }

    @Test
    void totalPointsPlant() {
 //così riesce ad andare perche non va a fare controlli su tutta da matrice da 45 elementi che sarebberro tutti null
        //inizializzo tutte le celle del campo
    /*    campo[0][0] = cell1;
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

     */
        istanzia_celle();
        PlayCard carta0 = mazzo_risorse_list.get(20); //prendo la carta in posizione indiex per le prove
        PlayCard carta1 = mazzo_risorse_list.get(21);
        PlayCard carta2 = mazzo_risorse_list.get(10);
        PlayCard carta3 = mazzo_risorse_list.get(11);
        PlayCard carta4 = mazzo_risorse_list.get(30);

        gameField.insertCard(carta3, 0, 1);
        System.err.println("carta 0 pos 1,0 colore: "+gameField.getCell(1, 0, 5).getCard().getColore());
        gameField.insertCard(carta2, 2, 1);
        System.err.println("carta 1 pos 3,0 colore: "+gameField.getCell(3, 0, 5).getCard().getColore());
        gameField.insertCard(carta2, 1, 2);
        gameField.insertCard(carta4, 3, 0);
        System.err.println("carta 1 pos 0,1 colore: "+gameField.getCell(0, 1, 5).getCard().getColore());

        int tot = goal.getGoalType().totalPoints(gameField, 4, AnglesEnum.PLANT);
        System.err.println("risultato dell'operazione = "+tot);
        assertEquals(4, tot);
    }

    @Test
    void totalPointsInsect() {
 //così riesce ad andare perche non va a fare controlli su tutta da matrice da 45 elementi che sarebberro tutti null
        //inizializzo tutte le celle del campo
    /*    campo[0][0] = cell1;
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

     */
        istanzia_celle();

        PlayCard carta0 = mazzo_risorse_list.get(20); //prendo la carta in posizione indiex per le prove
        PlayCard carta1 = mazzo_risorse_list.get(21);
        PlayCard carta2 = mazzo_risorse_list.get(10);
        PlayCard carta3 = mazzo_risorse_list.get(31);
        PlayCard carta4 = mazzo_risorse_list.get(30);

        gameField.insertCard(carta3, 3, 1);
        System.err.println("carta 0 pos 1,0 colore: "+gameField.getCell(1, 0, 5).getCard().getColore());
        gameField.insertCard(carta0, 0, 0);
        System.err.println("carta 1 pos 3,0 colore: "+gameField.getCell(3, 0, 5).getCard().getColore());
        gameField.insertCard(carta2, 1, 2);
        gameField.insertCard(carta4, 1, 1);
        System.err.println("carta 1 pos 0,1 colore: "+gameField.getCell(0, 1, 5).getCard().getColore());

        int tot = goal.getGoalType().totalPoints(gameField, 4, AnglesEnum.INSECTS);
        System.err.println("risultato dell'operazione = "+tot);
        assertEquals(4, tot);
    }
}

