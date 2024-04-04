package it.polimi.ingsw.MODEL;

import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.Costraint;

//Class for the matrix field of each player
/*@Davide   XXX= DONE
* TODO:
*  - finisci metodo check placeable       XXX
*  - implementa metodo add e sub risorse  XXX
*  - gestione errori
*  - implementa gestione punti per carte risorsa e non XXX
* */
public class GameField {
    private GameFieldSingleCell[][] field;
    private Player player;
    private int num_of_animal;
    private int num_of_mushroom;
    private int num_of_plant;
    private int num_of_insect;
    private int num_of_paper;
    private int num_of_pen;
    private int num_of_feather;

    public GameField(GameFieldSingleCell[][] field) {
        this.field = field;
    }

    public void setField(GameFieldSingleCell[][] field) {
        this.field = field;
    }
    public GameFieldSingleCell[][] getField() {
        return field;
    }

    //Get a single cell given index X,Y and dimension of the Matrix
    public GameFieldSingleCell getCell(int x, int y, int dim){
        if( ( x < 0 || x > dim ) || ( y < 0 || y > dim ) ) {System.out.print("ERROR: INDEX EXCEED MATRIX DIMENSION"); return null;}
        return field[x][y];
    }

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

    //Insert of a card, it checks if the card can be placed and updates the resources counter and
    // it changes the values of the matrix so that they are coherent with the new placed card
    public boolean insertCard(PlayCard card, int x, int y){

        field[x][y].setCardDown(field[x][y].getCard());
        field[x+1][y].setCardDown(field[x][y].getCard());
        field[x][y+1].setCardDown(field[x][y].getCard());
        field[x+1][y+1].setCardDown(field[x][y].getCard());

        //insert card in the 4 cells
        field[x][y].setFilled(true);
        field[x][y].setCard(card);
        field[x][y].setValue( card.getSide().getAngleLeftUp() );

        field[x+1][y].setFilled(true);
        field[x+1][y].setCard(card);
        field[x+1][y].setValue( card.getSide().getAngleRightUp() );

        field[x][y+1].setFilled(true);
        field[x][y+1].setCard(card);
        field[x][y+1].setValue( card.getSide().getAngleLeftDown() );

        field[x+1][y+1].setFilled(true);
        field[x+1][y+1].setCard(card);
        field[x+1][y+1].setValue( card.getSide().getAngleRightDown() );
        return true;
    }

    public void cardNotPlaceable(){};

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
            case NONE:
                break;
        }
    }
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


}










/* Equivalent if
if( ( !field[x][y].isEmpty() && field[x][y].getValue().equals( AnglesEnum.NONE) ) ||
        ( !field[x+1][y].isEmpty() && field[x+1][y].getValue().equals( AnglesEnum.NONE) ) ||
        ( !field[x][y+1].isEmpty() && field[x][y+1].getValue().equals( AnglesEnum.NONE) ) ||
        ( !field[x+1][y+1].isEmpty() && field[x+1][y+1].getValue().equals( AnglesEnum.NONE) ) ) return false;
        return true;
 */