package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

import java.io.Serializable;

/**
 * Implements goal strategy for calculating points based on double the number of a specific resource.
 *
 * This `GoalDoubleGold` class implements the `GoalStrategy` interface. It calculates the total points
 * for a goal that requires a player to have a specific number of a resource (specified by `resource`).
 * The points are awarded based on double the number of resources the player has, divided by 2
 * (effectively awarding points for every two resources). The base point value (`points`) is used as a multiplier.
 *
 */
public class GoalDoubleGold implements GoalStrategy, Serializable {

    /**
     * Calculates the total points based on double the number of a specific resource on the game field.
     *
     * @param field the player's game field
     * @param points the base point value for each "double resource"
     * @param resource the resource type to consider
     * @return the total number of points earned based on the resource count
     */
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        return (int) (points*Math.floor(field.getNumOf(resource)/2));
    }
}
