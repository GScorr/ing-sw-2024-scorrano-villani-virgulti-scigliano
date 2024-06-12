package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for checking starting card selection.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * to a client's query about whether they have selected their starting card.
 *
 */
public class checkStartingCardSelected extends ResponseMessage {
    public boolean isSelected;

    public checkStartingCardSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * Updates the client's state with the starting card selection information.
     *
     * This method sets the `starting_card_is_placed` flag in the client object
     * based on the received information (true if a card is selected, false otherwise).
     * It also resets the `flag_check` flag.
     */
    @Override
    public void action() {
        super.client.starting_card_is_placed = isSelected;
        super.client.flag_check = false;
    }
}
