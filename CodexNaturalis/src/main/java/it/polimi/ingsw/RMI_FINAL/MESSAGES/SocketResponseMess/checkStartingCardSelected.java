package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class checkStartingCardSelected extends ResponseMessage {
    public boolean isSelected;

    public checkStartingCardSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public void action() {
        super.client.starting_card_is_placed = isSelected;
        super.client.flag_check = false;
    }
}
