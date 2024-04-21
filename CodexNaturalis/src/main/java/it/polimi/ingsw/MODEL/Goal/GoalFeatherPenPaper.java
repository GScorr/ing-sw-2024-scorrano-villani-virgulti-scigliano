package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

import java.io.Serializable;

public class GoalFeatherPenPaper implements GoalStrategy, Serializable {
    public int totalPoints(GameField field, int points, AnglesEnum resource){
        int minimumResource;
        minimumResource = Math.min(Math.min(field.getNumOfFeather(), field.getNumOfPen()),field.getNumOfPaper());
        return points*minimumResource;
    }
}
