package it.polimi.ingsw.model.Goal;

import it.polimi.ingsw.model.ENUM.AnglesEnum;
import it.polimi.ingsw.model.GameField;

public interface GoalStrategy {
    int totalPoints(GameField field, int points, AnglesEnum resource);

}
