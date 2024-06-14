package it.polimi.ingsw.MODEL;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.StartingCard;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.EdgeEnum;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;

//Class for the matrix field of each player
/*@Davide
* */

/**
 * This class manage the matrix field for each player
 */
public class GameField implements Serializable {

    public int card_inserted = 0;
    /**
     * Each game field cell is divided into 4 subcells to manage the 4 corners of the card.
     * every card will be placed into 4 subcells
     */
    private GameFieldSingleCell[][] field;
    private Player player;
    private int num_of_animal;
    private int num_of_mushroom;
    private int num_of_plant;
    private int num_of_insect;
    private int num_of_paper;
    private int num_of_pen;
    private int num_of_feather;
    private Goal global_goal1;
    private Goal global_goal2;


    public GameField(GameFieldSingleCell[][] field, Player player) {
        this.field = field;
        this.player = player;
    }

    public void setField(GameFieldSingleCell[][] field) {
        this.field = field;
    }

    public GameFieldSingleCell[][] getField() {
        return field;
    }

    /**
     * Get a single cell given index X,Y and dimension of the Matrix
     *
     * @param x x-position
     * @param y y-position
     * @param dim max dimension of the game field
     * @return return the single game field cell
     */
    public GameFieldSingleCell getCell(int x, int y, int dim){
        if( ( x < 0 || x > dim ) || ( y < 0 || y > dim ) ) {System.out.print("ERROR: INDEX EXCEED MATRIX DIMENSION"); return null;}
        return field[x][y];
    }

    /**
     * Gets the number of cards in the player's game filed that belong to a specific category.
     *
     * This method allows querying the number of cards the player has of a particular type
     * defined by the `AnglesEnum` enumeration.
     *
     * @param val the category of cards to count (ANIMAL, MUSHROOMS, etc.)
     * @return the number of cards in the player's field belonging to the specified category
     *
     */
    public int getNumOf(AnglesEnum val){
        switch ( val ){
            case ANIMAL:
                return num_of_animal;
            case MUSHROOMS:
                return num_of_mushroom;
            case PLANT:
                return num_of_plant;
            case INSECTS:
                return num_of_insect;
            case PEN:
                return num_of_pen;
            case PAPER:
                return num_of_paper;
            case FEATHER:
                return num_of_feather;

        }return 0;
    }

    public void setNumOfAnimal(int num_of_animal) {
        this.num_of_animal = num_of_animal;
    }

    public int getNumOfAnimal() {
        return num_of_animal;
    }

    public void setNumOfMushroom(int num_of_mushroom) {
        this.num_of_mushroom = num_of_mushroom;
    }

    public int getNumOfMushroom() {
        return num_of_mushroom;
    }

    public void setNumOfInsect(int num_of_insect) {
        this.num_of_insect = num_of_insect;
    }

    public int getNumOfInsect() {
        return num_of_insect;
    }

    public void setNumOfPlant(int num_of_plant) {
        this.num_of_plant = num_of_plant;
    }

    public int getNumOfPlant() {
        return num_of_plant;
    }

    public void setNumOfPaper(int num_of_paper) {
        this.num_of_paper = num_of_paper;
    }

    public int getNumOfPaper() {
        return num_of_paper;
    }

    public void setNumOfPen(int num_of_pen) {
        this.num_of_pen = num_of_pen;
    }

    public int getNumOfPen() {
        return num_of_pen;
    }

    public void setNumOfFeather(int num_of_feather) {
        this.num_of_feather = num_of_feather;
    }

    public int getNumOfFeather() {
        return num_of_feather;
    }

    public Player getPlayer(){return  this.player;}

    /**
     * Insert of a card, it checks if the card can be placed and updates the resources counter and
     * it changes the values of the matrix so that they are coherent with the new placed card

    /**
     * Attempts to insert a card at the specified location on the game field, updates the resources counter and
     * changes the values of the matrix so that they are coherent with the new placed car.
     *
     * @param card the card to insert
     * @param x the x-coordinate of the target location
     * @param y the y-coordinate of the target location
     * @return true if the card was successfully inserted, false otherwise
     */
    public boolean insertCard(PlayCard card, int x, int y){
        card_inserted++;

        if( !field[x][y].isEmpty() ){
            field[x][y].setCardDown(field[x][y].getCard());
            field[x][y].setOrder_below(field[x][y].getOrder_above());
            field[x][y].setValues( card.getSide().getAngleLeftUp(), 1); //probably useless, but I'm waiting for the Gui_Initialization
            field[x][y].setEdges( EdgeEnum.LEFTUP, 1); //probably useless, but I'm waiting for the Gui_Initialization
        }
        if( !field[x+1][y].isEmpty() ){
            field[x+1][y].setCardDown(field[x+1][y].getCard());
            field[x+1][y].setOrder_below(field[x+1][y].getOrder_above());
            field[x+1][y].setValues( card.getSide().getAngleLeftDown(), 1);
            field[x+1][y].setEdges( EdgeEnum.LEFTDOWN, 1);
        }
        if( !field[x][y+1].isEmpty() ){
            field[x][y+1].setCardDown(field[x][y+1].getCard());
            field[x][y+1].setOrder_below(field[x][y+1].getOrder_above());
            field[x][y+1].setValues( card.getSide().getAngleRightUp(), 1);
            field[x][y+1].setEdges( EdgeEnum.RIGHTUP, 1);
        }
        if( !field[x+1][y+1].isEmpty() ){
            field[x+1][y+1].setCardDown(field[x+1][y+1].getCard());
            field[x+1][y+1].setOrder_below(field[x+1][y+1].getOrder_above());
            field[x+1][y+1].setValues( card.getSide().getAngleRightDown(), 1 );
            field[x+1][y+1].setEdges( EdgeEnum.RIGHTDOWN, 1);
        }

        //insert card in the 4 cells
        field[x][y].setFilled(true);
        field[x][y].setCard(card);
        field[x][y].setOrder_above(card_inserted);
        field[x][y].setValue( card.getSide().getAngleLeftUp() );
        field[x][y].setValues( card.getSide().getAngleLeftUp(), 0 );
        field[x][y].setEdges( EdgeEnum.LEFTUP, 0);

        field[x+1][y].setFilled(true);
        field[x+1][y].setCard(card);
        field[x+1][y].setOrder_above(card_inserted);
        field[x+1][y].setValue( card.getSide().getAngleLeftDown() );
        field[x+1][y].setValues( card.getSide().getAngleLeftDown(), 0 );
        field[x+1][y].setEdges( EdgeEnum.LEFTDOWN, 0);


        field[x][y+1].setFilled(true);
        field[x][y+1].setCard(card);
        field[x][y+1].setOrder_above(card_inserted);
        field[x][y+1].setValue( card.getSide().getAngleRightUp() );
        field[x][y+1].setValues( card.getSide().getAngleRightUp(), 0 );
        field[x][y+1].setEdges( EdgeEnum.RIGHTUP, 0);

        field[x+1][y+1].setFilled(true);
        field[x+1][y+1].setCard(card);
        field[x+1][y+1].setOrder_above(card_inserted);
        field[x+1][y+1].setValue( card.getSide().getAngleRightDown() );
        field[x+1][y+1].setValues( card.getSide().getAngleRightDown(), 0 );
        field[x+1][y+1].setEdges( EdgeEnum.RIGHTDOWN, 0);
        return true;

    }

    /**
     * Increments the counter for a specific resource category (CentralEnum).
     *
     * @param val the resource category (e.g., ANIMAL, MUSHROOMS, etc.)
     */
    public void addOne(CentralEnum val){
        switch ( val ){
            case ANIMAL:
                num_of_animal++; break;
            case MUSHROOMS:
                num_of_mushroom++; break;
            case PLANT:
                num_of_plant++; break;
            case INSECTS:
                num_of_insect++; break;
            default:
                break;
        }
    }

    /**
     * Increments the counter for a specific resource category (AnglesEnum).
     *
     * @param val the resource category (e.g., ANIMAL, MUSHROOMS, PEN, etc.)
     */
    public void addOne(AnglesEnum val){
        switch ( val ){
            case ANIMAL:
                num_of_animal++; break;
            case MUSHROOMS:
                num_of_mushroom++; break;
            case PLANT:
                num_of_plant++; break;
            case INSECTS:
                num_of_insect++; break;
            case PEN:
                num_of_pen++; break;
            case PAPER:
                num_of_paper++; break;
            case FEATHER:
                num_of_feather++; break;
            case NONE:
            case EMPTY:
                break;
        }
    }

    /**
     * Decrements the counter for a specific resource category (AnglesEnum).
     *
     * @param val the resource category (e.g., ANIMAL, MUSHROOMS, PEN, etc.)
     */
    public void subOne(AnglesEnum val){
        switch ( val ){
            case ANIMAL:
                num_of_animal--; break;
            case MUSHROOMS:
                num_of_mushroom--; break;
            case PLANT:
                num_of_plant--; break;
            case INSECTS:
                num_of_insect--; break;
            case PEN:
                num_of_pen--; break;
            case PAPER:
                num_of_paper--; break;
            case FEATHER:
                num_of_feather--; break;
        }
    }

    /**
     * Synchronyzed method, updates resource counters based on the starting card's side effects.
     *
     * This method adds resources to the player's pool based on the bonuses
     * provided by the starting card's revealed side.
     *
     * @param card the starting card to analyze
     */
    public synchronized void startingCardResourcesAdder( StartingCard card){

        //Add for each side and for the central resource(if it exist) their counter
        addOne( card.getSide().getCentral_resource() );
        addOne( card.getSide().getCentral_resource2() );
        addOne( card.getSide().getCentral_resource3() );
        addOne( card.getSide().getAngleLeftUp() );
        addOne( card.getSide().getAngleLeftDown() );
        addOne( card.getSide().getAngleRightDown() );
        addOne( card.getSide().getAngleRightUp() );
    }

    public int getPlayerPoints(){
        return player.getPlayerPoints();
    }


    public void setGlobalGoal(Goal goal1, Goal goal2 ){
        global_goal1 = goal1;
        global_goal2 = goal2;}

}





