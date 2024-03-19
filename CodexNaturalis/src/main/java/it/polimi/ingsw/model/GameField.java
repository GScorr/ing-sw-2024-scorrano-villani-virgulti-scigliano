package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Card.GoldCard;
import it.polimi.ingsw.model.Card.PlayCard;
import it.polimi.ingsw.model.Card.ResourceCard;
import it.polimi.ingsw.model.ENUM.AnglesEnum;
import it.polimi.ingsw.model.ENUM.CentralEnum;
import it.polimi.ingsw.model.ENUM.Costraint;

//Class for the matrix field of each player
/*@Davide   XXX= DONE
* TODO:
*  - finisci metodo check placeable       XXX
*  - implementa metodo add e sub risorse  XXX
*  - gestione errori
*  - implementa gestione punti per carte risorsa e non
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

    //Insert of a card, it checks if the card can be placed and updates the resources counter and
    // it changes the values of the matrix so that they are coherent with the new placed card
    public boolean insertCard(PlayCard card, int x, int y){
        //check if you can place a card, not by constraint but by topology
        if( !checkPlacing( x, y ) ) return false;
        //check if you can place a card by constraint
        if ( card instanceof GoldCard ) if( !checkConstraints(((GoldCard) card).getCostraint()) ) return false;

        resourceCountChange(card, x, y);

        //add the points to the player, based on the points of the card
        if ( card instanceof GoldCard ) {   }
        else if ( card instanceof ResourceCard ) { player.addPoints(card.getPoint() ); }



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
        //Check that the card we are trying to place doesn't completely cover another card and that the sides of the cards aren't completely covered (all 4 of them)
        if   (  field[x][y].getCard().equals( field[x+1][y+1].getCard() )   ||
                field[x][y].getCard().equals( field[x][y+1].getCard() )     ||
                field[x][y].getCard().equals( field[x+1][y].getCard() )     ||
                field[x+1][y].getCard().equals( field[x+1][y+1].getCard() ) ||
                field[x][y+1].getCard().equals( field[x+1][y+1].getCard() ) )     return false;

        // Check that there is at least one card in the space ( you can't place a card in an empty space )
        if( !field[x][y].isEmpty() || !field[x+1][y].isEmpty() || !field[x][y+1].isEmpty() || !field[x+1][y+1].isEmpty()) {
            //Check if the card(s) that exist(s) have a valid angle that is not NONE --> check what is NONE in AnglesEnum  (if you don't understand this there is an equivalent if in the end**)
            return (field[x][y].isEmpty() || !field[x][y].getValue().equals(AnglesEnum.NONE)) &&
                    (field[x + 1][y].isEmpty() || !field[x + 1][y].getValue().equals(AnglesEnum.NONE)) &&
                    (field[x][y + 1].isEmpty() || !field[x][y + 1].getValue().equals(AnglesEnum.NONE)) &&
                    (field[x + 1][y + 1].isEmpty() || !field[x + 1][y + 1].getValue().equals(AnglesEnum.NONE));
        }
        return false;
    }

    //check all the resources num that the field will have after putting the card, given the card and the position
    public void resourceCountChange(PlayCard card, int x, int y){

        //Add for each side and for the central resource(if it exist) their counter
        addOne( card.getSide().getCentral_resource() );
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

    //check for all constraints of Gold Card, given a value of the constraint
    public boolean checkConstraints(Costraint val){
        switch ( val ){
            case FIVEINS:
                if( getNumOfInsect() >= 5 ) return true;
                return false;
            case FIVEANIM:
                if( getNumOfAnimal() >= 5 ) return true;
                return false;
            case FIVEMUSH:
                if( getNumOfMushroom() >= 5 ) return true;
                return false;
            case FIVEPLANT:
                if( getNumOfPlant() >= 5 ) return true;
                return false;
            case THREEINS:
                if( getNumOfInsect() >= 3 ) return true;
                return false;
            case THREEANIM:
                if( getNumOfAnimal() >= 3 ) return true;
                return false;
            case THREEMUSH:
                if( getNumOfMushroom() >= 3 ) return true;
                return false;
            case THREEPLANT:
                if( getNumOfPlant() >= 3 ) return true;
                return false;
            case TWOANIM_ONEINS:
                if( getNumOfAnimal() >= 2 && getNumOfInsect() >= 1 ) return true;
                return false;
            case TWOINS_ONEANIM:
                if( getNumOfInsect() >= 2 && getNumOfAnimal() >= 1 ) return true;
                return false;
            case TWOINS_ONEMUSH:
                if( getNumOfInsect() >= 2 && getNumOfMushroom() >= 1 ) return true;
                return false;
            case TWOMUSH_ONEINS:
                if( getNumOfMushroom() >= 2 && getNumOfInsect() >= 1 ) return true;
                return false;
            case TWOANIM_ONEMUSH:
                if( getNumOfAnimal() >= 2 && getNumOfMushroom() >= 1 ) return true;
                return false;
            case TWOINS_ONEPLANT:
                if( getNumOfInsect() >= 2 && getNumOfPlant() >= 1 ) return true;
                return false;
            case TWOMUSH_ONEANIM:
                if( getNumOfMushroom() >= 2 && getNumOfAnimal() >= 1 ) return true;
                return false;
            case TWOPLANT_ONEINS:
                if( getNumOfPlant() >= 2 && getNumOfInsect() >= 1 ) return true;
                return false;
            case THREEANIM_ONEINS:
                if( getNumOfAnimal() >= 3 && getNumOfInsect() >= 1 ) return true;
                return false;
            case THREEINS_ONEANIM:
                if( getNumOfInsect() >= 3 && getNumOfAnimal() >= 1 ) return true;
                return false;
            case THREEMUSH_ONEINS:
                if( getNumOfMushroom() >= 3 && getNumOfInsect() >= 1 ) return true;
                return false;
            case TWOANIM_ONEPLANT:
                if( getNumOfAnimal() >= 2 && getNumOfPlant() >= 1 ) return true;
                return false;
            case TWOMUSH_ONEPLANT:
                if( getNumOfMushroom() >= 2 && getNumOfPlant() >= 1 ) return true;
                return false;
            case TWOPLANT_ONEANIM:
                if( getNumOfPlant() >= 2 && getNumOfAnimal() >= 1 ) return true;
                return false;
            case TWOPLANT_ONEMUSH:
                if( getNumOfPlant() >= 2 && getNumOfMushroom() >= 1 ) return true;
                return false;
            case THREEINS_ONEPLANT:
                if( getNumOfInsect() >= 3 && getNumOfPlant() >= 1 ) return true;
                return false;
            case THREEINT_ONEAMUSH:
                if( getNumOfInsect() >= 3 && getNumOfMushroom() >= 1 ) return true;
                return false;
            case THREEMUSH_ONEANIM:
                if( getNumOfMushroom() >= 3 && getNumOfAnimal() >= 1 ) return true;
                return false;
            case THREEPLANT_ONEINS:
                if( getNumOfPlant() >= 3 && getNumOfInsect() >= 1 ) return true;
                return false;
            case THREEANIM_ONEAMUSH:
                if( getNumOfAnimal() >= 3 && getNumOfMushroom() >= 1 ) return true;
                return false;
            case THREEANIM_ONEPLANT:
                if( getNumOfAnimal() >= 3 && getNumOfPlant() >= 1 ) return true;
                return false;
            case THREEMUSH_ONEPLANT:
                if( getNumOfMushroom() >= 3 && getNumOfPlant() >= 1 ) return true;
                return false;
            case THREEPLANT_ONEANIM:
                if( getNumOfPlant() >= 3 && getNumOfAnimal() >= 1 ) return true;
                return false;
            case THREEPLANT_ONEAMUSH:
                if( getNumOfPlant() >= 3 && getNumOfMushroom() >= 1 ) return true;
                return false;
        }
        return true;
    }

}










/* Equivalent if
if( ( !field[x][y].isEmpty() && field[x][y].getValue().equals( AnglesEnum.NONE) ) ||
        ( !field[x+1][y].isEmpty() && field[x+1][y].getValue().equals( AnglesEnum.NONE) ) ||
        ( !field[x][y+1].isEmpty() && field[x][y+1].getValue().equals( AnglesEnum.NONE) ) ||
        ( !field[x+1][y+1].isEmpty() && field[x+1][y+1].getValue().equals( AnglesEnum.NONE) ) ) return false;
        return true;
 */