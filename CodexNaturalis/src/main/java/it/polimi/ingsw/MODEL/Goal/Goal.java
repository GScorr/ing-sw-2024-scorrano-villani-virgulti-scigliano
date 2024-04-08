package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

public class Goal {
    private GoalStrategy goalType;
    private AnglesEnum resource;
    private int points;

    public Goal(GoalStrategy goalType,int points,AnglesEnum resource) {
        this.goalType = goalType;
        this.points = points;
        this.resource=resource;
    }

    public void setGoalType(GoalStrategy goalType) {
        this.goalType = goalType;
    }

    public GoalStrategy getGoalType() {
        return goalType;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    //returns the total number of points achieved from the player with the GameField field
    public int numPoints(GameField field){
        return goalType.totalPoints(field,points,resource);
    }
}
