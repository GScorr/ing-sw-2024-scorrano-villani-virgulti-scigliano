package it.polimi.ingsw.CONTROLLER;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.Costraint;
import it.polimi.ingsw.MODEL.GameField;

public class GameFieldController {
    private GameField player_field;

    //check all the resources num that the field will have after putting the card, given the card and the position
    public void resourceCountChange(PlayCard card, int x, int y){

        //Add for each side and for the central resource(if it exist) their counter
        player_field.addOne( card.getSide().getCentral_resource() );
        player_field.addOne( card.getSide().getAngleLeftUp() );
        player_field.addOne( card.getSide().getAngleLeftDown() );
        player_field.addOne( card.getSide().getAngleRightDown() );
        player_field.addOne( card.getSide().getAngleRightUp() );

        // sub 1 to each \old field if they are not empty --> the sub function doesn't sub anything if the value is NONE or EMPTY
        if( !player_field.getField()[x][y].isEmpty() )     player_field.subOne( player_field.getField()[x][y].getValue() );
        if( !player_field.getField()[x+1][y].isEmpty() )   player_field.subOne( player_field.getField()[x+1][y].getValue() );
        if( !player_field.getField()[x][y+1].isEmpty() )   player_field.subOne( player_field.getField()[x][y+1].getValue() );
        if( !player_field.getField()[x+1][y+1].isEmpty() ) player_field.subOne( player_field.getField()[x+1][y+1].getValue() );
    }

    // Function to check if the card can be placed, Return false if you can't, true if you can
    public boolean checkPlacing(int x, int y){
        //Check that the card we are trying to place doesn't completely cover another card and that the sides of the cards aren't completely covered (all 4 of them)
        if   (  player_field.getField()[x][y].getCard().equals( player_field.getField()[x+1][y+1].getCard() )   ||
                player_field.getField()[x][y].getCard().equals( player_field.getField()[x][y+1].getCard() )     ||
                player_field.getField()[x][y].getCard().equals( player_field.getField()[x+1][y].getCard() )     ||
                player_field.getField()[x+1][y].getCard().equals( player_field.getField()[x+1][y+1].getCard() ) ||
                player_field.getField()[x][y+1].getCard().equals( player_field.getField()[x+1][y+1].getCard() ) )     return false;

        // Check that there is at least one card in the space ( you can't place a card in an empty space )
        if(     !player_field.getField()[x][y].isEmpty()   ||
                !player_field.getField()[x+1][y].isEmpty() ||
                !player_field.getField()[x][y+1].isEmpty() ||
                !player_field.getField()[x+1][y+1].isEmpty())
        {
            //Check if the card(s) that exist(s) have a valid angle that is not NONE --> check what is NONE in AnglesEnum  (if you don't understand this there is an equivalent if in the end**)
            return  (player_field.getField()[x][y].isEmpty() || !player_field.getField()[x][y].getValue().equals(AnglesEnum.NONE)) &&
                    (player_field.getField()[x + 1][y].isEmpty() || !player_field.getField()[x + 1][y].getValue().equals(AnglesEnum.NONE)) &&
                    (player_field.getField()[x][y + 1].isEmpty() || !player_field.getField()[x][y + 1].getValue().equals(AnglesEnum.NONE)) &&
                    (player_field.getField()[x + 1][y + 1].isEmpty() || !player_field.getField()[x + 1][y + 1].getValue().equals(AnglesEnum.NONE));
        }
        return false;
    }


    //check for all constraints of Gold Card, given a value of the constraint
    public boolean checkConstraints(Costraint val){
        switch ( val ){
            case FIVEINS:
                return player_field.getNumOfInsect() >= 5;
            case FIVEANIM:
                return player_field.getNumOfAnimal() >= 5;
            case FIVEMUSH:
                return player_field.getNumOfMushroom() >= 5;
            case FIVEPLANT:
                return player_field.getNumOfPlant() >= 5;
            case THREEINS:
                return player_field.getNumOfInsect() >= 3;
            case THREEANIM:
                return player_field.getNumOfAnimal() >= 3;
            case THREEMUSH:
                return player_field.getNumOfMushroom() >= 3;
            case THREEPLANT:
                return player_field.getNumOfPlant() >= 3;
            case TWOANIM_ONEINS:
                return player_field.getNumOfAnimal() >= 2 && player_field.getNumOfInsect() >= 1;
            case TWOINS_ONEANIM:
                return player_field.getNumOfInsect() >= 2 && player_field.getNumOfAnimal() >= 1;
            case TWOINS_ONEMUSH:
                return player_field.getNumOfInsect() >= 2 && player_field.getNumOfMushroom() >= 1;
            case TWOMUSH_ONEINS:
                return player_field.getNumOfMushroom() >= 2 && player_field.getNumOfInsect() >= 1;
            case TWOANIM_ONEMUSH:
                return player_field.getNumOfAnimal() >= 2 && player_field.getNumOfMushroom() >= 1;
            case TWOINS_ONEPLANT:
                return player_field.getNumOfInsect() >= 2 && player_field.getNumOfPlant() >= 1;
            case TWOMUSH_ONEANIM:
                return player_field.getNumOfMushroom() >= 2 && player_field.getNumOfAnimal() >= 1;
            case TWOPLANT_ONEINS:
                return player_field.getNumOfPlant() >= 2 && player_field.getNumOfInsect() >= 1;
            case THREEANIM_ONEINS:
                return player_field.getNumOfAnimal() >= 3 && player_field.getNumOfInsect() >= 1;
            case THREEINS_ONEANIM:
                return player_field.getNumOfInsect() >= 3 && player_field.getNumOfAnimal() >= 1;
            case THREEMUSH_ONEINS:
                return player_field.getNumOfMushroom() >= 3 && player_field.getNumOfInsect() >= 1;
            case TWOANIM_ONEPLANT:
                return player_field.getNumOfAnimal() >= 2 && player_field.getNumOfPlant() >= 1;
            case TWOMUSH_ONEPLANT:
                return player_field.getNumOfMushroom() >= 2 && player_field.getNumOfPlant() >= 1;
            case TWOPLANT_ONEANIM:
                return player_field.getNumOfPlant() >= 2 && player_field.getNumOfAnimal() >= 1;
            case TWOPLANT_ONEMUSH:
                return player_field.getNumOfPlant() >= 2 && player_field.getNumOfMushroom() >= 1;
            case THREEINS_ONEPLANT:
                return player_field.getNumOfInsect() >= 3 && player_field.getNumOfPlant() >= 1;
            case THREEINT_ONEAMUSH:
                return player_field.getNumOfInsect() >= 3 && player_field.getNumOfMushroom() >= 1;
            case THREEMUSH_ONEANIM:
                return player_field.getNumOfMushroom() >= 3 && player_field.getNumOfAnimal() >= 1;
            case THREEPLANT_ONEINS:
                return player_field.getNumOfPlant() >= 3 && player_field.getNumOfInsect() >= 1;
            case THREEANIM_ONEAMUSH:
                return player_field.getNumOfAnimal() >= 3 && player_field.getNumOfMushroom() >= 1;
            case THREEANIM_ONEPLANT:
                return player_field.getNumOfAnimal() >= 3 && player_field.getNumOfPlant() >= 1;
            case THREEMUSH_ONEPLANT:
                return player_field.getNumOfMushroom() >= 3 && player_field.getNumOfPlant() >= 1;
            case THREEPLANT_ONEANIM:
                return player_field.getNumOfPlant() >= 3 && player_field.getNumOfAnimal() >= 1;
            case THREEPLANT_ONEAMUSH:
                return player_field.getNumOfPlant() >= 3 && player_field.getNumOfMushroom() >= 1;
        }
        return true;
    }



}


