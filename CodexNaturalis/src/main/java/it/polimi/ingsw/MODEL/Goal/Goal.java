package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

import java.io.Serializable;

/**
 *
 * This class defines the properties and behavior of a goal card in the game.
 */
public class Goal implements Serializable {

    private GoalStrategy goalType;
    private AnglesEnum resource;
    private int points;
    private String string;

    public  String front_side_path;
    public  String back_side_path;

    public Goal(GoalStrategy goalType,int points,AnglesEnum resource, String string) {
        this.goalType = goalType;
        this.points = points;
        this.resource=resource;
        this.string = string;
    }

    public void setFront_side_path(String front_side_path) {
        this.front_side_path = front_side_path;
    }

    public void setBack_side_path(String back_side_path) {
        this.back_side_path = back_side_path;
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

    /**
     *
     * This method takes a `GameField` object as input and uses the `goalType` object
     * to calculate the total points earned by the player based on the contents of the field,
     * considering points, resources, and the specific goal type.
     *
     * @param field the player's GameField object
     * @return the total number of points achieved by the player
     */
    public int numPoints(GameField field){
        return goalType.totalPoints(field,points,resource);
    }

    /**
     *
     * @return a string representation of the object
     */
    public String toString(){
        return string;
    }

}
