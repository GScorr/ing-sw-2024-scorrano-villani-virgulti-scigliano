package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;
import org.junit.jupiter.api.Test;

//import org.json

import static org.junit.jupiter.api.Assertions.*;

class GoalDiagonalTest {
    Side front = new Side(AnglesEnum.MUSHROOMS, AnglesEnum.MUSHROOMS, AnglesEnum.MUSHROOMS, AnglesEnum.MUSHROOMS, CentralEnum.ANIMAL, CentralEnum.ANIMAL, CentralEnum.ANIMAL);
    Side back = new Side(AnglesEnum.MUSHROOMS, AnglesEnum.MUSHROOMS, AnglesEnum.MUSHROOMS, AnglesEnum.MUSHROOMS, CentralEnum.ANIMAL, CentralEnum.ANIMAL, CentralEnum.ANIMAL);
    PlayCard card1 = new ResourceCard(front, back, false, 1); //usando le carte direttamente evito
    PlayCard card2 = new ResourceCard(front, back, false, 1);
    //prova[0] -> indica tutta carta devo comunque istanziarla manualmente tutta

    //provare ad istanziare tutto il deck

    PlayCard[] cards = new PlayCard[4];

    public void istanziaCarte(){
        for(int i=0; i<4; i++){
            cards[i] = new ResourceCard();
        }
    }




/*    GameFieldSingleCell gameFieldSingleCell1 = new GameFieldSingleCell(true, card1, AnglesEnum.MUSHROOMS, card2);
    GameFieldSingleCell gameFieldSingleCell2 = new GameFieldSingleCell(true, card1, AnglesEnum.MUSHROOMS, card2);
    GameFieldSingleCell gameFieldSingleCell3 = new GameFieldSingleCell(false, card1, AnglesEnum.MUSHROOMS, card2);
    GameFieldSingleCell gameFieldSingleCell4 = new GameFieldSingleCell(false, card1, AnglesEnum.MUSHROOMS, card2);
    GameFieldSingleCell gameFieldSingleCell5 = new GameFieldSingleCell(true, card1, AnglesEnum.MUSHROOMS, card2);
    GameFieldSingleCell gameFieldSingleCell6 = new GameFieldSingleCell(true, card1, AnglesEnum.MUSHROOMS, card2);
*/
    GameFieldSingleCell[][] campo = new GameFieldSingleCell[4][4];

    GameField gameField = new GameField(campo);

    @Test
    void totalPoints() {

        gameField.insertCard(card1, 0, 0); //inserisco la carta nel gamefield



        GoalDiagonal goalDiagonal = new GoalDiagonal();

        int i = goalDiagonal.totalPoints(gameField, 4, AnglesEnum.MUSHROOMS);
        System.err.println("risultato dell'operazione = "+i);
/*
non va perche la dimensione deve essere di 44 ora devo capire come istanziare tutto
 */


    }
}
