package it.polimi.ingsw.CONTROLLER;

import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.Costraint;
import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;

//import javax.smartcardio.Card;


public class GameFieldController {
    private GameField player_field;


    // TODO capire come collegare a MODEL
    // Function to check if the card can be placed,
    // Return false if you can't, true if you can
    public synchronized boolean checkPlacing(PlayCard card,int x, int y){
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
            if( ( !player_field.getField()[x][y].isEmpty() && player_field.getField()[x][y].getValue().equals( AnglesEnum.NONE) ) ||
                    ( !player_field.getField()[x+1][y].isEmpty() && player_field.getField()[x+1][y].getValue().equals( AnglesEnum.NONE) ) ||
                    ( !player_field.getField()[x][y+1].isEmpty() && player_field.getField()[x][y+1].getValue().equals( AnglesEnum.NONE) ) ||
                    ( !player_field.getField()[x+1][y+1].isEmpty() && player_field.getField()[x+1][y+1].getValue().equals( AnglesEnum.NONE) ) ) return false;
            else{
                if (card instanceof GoldCard) {
                    if ( !checkGoldConstraints( card.getCostraint()) ) return false;
                    player_field.getPlayer().addPoints(goldPointsCount((GoldCard) card, x, y));
                }
                if (card instanceof ResourceCard ) player_field.getPlayer().addPoints( resourcePointsCount(((ResourceCard) card)));
                resourcePointsChange(card, x, y);

                return player_field.insertCard(card, x, y);

            }
        }
        return false;
    }
    //check for all constraints of Gold Card,
    // given a value of the constraint
    public synchronized boolean checkGoldConstraints(Costraint val){
        return switch (val) {
            case FIVEINS -> player_field.getNumOfInsect() >= 5;
            case FIVEANIM -> player_field.getNumOfAnimal() >= 5;
            case FIVEMUSH -> player_field.getNumOfMushroom() >= 5;
            case FIVEPLANT -> player_field.getNumOfPlant() >= 5;
            case THREEINS -> player_field.getNumOfInsect() >= 3;
            case THREEANIM -> player_field.getNumOfAnimal() >= 3;
            case THREEMUSH -> player_field.getNumOfMushroom() >= 3;
            case THREEPLANT -> player_field.getNumOfPlant() >= 3;
            case TWOANIM_ONEINS -> player_field.getNumOfAnimal() >= 2 && player_field.getNumOfInsect() >= 1;
            case TWOINS_ONEANIM -> player_field.getNumOfInsect() >= 2 && player_field.getNumOfAnimal() >= 1;
            case TWOINS_ONEMUSH -> player_field.getNumOfInsect() >= 2 && player_field.getNumOfMushroom() >= 1;
            case TWOMUSH_ONEINS -> player_field.getNumOfMushroom() >= 2 && player_field.getNumOfInsect() >= 1;
            case TWOANIM_ONEMUSH -> player_field.getNumOfAnimal() >= 2 && player_field.getNumOfMushroom() >= 1;
            case TWOINS_ONEPLANT -> player_field.getNumOfInsect() >= 2 && player_field.getNumOfPlant() >= 1;
            case TWOMUSH_ONEANIM -> player_field.getNumOfMushroom() >= 2 && player_field.getNumOfAnimal() >= 1;
            case TWOPLANT_ONEINS -> player_field.getNumOfPlant() >= 2 && player_field.getNumOfInsect() >= 1;
            case THREEANIM_ONEINS -> player_field.getNumOfAnimal() >= 3 && player_field.getNumOfInsect() >= 1;
            case THREEINS_ONEANIM -> player_field.getNumOfInsect() >= 3 && player_field.getNumOfAnimal() >= 1;
            case THREEMUSH_ONEINS -> player_field.getNumOfMushroom() >= 3 && player_field.getNumOfInsect() >= 1;
            case TWOANIM_ONEPLANT -> player_field.getNumOfAnimal() >= 2 && player_field.getNumOfPlant() >= 1;
            case TWOMUSH_ONEPLANT -> player_field.getNumOfMushroom() >= 2 && player_field.getNumOfPlant() >= 1;
            case TWOPLANT_ONEANIM -> player_field.getNumOfPlant() >= 2 && player_field.getNumOfAnimal() >= 1;
            case TWOPLANT_ONEMUSH -> player_field.getNumOfPlant() >= 2 && player_field.getNumOfMushroom() >= 1;
            case THREEINS_ONEPLANT -> player_field.getNumOfInsect() >= 3 && player_field.getNumOfPlant() >= 1;
            case THREEINT_ONEAMUSH -> player_field.getNumOfInsect() >= 3 && player_field.getNumOfMushroom() >= 1;
            case THREEMUSH_ONEANIM -> player_field.getNumOfMushroom() >= 3 && player_field.getNumOfAnimal() >= 1;
            case THREEPLANT_ONEINS -> player_field.getNumOfPlant() >= 3 && player_field.getNumOfInsect() >= 1;
            case THREEANIM_ONEAMUSH -> player_field.getNumOfAnimal() >= 3 && player_field.getNumOfMushroom() >= 1;
            case THREEANIM_ONEPLANT -> player_field.getNumOfAnimal() >= 3 && player_field.getNumOfPlant() >= 1;
            case THREEMUSH_ONEPLANT -> player_field.getNumOfMushroom() >= 3 && player_field.getNumOfPlant() >= 1;
            case THREEPLANT_ONEANIM -> player_field.getNumOfPlant() >= 3 && player_field.getNumOfAnimal() >= 1;
            case THREEPLANT_ONEAMUSH -> player_field.getNumOfPlant() >= 3 && player_field.getNumOfMushroom() >= 1;
            default -> true;
        };
    }
    //count number of points if the card is Gold and has bonus related to number of stuff
    public synchronized int goldPointsCount(GoldCard card, int x, int y){
        switch ( card.getPointBonus() ){
            case NONE: return 0;
            case PEN: return player_field.getNumOfPen();
            case ANGLE: {
                int num_of_touch=0;
                //check how many cards i'll cover
                for(int i=x; i<x+2; i++)
                    for(int j=y; j<y+2; j++)
                        if( !player_field.getCell(i, j, 45).isEmpty() ) num_of_touch++;

                return num_of_touch * card.getPoint();
            }
            case PAPER: return player_field.getNumOfPaper();
            case FEATHER: return player_field.getNumOfFeather();
        }
        return 0;
    }
    //count number of points for resource cards
    public synchronized int resourcePointsCount(ResourceCard card){
        return card.getPoint();
    }
    //check all the resources num that the field will have after putting the card,
    // given the card and the position
    public synchronized void resourcePointsChange(PlayCard card, int x, int y){

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

    public synchronized GameField getCurrent(){
        return  this.player_field;
    }

}


