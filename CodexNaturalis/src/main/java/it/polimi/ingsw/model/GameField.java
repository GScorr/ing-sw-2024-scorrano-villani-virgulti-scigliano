package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Card.PlayCard;
import it.polimi.ingsw.model.ENUM.AnglesEnum;
import it.polimi.ingsw.model.ENUM.CentralEnum;

//Class for the matrix field of each player
/*@Davide   XXX= DONE
* TODO:
*  - finisci metodo check placeable
*  - implementa metodo add e sub risorse  XXX
*  - gestione errori
*  - implementa gestione punti per carte risorsa e non
* */
public class GameField {
    private GameFieldSingleCell[][] field;
    private int num_of_animal;
    private int num_of_mushroom;
    private int num_of_plant;
    private int num_of_insect;
    private int num_of_paper;
    private int num_of_pen;
    private int num_of_feather;


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

    public PlayCard getCardByPosition(int x, int y, int dim){
        if( ( x < 0 || x > dim ) || ( y < 0 || y > dim ) ) {System.out.print("ERROR: INDEX EXCEED MATRIX DIMENSION"); return null;}
        return field[x][y].getCard();
    }

    //Insert of a card, it checks if the card can be placed and updates the resources counter and
    // it changes the values of the matrix so that they are coherent with the new placed card
    public void insertCard(PlayCard card, int x, int y){

        if( !checkPlacing( x, y ) ) System.out.println("ERROR: YOU CANT PLACE THE CARD HERE");

        resourceCountChange(card, x, y);

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

    }
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


    // Function to check if the card can be placed, Return false if you can't, true if you can
    public boolean checkPlacing(int x, int y){
        //TODO: Check that you have the requirements to put the card
        //Check that the card we are trying to place doesn't completely cover another card
        if( field[x][y].getCard().equals( field[x+1][y+1].getCard() )) return false;
        // Check that there is at least one card in the space
        if( !field[x][y].isEmpty() || !field[x+1][y].isEmpty() || !field[x][y+1].isEmpty() || !field[x+1][y+1].isEmpty()) {
            //Check if the card(s) that exist(s) have a valid angle that is not NONE --> check what is NONE in AnglesEnum  (if you don't understand this there is an equivalent if in the end**)
            return (field[x][y].isEmpty() || !field[x][y].getValue().equals(AnglesEnum.NONE)) &&
                    (field[x + 1][y].isEmpty() || !field[x + 1][y].getValue().equals(AnglesEnum.NONE)) &&
                    (field[x][y + 1].isEmpty() || !field[x][y + 1].getValue().equals(AnglesEnum.NONE)) &&
                    (field[x + 1][y + 1].isEmpty() || !field[x + 1][y + 1].getValue().equals(AnglesEnum.NONE));
        }
        return false;
    }
    public void resourceCountChange(PlayCard card, int x, int y){

        //Add for each side and for the central resource(if it exist) their counter
        addOne( card.getCentralResources() );
        addOne( card.getSide().getAngleLeftUp() );
        addOne( card.getSide().getAngleLeftDown() );
        addOne( card.getSide().getAngleRightDown() );
        addOne( card.getSide().getAngleRightUp() );

        // sub 1 to each \old field if they are not empty --> the sub function doesn't sub anything if the value is NONE or EMPTY
        if( !field[x][y].isEmpty() )     subOne( field[x][y].getValue() );
        if( !field[x+1][y].isEmpty() )   subOne( field[x+1][y].getValue() );
        if( !field[x][y+1].isEmpty() )   subOne( field[x][y+1].getValue() );
        if( !field[x+1][y+1].isEmpty() ) subOne( field[x+1][y+1].getValue() );
    }
}










/* Equivalent if
if( ( !field[x][y].isEmpty() && field[x][y].getValue().equals( AnglesEnum.NONE) ) ||
        ( !field[x+1][y].isEmpty() && field[x+1][y].getValue().equals( AnglesEnum.NONE) ) ||
        ( !field[x][y+1].isEmpty() && field[x][y+1].getValue().equals( AnglesEnum.NONE) ) ||
        ( !field[x+1][y+1].isEmpty() && field[x+1][y+1].getValue().equals( AnglesEnum.NONE) ) ) return false;
        return true;
 */