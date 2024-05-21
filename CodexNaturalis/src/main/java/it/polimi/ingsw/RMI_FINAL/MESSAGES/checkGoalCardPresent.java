package it.polimi.ingsw.RMI_FINAL.MESSAGES;

public class checkGoalCardPresent extends ResponseMessage{
    public boolean isPresent;

    public checkGoalCardPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
