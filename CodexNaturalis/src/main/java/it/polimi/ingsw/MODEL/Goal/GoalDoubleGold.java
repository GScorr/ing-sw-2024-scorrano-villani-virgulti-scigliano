package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

import java.io.Serializable;

public class GoalDoubleGold implements GoalStrategy, Serializable {
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        return (int) (points*Math.floor(field.getNumOf(resource)/2));
    }
}
