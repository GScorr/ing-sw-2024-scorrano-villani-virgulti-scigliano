package it.polimi.ingsw.MODEL.Game;
import java.io.*;

import com.google.gson.*;
import it.polimi.ingsw.MODEL.Card.*;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.BonusEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.Costraint;
import it.polimi.ingsw.MODEL.Goal.*;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DeckCreation {
    private String resources_filePath = "src/main/java/it/polimi/ingsw/MODEL/Game/INITIALIZED/resources.json";
    private String gold_filePath = "src/main/java/it/polimi/ingsw/MODEL/Game/INITIALIZED/gold.json";
    private String starting_filePath = "src/main/java/it/polimi/ingsw/MODEL/Game/INITIALIZED/starting.json";
    private String goal_filePath = "src/main/java/it/polimi/ingsw/MODEL/Game/INITIALIZED/goals.json";

    private JsonArray resources_jsonArray, gold_jsonArray,starting_jsonArray, goal_jsonArray;
    public static List<ResourceCard> deck_resources = new ArrayList<>();
    public static List<GoldCard> deck_gold = new ArrayList<>();
    public static List<StartingCard> deck_starting = new ArrayList<>();
    public static List<Goal> deck_goal = new ArrayList<>();

    public static int getSizeResourcesDeck(){ return deck_resources.size();}
    public static int getSizeGoldDeck(){ return deck_gold.size();}
    public static int getSizeStartingDeck(){ return deck_starting.size();}
    public static int getSizeGoalDeck(){ return deck_goal.size();}

    public DeckCreation() {
        try (BufferedReader reader = new BufferedReader(new FileReader(resources_filePath))) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
            String jsonString = jsonStringBuilder.toString();
            resources_jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(gold_filePath))) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
            String jsonString = jsonStringBuilder.toString();
            gold_jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(starting_filePath))) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
            String jsonString = jsonStringBuilder.toString();
            starting_jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(starting_filePath))) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
            String jsonString = jsonStringBuilder.toString();
            goal_jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
        } catch (IOException e) {
            e.printStackTrace();
        }


        creteResourceDeck();
        creteGoldDeck();
        creteStartingDeck();
        //creteGoalDeck();

    }

    public static List<GoldCard> getDeck_gold() {
        return deck_gold;
    }

    public static List<ResourceCard> getDeck_resources() {
        return deck_resources;
    }

    public static List<StartingCard> getDeck_starting() {
        return deck_starting;
    }
    public static List<Goal> getDeck_goal() {
        return deck_goal;
    }

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
    public void mixUpResouceDeck(){
        Collections.shuffle(deck_resources);
    }

    public void creteGoldDeck(){
        for (JsonElement element : gold_jsonArray) {
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
            Costraint costraint = Costraint.valueOf(singleCard.get("costraint").getAsString());
            BonusEnum bonus = BonusEnum.valueOf(singleCard.get("point_bonus").getAsString());

            GoldCard tmp = new GoldCard(front_side,back_side,false,point,costraint,bonus);
            deck_gold.add(tmp);
        }
    }
    public void mixUpGoldDeck(){
        Collections.shuffle(deck_gold);
    }

    public void creteStartingDeck(){
        for (JsonElement element : starting_jsonArray) {
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
            StartingCard tmp = new StartingCard(front_side,back_side,false);
            deck_starting.add(tmp);
        }
    }

    public void creteGoalDeck(){
        for (JsonElement element : goal_jsonArray) {
            JsonObject singlegoal = element.getAsJsonObject();
            String resource = singlegoal.get("resource").getAsString();
            AnglesEnum enum_resource = AnglesEnum.fromString(resource);
            int point = singlegoal.get("point").getAsInt();
            String strategyString = singlegoal.get("goalType").getAsString();
            GoalStrategy strategy = new GoalDiagonal(); //inizializzata per evitare errori
            switch(strategyString){
                case "GoalDiagonal":
                    strategy = new GoalDiagonal();
                    break;
                case "GoalDoubleGold":
                    strategy = new GoalDoubleGold();
                    break;
                case "GoalLFigure":
                    strategy = new GoalLFigure();
                    break;
                case "GoalTriple":
                    strategy = new GoalTriple();
                    break;
                case "GoalFeatherPenPaper":
                    strategy = new GoalFeatherPenPaper();
                    break;
            }
            Goal tmp = new Goal(strategy,point,enum_resource);
            deck_goal.add(tmp);
        }
    }
    public void mixUpGoalDeck(){
        Collections.shuffle(deck_goal);
    }
    public void mixUpStartingDeck(){
        Collections.shuffle(deck_starting);
    }
    public Deque<Goal> getGoalDeck(){
        Deque<Goal> g_deck = new ArrayDeque<Goal>();
        g_deck.addAll(deck_goal);
        return g_deck;
    }
    public Deque<Goal> getMixGoalDeck(){
        Deque<Goal> g_deck = new ArrayDeque<Goal>();
        mixUpGoalDeck();
        g_deck.addAll(deck_goal);
        return g_deck;
    }
    public Deque<PlayCard> getResourcesDeck(){
        Deque<PlayCard> res_deck = new ArrayDeque<PlayCard>();
        res_deck.addAll(deck_resources);
        return res_deck;
    }


    public Deque<PlayCard> getMixResourcesDeck(){
        Deque<PlayCard> res_deck = new ArrayDeque<PlayCard>();
        mixUpResouceDeck();
        res_deck.addAll(deck_resources);
        return res_deck;
    }



    public Deque<PlayCard> getGoldDeck(){
        Deque<PlayCard> res_deck = new ArrayDeque<PlayCard>();
        res_deck.addAll(deck_gold);
        return res_deck;
    }

    public Deque<PlayCard> getMixGoldDeck(){
        Deque<PlayCard> res_deck = new ArrayDeque<PlayCard>();
        mixUpGoldDeck();
        res_deck.addAll(deck_gold);
        return res_deck;
    }

    public Deque<PlayCard> getStartingDeck(){
        Deque<PlayCard> res_deck = new ArrayDeque<PlayCard>();
        res_deck.addAll(deck_starting);
        return res_deck;
    }

    public Deque<PlayCard> getMixStartingDeck(){
        Deque<PlayCard> res_deck = new ArrayDeque<PlayCard>();
        mixUpStartingDeck();
        res_deck.addAll(deck_starting);
        return res_deck;
    }

    public static void main(String[] args) {
        // Creazione di un'istanza di DeckCreation
        DeckCreation deckCreation = new DeckCreation();



        //lunghezza del deck
        System.out.println("Size del deck:");
        System.out.println(getSizeResourcesDeck());
        System.out.println(getSizeGoldDeck());
        System.out.println(getSizeStartingDeck());
        System.out.println(getSizeGoalDeck());

    }



}
