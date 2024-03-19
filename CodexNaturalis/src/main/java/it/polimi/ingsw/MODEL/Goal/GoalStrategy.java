package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

public interface GoalStrategy {
    int totalPoints(GameField field, int points, AnglesEnum resource);

}
