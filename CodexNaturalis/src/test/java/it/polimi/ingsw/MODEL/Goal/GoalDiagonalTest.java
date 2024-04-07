package it.polimi.ingsw.MODEL.Goal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.MODEL.Card.*;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoalDiagonalTest {

    private String resources_filePath = "src/main/java/it/polimi/ingsw/MODEL/Game/INITIALIZED/resources.json";
    private String gold_filePath = "src/main/java/it/polimi/ingsw/MODEL/Game/INITIALIZED/gold.json";
    private String starting_filePath = "src/main/java/it/polimi/ingsw/MODEL/Game/INITIALIZED/starting.json";

    private JsonArray resources_jsonArray, gold_jsonArray,starting_jsonArray; //cosa fanno questi attributi?
    public static List<ResourceCard> deck_resources = new ArrayList<>();
    public static List<GoldCard> deck_gold = new ArrayList<>();
    public static List<StartingCard> deck_starting = new ArrayList<>();

  //  Side front = new Side(AnglesEnum.MUSHROOMS, AnglesEnum.MUSHROOMS, AnglesEnum.MUSHROOMS, AnglesEnum.MUSHROOMS, CentralEnum.ANIMAL, CentralEnum.ANIMAL, CentralEnum.ANIMAL);
   // Side back = new Side(AnglesEnum.MUSHROOMS, AnglesEnum.MUSHROOMS, AnglesEnum.MUSHROOMS, AnglesEnum.MUSHROOMS, CentralEnum.ANIMAL, CentralEnum.ANIMAL, CentralEnum.ANIMAL);
   // PlayCard card1 = new ResourceCard(front, back, false, 1); //usando le carte direttamente evito
    //PlayCard card2 = new ResourceCard(front, back, false, 1);
    //prova[0] -> indica tutta carta devo comunque istanziarla manualmente tutta

    //provare ad istanziare tutto il deck

   // PlayCard[] cards = new PlayCard[4];

 /*   public void istanziaCarte(){
        for(int i=0; i<4; i++){
            cards[i] = new ResourceCard();
        }
    }*/


    /*
        cosi sto istanziando il mazzo delle risorse, mi è utile per fare i test su goal
        ma io ho solo bisogno di istanziare 4 carte in modo che posso metterele nel gamefield
     */

    public void creteResourceDeck(){

        for (JsonElement element : resources_jsonArray) {
            JsonObject singleCard = element.getAsJsonObject();
            //------- front side
            JsonObject frontSide = singleCard.get("front_side").getAsJsonObject();

            String angleLeftUp = frontSide.get("angle_left_up").getAsString();
            String angleLeftDown = frontSide.get("angle_left_down").getAsString();
            String angleRightUp = frontSide.get("angle_right_up").getAsString();
            String angleRightDown = frontSide.get("angle_right_down").getAsString();
            String centralResource_1 = frontSide.get("central1").getAsString();
            String centralResource_2 = frontSide.get("central2").getAsString();
            String centralResource_3 = frontSide.get("central3").getAsString();

            AnglesEnum enum_angleLeftUp = AnglesEnum.fromString(angleLeftUp);
            AnglesEnum enum_angleLeftDown = AnglesEnum.fromString(angleLeftDown);
            AnglesEnum enum_angleRightUp = AnglesEnum.fromString(angleRightUp);
            AnglesEnum enum_angleRightDown = AnglesEnum.fromString(angleRightDown);
            CentralEnum c_1 = CentralEnum.valueOf(centralResource_1);
            CentralEnum c_2 = CentralEnum.valueOf(centralResource_2);
            CentralEnum c_3 = CentralEnum.valueOf(centralResource_3);

            Side front_side = new Side(enum_angleRightUp,enum_angleRightDown,enum_angleLeftUp,enum_angleLeftDown,c_1,c_2,c_3);

            //------- back side

            JsonObject backSide = singleCard.get("back_side").getAsJsonObject();

            angleLeftUp = backSide.get("angle_left_up").getAsString();
            angleLeftDown = backSide.get("angle_left_down").getAsString();
            angleRightUp = backSide.get("angle_right_up").getAsString();
            angleRightDown = backSide.get("angle_right_down").getAsString();
            centralResource_1 = backSide.get("central1").getAsString();
            centralResource_2 = backSide.get("central2").getAsString();
            centralResource_3 = backSide.get("central3").getAsString();

            enum_angleLeftUp = AnglesEnum.fromString(angleLeftUp);
            enum_angleLeftDown = AnglesEnum.fromString(angleLeftDown);
            enum_angleRightUp = AnglesEnum.fromString(angleRightUp);
            enum_angleRightDown = AnglesEnum.fromString(angleRightDown);
            c_1 = CentralEnum.valueOf(centralResource_1);
            c_2 = CentralEnum.valueOf(centralResource_2);
            c_3 = CentralEnum.valueOf(centralResource_3);

            Side back_side = new Side(enum_angleRightUp,enum_angleRightDown,enum_angleLeftUp,enum_angleLeftDown,c_1,c_2,c_3);
            int point = singleCard.get("point").getAsInt();
            ResourceCard tmp = new ResourceCard(front_side,back_side,false,point);
            deck_resources.add(tmp);
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
