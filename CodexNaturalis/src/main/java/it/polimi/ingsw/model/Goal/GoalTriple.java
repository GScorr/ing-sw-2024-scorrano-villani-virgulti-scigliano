package it.polimi.ingsw.model.Goal;

import it.polimi.ingsw.model.ENUM.AnglesEnum;
import it.polimi.ingsw.model.GameField;

public class GoalTriple implements GoalStrategy{
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        return (int) (points*Math.floor(field.getNumOf(resource)/3));
    }
}
