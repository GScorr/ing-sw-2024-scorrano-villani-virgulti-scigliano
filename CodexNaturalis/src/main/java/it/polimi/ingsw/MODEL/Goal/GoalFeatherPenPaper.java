package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

import java.io.Serializable;

/**
 * Implements goal strategy for calculating points based on the minimum of three resources.
 *
 * This `GoalFeatherPenPaper` class implements the `GoalStrategy` interface. It calculates the total points
 * for a goal that requires a player to have a certain amount of three resources: Feather, Pen, and Paper.
 * The points are awarded based on the minimum number of any of these resources the player has on the field.
 * The base point value (`points`) is used as a multiplier.
 *
 */
public class GoalFeatherPenPaper implements GoalStrategy, Serializable {

    /**
     * Calculates the total points based on the minimum amount of Feather, Pen, or Paper resources.
     *
     * @param field the player's game field
     * @param points the base point value for each resource in the minimum set
     * @param resource
     * @return the total number of points earned based on the minimum resource count
     */
    public int totalPoints(GameField field, int points, AnglesEnum resource){
        int minimumResource;
        minimumResource = Math.min(Math.min(field.getNumOfFeather(), field.getNumOfPen()),field.getNumOfPaper());
        return points*minimumResource;
    }

}
