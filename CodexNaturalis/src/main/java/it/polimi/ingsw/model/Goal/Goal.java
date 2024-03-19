package it.polimi.ingsw.model.Goal;

public class Goal {
    private GoalStrategy goalType;
    private int points;

    public Goal(GoalStrategy goalType,int points) {
        this.goalType = goalType;
        this.points = points;
    }

    public void setGoalType(GoalStrategy goalType) {
        this.goalType = goalType;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public GoalStrategy getGoalType() {
        return goalType;
    }

    public int numPoints(){
        return goalType.totalPoints(points);
    }
}
