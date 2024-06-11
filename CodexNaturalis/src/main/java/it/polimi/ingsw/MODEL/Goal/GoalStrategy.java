package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

/**
 * Interface defining a strategy for calculating points based on game goals.
 *
 */
public interface GoalStrategy {

    /**
     * Calculates the total points based on a specific goal strategy.
     *
     * @param field the player's game field
     * @param points the base point value for the goal
     * @param resource the type of resource or card color that might be relevant to the strategy
     * @return the total number of points earned based on the implemented goal strategy
     */
    int totalPoints(GameField field, int points, AnglesEnum resource);

}
