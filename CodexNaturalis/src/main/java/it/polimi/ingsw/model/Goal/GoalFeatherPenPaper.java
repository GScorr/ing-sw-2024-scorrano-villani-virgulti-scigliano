package it.polimi.ingsw.model.Goal;

import it.polimi.ingsw.model.ENUM.AnglesEnum;
import it.polimi.ingsw.model.GameField;

public class GoalFeatherPenPaper implements GoalStrategy{
    public int totalPoints(GameField field, int points, AnglesEnum resource){
        int minimumResource;
        minimumResource = Math.min(Math.min(field.getNumOfFeather(), field.getNumOfPen()),field.getNumOfPaper());
        return minimumResource;
    }
}
