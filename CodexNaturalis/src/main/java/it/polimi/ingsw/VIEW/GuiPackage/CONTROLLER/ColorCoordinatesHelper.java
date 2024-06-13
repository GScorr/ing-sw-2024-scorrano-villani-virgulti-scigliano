package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.ENUM.Costraint;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import javafx.scene.paint.Color;

/**
 * This class provides helper methods for color conversion and calculating coordinates
 * on the game board based on player points and card colors. It also handles
 * card placement validation considering overlapping cards, empty spaces, valid
 * card angles, and gold card constraints.
 */
public class ColorCoordinatesHelper {

    /**
     * Converts a ColorsEnum value (e.g., RED, BLU) to a JavaFX Color object.
     *
     * @param color The color enum value to convert.
     * @return The corresponding JavaFX Color object, or null if the color is not found.
     */
    public Color fromEnumtoColor(ColorsEnum color) {
        switch (color) {
            case RED:
                return Color.RED;
            case BLU:
                return Color.BLUE;
            case YELLOW:
                return Color.YELLOW;
            case GREEN:
                return Color.GREEN;
            default:
                return null;
        }
    }

    /**
     * Calculates the X coordinate on the game board for placing a card based on the player's points
     * and the card's color. It considers an offset to avoid overlapping card placements for different colors.
     *
     * @param p The player object.
     * @param color The color of the card to be placed.
     * @return The X coordinate for the card placement.
     */
    public int fromPointstoX(Player p, ColorsEnum color) {
        int x = calculateBaseX(p);
        // Adjust x based on color to avoid overlap
        switch (color) {
            case RED:
                x += 5;
                break;
            case BLU:
                x -= 5;
                break;
            case YELLOW:
                x += 5;
                break;
            case GREEN:
                x -= 5;
                break;
        }
        return x;
    }

    /**
     * Calculates the Y coordinate on the game board for placing a card based on the player's points
     * and the card's color. It considers an offset to avoid overlapping card placements for different colors.
     *
     * @param p The player object.
     * @param color The color of the card to be placed.
     * @return The Y coordinate for the card placement.
     */
    public int fromPointstoY(Player p, ColorsEnum color) {
        int y = calculateBaseY(p);
        // Adjust y based on color to avoid overlap
        switch (color) {
            case RED:
                y += 5;
                break;
            case BLU:
                y -= 5;
                break;
            case YELLOW:
                y -= 5;
                break;
            case GREEN:
                y += 5;
                break;
        }
        return y;
    }

    /**
     * Calculates the base X coordinate on the game board based on the player's points.
     *
     * @param p The player object.
     * @return The base X coordinate for the player's card placement.
     */
    private int calculateBaseX(Player p) {
        switch (p.getPlayerPoints()) {
            case 0:
                return 80;
            case 1:
            case 20:
            case 25:
            case 29:
                return 155;
            case 2:
                return 230;
            case 3:
            case 10:
            case 11:
            case 18:
            case 19:
            case 27:
            case 28:
                return 270;
            case 4:
            case 9:
            case 12:
            case 17:
                return 195;
            case 5:
            case 8:
            case 13:
            case 16:
                return 120;
            case 6:
            case 7:
            case 14:
            case 15:
            case 21:
            case 22:
            case 23:
                return 45;
            case 24:
                return 90;
            case 26:
                return 225;
            default:
                return 0;
        }
    }

    /**
     * Calculates the base Y coordinate on the game board based on the player's points.
     *
     * @param p The player object.
     * @return The base Y coordinate for the player's card placement.
     */
    private int calculateBaseY(Player p) {
        switch (p.getPlayerPoints()) {
            case 0:
            case 1:
            case 2:
                return 585;
            case 3:
            case 4:
            case 5:
            case 6:
                return 525;
            case 7:
            case 8:
            case 9:
            case 10:
                return 455;
            case 11:
            case 12:
            case 13:
            case 14:
                return 385;
            case 15:
            case 16:
            case 17:
            case 18:
                return 315;
            case 19:
            case 21:
                return 245;
            case 22:
            case 28:
                return 175;
            case 20:
                return 215;
            case 23:
            case 27:
                return 110;
            case 24:
            case 26:
                return 55;
            case 25:
                return 45;
            case 29:
                return 130;
            default:
                return 0;
        }
    }

    /**
     * Checks if a specific card (`card`) can be placed at a given position (`x`, `y`) on the player's game board (`player_field`),
     * considering various factors:
     *  * Overlapping cards: Ensures the new card doesn't completely cover existing cards.
     *  * Empty space: Verifies that the target location isn't empty.
     *  * Valid card angles: Checks if existing cards at the target location have valid angles (not NONE in the `AnglesEnum`).
     *  * Gold card constraints: For gold cards, it verifies if the player's field satisfies the card's specific constraint
     *  (using the `checkGoldConstraints` method) before allowing placement (only applies if the card isn't flipped).
     *
     * @param flipped Whether the card is flipped or not.
     * @param card The card to be placed.
     * @param player_field The player's game field.
     * @param x The X coordinate of the target placement location.
     * @param y The Y coordinate of the target placement location.
     * @return True if the card can be placed at the specified location, false otherwise.
     *
     */
    public synchronized boolean checkPlacing(boolean flipped, PlayCard card, GameField player_field, int x, int y) {
        //Check that the card we are trying to place doesn't completely cover another card and that the sides of the cards aren't completely covered (all 4 of them)
        if ((player_field.getField()[x][y].getCard().equals(player_field.getField()[x + 1][y + 1].getCard()) && player_field.getField()[x][y].isFilled()) ||
                (player_field.getField()[x][y].getCard().equals(player_field.getField()[x][y + 1].getCard()) && player_field.getField()[x][y].isFilled()) ||
                (player_field.getField()[x][y].getCard().equals(player_field.getField()[x + 1][y].getCard()) && player_field.getField()[x][y].isFilled()) ||
                (player_field.getField()[x + 1][y].getCard().equals(player_field.getField()[x + 1][y + 1].getCard()) && player_field.getField()[x + 1][y].isFilled()) ||
                (player_field.getField()[x][y + 1].getCard().equals(player_field.getField()[x + 1][y + 1].getCard()) && player_field.getField()[x][y + 1].isFilled())) {
            System.out.println("a");
            return false;
        }
        // Check that there is at least one card in the space ( you can't place a card in an empty space )
        if (!player_field.getField()[x][y].isEmpty() ||
                !player_field.getField()[x + 1][y].isEmpty() ||
                !player_field.getField()[x][y + 1].isEmpty() ||
                !player_field.getField()[x + 1][y + 1].isEmpty()) {
            //Check if the card(s) that exist(s) have a valid angle that is not NONE --> check what is NONE in AnglesEnum  (if you don't understand this there is an equivalent if in the end**)
            if ((!player_field.getField()[x][y].isEmpty() && player_field.getField()[x][y].getValue().equals(AnglesEnum.NONE)) ||
                    (!player_field.getField()[x + 1][y].isEmpty() && player_field.getField()[x + 1][y].getValue().equals(AnglesEnum.NONE)) ||
                    (!player_field.getField()[x][y + 1].isEmpty() && player_field.getField()[x][y + 1].getValue().equals(AnglesEnum.NONE)) ||
                    (!player_field.getField()[x + 1][y + 1].isEmpty() && player_field.getField()[x + 1][y + 1].getValue().equals(AnglesEnum.NONE))) {
                System.out.println("b");
                return false;
            }
            else {
                if (card instanceof GoldCard) {
                    if(!flipped){
                        if (!checkGoldConstraints(player_field, card.getCostraint())) {
                            System.out.println("c");
                            return false;
                        }
                    }
                }
            }
        } else {
            return false;
        }
        System.out.println("e");
        return true;
    }

    /**
     * Checks if the player's game field meets the specific constraint (`val`) required for placing a gold card.
     *
     * @param player_field The player's game field.
     * @param val The constraint to be checked (e.g., FIVEINS requires at least 5 insects on the field).
     * @return True if the player's field satisfies the constraint, false otherwise.
     */
    private synchronized boolean checkGoldConstraints(GameField player_field, Costraint val) {
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

}