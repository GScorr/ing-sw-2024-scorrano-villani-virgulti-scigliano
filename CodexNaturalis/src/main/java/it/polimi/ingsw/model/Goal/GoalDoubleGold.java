package it.polimi.ingsw.model.Goal;

import it.polimi.ingsw.model.GameField;

public class GoalDoubleGold implements GoalStrategy{
    public int totalPoints(GameField field, int points) {
        return (int) (points*Math.floor(field.getNumOfFeather()/2));
    }
}
