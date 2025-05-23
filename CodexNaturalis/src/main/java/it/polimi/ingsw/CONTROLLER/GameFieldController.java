package it.polimi.ingsw.CONTROLLER;

import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.Costraint;
import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;


/**
 * Manages interaction between a player and their game field.
 */
public class GameFieldController implements Serializable {

    private GameField player_field;

    private Player player;

    public Player getPlayer() {
        return player;
    }

    /**
     * Creates a GameFieldController for a specific player.
     *
     * This controller manages the interaction between the player and their game field.
     *
     * @param player The player for whom this controller is created.
     */
    public GameFieldController(Player player) {
        this.player = player;
        this.player_field = player.getGameField();
    }

    public GameField getPlayer_field() {
        return player_field;
    }

    /**
     * generic error
     */
    String errore = "Impossible insert a card in this position: ";

    /**
     * Checks if a card can be placed on the game field at a specified location (x, y).
     * <p>
     * This method throws exceptions for various invalid placement scenarios, including:
     * </p>
     * <ul>
     *   <li>Card completely covers another card or gets completely covered by existing cards.</li>
     *   <li>Card is placed on an empty space (no existing cards to connect with).</li>
     *   <li>Existing cards have invalid angles (potentially defined as AnglesEnum.NONE).</li>
     *   <li>Card violates specific constraints for Gold Cards (if applicable).</li>
     * </ul>
     * <p>
     * It updates player points based on the placed card type
     * </p>
     *
     * @param card The PlayCard object representing the card to be placed.
     * @param x The x-coordinate on the game field for placement.
     * @param y The y-coordinate on the game field for placement.
     * @return True if the card can be placed, false otherwise.
     * @throws ControllerException If the card placement is invalid (various error codes).
     */
    public synchronized boolean checkPlacing(PlayCard card,int x, int y){
        //Check that the card we are trying to place doesn't completely cover another card and that the sides of the cards aren't completely covered (all 4 of them)
        if   (  (player_field.getField()[x][y].getCard().equals( player_field.getField()[x+1][y+1].getCard() )&& player_field.getField()[x][y].isFilled())||
                (player_field.getField()[x][y].getCard().equals( player_field.getField()[x][y+1].getCard() ) && player_field.getField()[x][y].isFilled())     ||
                (player_field.getField()[x][y].getCard().equals( player_field.getField()[x+1][y].getCard() ) && player_field.getField()[x][y].isFilled())    ||
                (player_field.getField()[x+1][y].getCard().equals( player_field.getField()[x+1][y+1].getCard() ) && player_field.getField()[x+1][y].isFilled()) ||
                (player_field.getField()[x][y+1].getCard().equals( player_field.getField()[x+1][y+1].getCard() )&& player_field.getField()[x][y+1].isFilled())){
            throw new ControllerException(11,errore + "another Card is already insert in this position ");
            }
        // Check that there is at least one card in the space ( you can't place a card in an empty space )
        if(     !player_field.getField()[x][y].isEmpty()   ||
                !player_field.getField()[x+1][y].isEmpty() ||
                !player_field.getField()[x][y+1].isEmpty() ||
                !player_field.getField()[x+1][y+1].isEmpty()) {
            //Check if the card(s) that exist(s) have a valid angle that is not NONE --> check what is NONE in AnglesEnum  (if you don't understand this there is an equivalent if in the end**)
            if ((!player_field.getField()[x][y].isEmpty() && player_field.getField()[x][y].getValue().equals(AnglesEnum.NONE)) ||
                    (!player_field.getField()[x + 1][y].isEmpty() && player_field.getField()[x + 1][y].getValue().equals(AnglesEnum.NONE)) ||
                    (!player_field.getField()[x][y + 1].isEmpty() && player_field.getField()[x][y + 1].getValue().equals(AnglesEnum.NONE)) ||
                    (!player_field.getField()[x + 1][y + 1].isEmpty() && player_field.getField()[x + 1][y + 1].getValue().equals(AnglesEnum.NONE))){
                throw new ControllerException(13,errore + "The Cards already in the field doesn't have a valid angle ");}
            else {
                if (card instanceof GoldCard) {
                    if (!checkGoldConstraints(card.getCostraint())) {
                        throw new ControllerException(14,errore + "Gold Costraint requirement not matched ");
                    }
                    else {
                        player.addPoints(goldPointsCount((GoldCard) card, x, y));
                    }
                }
                if (card instanceof ResourceCard)
                    player.addPoints(resourcePointsCount(((ResourceCard) card)));
                resourcePointsChange(card, x, y);
                return true;
            }
        }else{
            throw new ControllerException(12,errore + "Card insert in an empty space ");
        }

    }

    /**
     * Checks if the given constraint for a Gold Card is satisfied by the player's game field.
     * @param val The Constraint value representing the specific constraint to check.
     * @return True if the constraint is met, false otherwise.
     */
    private synchronized boolean checkGoldConstraints(Costraint val){
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

    /**
     * Calculates point bonuses for placed Gold Cards based on card type and field state.
     * <p>
     * This method evaluates the specific conditions and constraints of the placed Gold Card
     * to determine the number of bonus points awarded. The evaluation considers the card's type
     * and the current state of the game field at the specified coordinates.
     * </p>
     *
     * @param card The GoldCard object representing the placed card.
     * @param x The x-coordinate on the game field for the placed card.
     * @param y The y-coordinate on the game field for the placed card.
     * @return The number of points awarded based on the Gold Card's point bonus.
     */
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

                return num_of_touch ;
            }
            case PAPER: return player_field.getNumOfPaper();
            case FEATHER: return player_field.getNumOfFeather();
        }
        return 0;
    }

    /**
     * count number of points for resource cards
     * @param card
     * @return
     */
    public synchronized int resourcePointsCount(ResourceCard card){
        return card.getPoint();
    }

    /**
     * <p>
     * This method adds resource points based on the placed card's side information:
     * </p>
     * <ul>
     *   <li>Central resource (if present)</li>
     *   <li>Angle resources (Left Up, Left Down, Right Down, Right Up)</li>
     * </ul>
     * <p>
     * It then subtracts resource points from previously occupied spaces on the field (if not empty).
     * </p>
     *
     * @param card The PlayCard object representing the placed card.
     * @param x The x-coordinate on the game field for the placed card.
     * @param y The y-coordinate on the game field for the placed card.
     */

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


