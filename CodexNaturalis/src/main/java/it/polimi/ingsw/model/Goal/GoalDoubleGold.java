package it.polimi.ingsw.model.Goal;

import it.polimi.ingsw.model.ENUM.AnglesEnum;
import it.polimi.ingsw.model.GameField;

public class GoalDoubleGold implements GoalStrategy{
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        return (int) (points*Math.floor(field.get()/2));
    }
}
