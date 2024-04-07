package it.polimi.ingsw.MODEL.Goal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.MODEL.Card.*;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.Game.DeckCreation;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;
import org.junit.jupiter.api.Test;
import com.google.gson.*;


import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;

class GoalDiagonalTest {

    DeckCreation creation = new DeckCreation();
    List<ResourceCard> mazzo_risorse_list = creation.getDeck_resources();

    PlayCard carta = mazzo_risorse_list.get(1); //prendo la carta in posizione indiex per le prove
    Deck resource_deck = new Deck(creation.getResourcesDeck()); //creo il deck delle carte risorce come una lista



/*    GameFieldSingleCell gameFieldSingleCell1 = new GameFieldSingleCell(true, card1, AnglesEnum.MUSHROOMS, card2);
    GameFieldSingleCell gameFieldSingleCell2 = new GameFieldSingleCell(true, card1, AnglesEnum.MUSHROOMS, card2);
    GameFieldSingleCell gameFieldSingleCell3 = new GameFieldSingleCell(false, card1, AnglesEnum.MUSHROOMS, card2);
    GameFieldSingleCell gameFieldSingleCell4 = new GameFieldSingleCell(false, card1, AnglesEnum.MUSHROOMS, card2);
    GameFieldSingleCell gameFieldSingleCell5 = new GameFieldSingleCell(true, card1, AnglesEnum.MUSHROOMS, card2);
    GameFieldSingleCell gameFieldSingleCell6 = new GameFieldSingleCell(true, card1, AnglesEnum.MUSHROOMS, card2);
*/
   // GameFieldSingleCell[][] campo = new GameFieldSingleCell[4][4];

  //  GameField gameField = new GameField(campo);

    @Test
    void totalPoints() {




        //gameField.insertCard(card1, 0, 0); //inserisco la carta nel gamefield



        GoalDiagonal goalDiagonal = new GoalDiagonal();

        //int i = goalDiagonal.totalPoints(gameField, 4, AnglesEnum.MUSHROOMS);
        //System.err.println("risultato dell'operazione = "+i);
/*
non va perche la dimensione deve essere di 44 ora devo capire come istanziare tutto
 */


    }
}
