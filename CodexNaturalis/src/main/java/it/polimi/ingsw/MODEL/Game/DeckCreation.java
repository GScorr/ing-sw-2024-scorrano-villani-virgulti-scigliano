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

/**
 * This class manages the creation and shuffling of decks used in the game.
 */
public class DeckCreation implements Serializable {


    InputStream resources_filePath = getClass().getClassLoader().getResourceAsStream("JSON/resources.json");
    InputStream gold_filePath = getClass().getClassLoader().getResourceAsStream("JSON/gold.json");
    InputStream starting_filePath = getClass().getClassLoader().getResourceAsStream("JSON/starting.json");
    InputStream goal_filePath = getClass().getClassLoader().getResourceAsStream("JSON/goals.json");

    /*
     * file path

    private String resources_filePath = "src/main/java/it/polimi/ingsw/MODEL/Game/INITIALIZED/resources.json";
    private String gold_filePath = "src/main/java/it/polimi/ingsw/MODEL/Game/INITIALIZED/gold.json";
    private String starting_filePath = "src/main/java/it/polimi/ingsw/MODEL/Game/INITIALIZED/starting.json";
    private String goal_filePath = "src/main/java/it/polimi/ingsw/MODEL/Game/INITIALIZED/goals.json";
     */

    private transient JsonArray resources_jsonArray, gold_jsonArray,starting_jsonArray, goal_jsonArray;
    public static List<ResourceCard> deck_resources = new ArrayList<>();
    public static List<GoldCard> deck_gold = new ArrayList<>();
    public static List<StartingCard> deck_starting = new ArrayList<>();
    public static List<Goal> deck_goal = new ArrayList<>();

    public static int getSizeResourcesDeck(){ return deck_resources.size();}
    public static int getSizeGoldDeck(){ return deck_gold.size();}
    public static int getSizeStartingDeck(){ return deck_starting.size();}
    public static int getSizeGoalDeck(){ return deck_goal.size();}

    /**
     * Creates a Deck instance by reading JSON data from specified files and building the corresponding card decks.
     *
     * @throws IOException If any issues occur while reading the JSON files.
     */
    public DeckCreation() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resources_filePath))) {
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

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(gold_filePath))) {
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

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(starting_filePath))) {
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(goal_filePath))) {
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
        creteGoalDeck();
    }

    public static List<GoldCard> getDeck_gold() {
        return deck_gold;
    }

    public static List<ResourceCard> getDeck_resources() {
        return deck_resources;
    }

    /**
     * Creates a deck of resource cards by parsing the JSON data in `resources_jsonArray`
     * and constructing ResourceCard deck.
     */
    public void creteResourceDeck(){
        deck_resources.clear();
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

            //-- GUI file path
            String front_side_path = singleCard.get("front_img_path").getAsString();
            String back_side_path = singleCard.get("back_img_path").getAsString();

            tmp.setFront_side_path(front_side_path);
            tmp.setBack_side_path(back_side_path);
            deck_resources.add(tmp);
        }
    }

    /**
     * Shuffles the resource deck by randomizing the order of cards.
     */
    public void mixUpResouceDeck(){
        Collections.shuffle(deck_resources);
    }

    /**
     * Creates a deck of gold cards by parsing the JSON data in `golg_jsonArray`
     * and constructing GoldCard deck.
     */
    public void creteGoldDeck(){
        deck_gold.clear();

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
            //-- GUI file path
            String front_side_path = singleCard.get("front_img_path").getAsString();
            String back_side_path = singleCard.get("back_img_path").getAsString();

            tmp.setFront_side_path(front_side_path);
            tmp.setBack_side_path(back_side_path);

            deck_gold.add(tmp);
        }
    }

    /**
     * Shuffles the gold deck by randomizing the order of cards.
     */
    public void mixUpGoldDeck(){
        Collections.shuffle(deck_gold);
    }

    /**
     * Creates a deck of starting cards by parsing the JSON data in `starting_jsonArray`
     * and constructing StartingCard deck.
     */
    public void creteStartingDeck(){

        deck_starting.clear();
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

            //-- GUI file path
            String front_side_path = singleCard.get("front_img_path").getAsString();
            String back_side_path = singleCard.get("back_img_path").getAsString();

            tmp.setFront_side_path(front_side_path);
            tmp.setBack_side_path(back_side_path);

            deck_starting.add(tmp);
        }
    }

    /**
     * Creates a deck of goal cards by parsing the JSON data in `goal_jsonArray`
     * and constructing Goal deck.
     */
    public void creteGoalDeck(){
        deck_goal.clear();

        for (JsonElement element : goal_jsonArray) {
            JsonObject singlegoal = element.getAsJsonObject();
            String resource = singlegoal.get("resource").getAsString();
            AnglesEnum enum_resource = AnglesEnum.fromString(resource);
            int point = singlegoal.get("points").getAsInt();
            String string = singlegoal.get("string").getAsString();
            String strategyString = singlegoal.get("goalType").getAsString();
            GoalStrategy strategy = new GoalDiagonal();
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
            Goal tmp = new Goal(strategy,point,enum_resource,string);

            //-- GUI file path
            String front_side_path = singlegoal.get("front_img_path").getAsString();
            String back_side_path = singlegoal.get("back_img_path").getAsString();
            tmp.setFront_side_path(front_side_path);
            tmp.setBack_side_path(back_side_path);

            deck_goal.add(tmp);
        }
    }

    /**
     * Shuffles the goal deck by randomizing the order of cards.
     */
    public void mixUpGoalDeck(){
        Collections.shuffle(deck_goal);
    }

    /**
     * Shuffles the starting card deck by randomizing the order of cards.
     */
    public void mixUpStartingDeck(){
        Collections.shuffle(deck_starting);
    }

    public Deque<Goal> getGoalDeck(){
        return new ArrayDeque<Goal>(deck_goal);
    }

    public Deque<Goal> getMixGoalDeck(){
        mixUpGoalDeck();
        return new ArrayDeque<Goal>(deck_goal);
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
        mixUpStartingDeck();
        Deque<PlayCard> res_deck = new ArrayDeque<PlayCard>(deck_starting);
        return res_deck;
    }

}
