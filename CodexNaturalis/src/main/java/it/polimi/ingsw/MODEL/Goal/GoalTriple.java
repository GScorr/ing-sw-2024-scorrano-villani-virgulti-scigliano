package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

import java.io.Serializable;

/**
 * Implements goal strategy for calculating points based on multiples of three resources.
 *
 * This `GoalTriple` class implements the `GoalStrategy` interface. It calculates the total points
 * for a goal that requires a player to have a certain number of a specific resource (specified by `resource`).
 * The points are awarded based on multiples of three of the resource the player has on the field.
 * The base point value (`points`) is used as a multiplier.
 *
 */
public class GoalTriple implements GoalStrategy, Serializable {

    /**
     *
     * @param field the player's game field
     * @param points the base point value for the goal
     * @param resource the type of resource or card color that might be relevant to the strategy
     * @return
     */
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        return (int) (points*Math.floor(field.getNumOf(resource)/3));
    }

}
