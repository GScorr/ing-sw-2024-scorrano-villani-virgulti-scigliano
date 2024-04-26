package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

import java.io.Serializable;

public class Goal implements Serializable {
    private GoalStrategy goalType;
    private AnglesEnum resource;
    private int points;
    private String string;

    public Goal(GoalStrategy goalType,int points,AnglesEnum resource, String string) {
        this.goalType = goalType;
        this.points = points;
        this.resource=resource;
        this.string = string;
    }

    public void setGoalType(GoalStrategy goalType) {
        this.goalType = goalType;
    }

    public GoalStrategy getGoalType() {
        return goalType;
    }

    public AnglesEnum getResource() {
        return resource;
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
    public String toString(){
        return string;
    }
}
