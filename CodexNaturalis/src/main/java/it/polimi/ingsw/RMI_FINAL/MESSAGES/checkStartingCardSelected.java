package it.polimi.ingsw.RMI_FINAL.MESSAGES;

public class checkStartingCardSelected extends ResponseMessage{
    public boolean isSelected;

    public checkStartingCardSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
