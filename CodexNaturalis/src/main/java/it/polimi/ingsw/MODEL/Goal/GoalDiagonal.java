package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.GameField;

import java.io.Serializable;


/**
 * Implements goal strategy for calculating points based on diagonal pairs of matching cards.
 */
public class GoalDiagonal implements GoalStrategy, Serializable {

    /**
     * Calculates the total points based on diagonal pairs of matching cards on the game field.
     *
     * @param field the player's game field
     * @param points the base point value for each matching pair
     * @param resource the resource type to consider for matching
     * @return the total number of points earned based on diagonal pairs
     */
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        int counter = 0;
        switch (resource) {
            case INSECTS:
                for (int i = 1; i < Constants.MATRIXDIM - 2; i++) {
                    for (int j = 1; j < Constants.MATRIXDIM - 2; j++) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.INSECTS)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.INSECTS)) {
                            if (field.getCell(i + 1, j + 1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.INSECTS)&&field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.INSECTS)) {
                                counter++;
                            }
                        }
                    }
                }
                break;
            case PLANT:
                for (int i = 1; i < Constants.MATRIXDIM - 2; i++) {
                    for (int j = 1; j < Constants.MATRIXDIM - 2; j++) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT)) {
                            if (field.getCell(i + 1, j + 1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT)&&field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT)) {
                                counter++;
                            }
                        }
                    }
                }
                break;
            case MUSHROOMS:
                for (int i = 1; i < Constants.MATRIXDIM - 2; i++){
                    for (int j = Constants.MATRIXDIM - 2; j >1 ; j--) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS)) {
                            if (field.getCell(i + 1, j - 1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS)&&field.getCell(i+1, j-1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS)) {
                                counter++;
                            }
                        }
                    }
                }
                break;
            case ANIMAL:
                for (int i = 1; i < Constants.MATRIXDIM - 2; i++){
                    for (int j = Constants.MATRIXDIM - 2; j >1 ; j--) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL)) {
                            if (field.getCell(i + 1, j - 1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL)&&field.getCell(i+1, j-1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL)) {
                                counter++;
                            }
                        }
                    }
                }
                break;
        }
        return counter*points;
    }
}
