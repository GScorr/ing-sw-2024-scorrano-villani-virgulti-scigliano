package it.polimi.ingsw.RMI_FINAL.MESSAGES;

/**
 * Class for managing state information.
 *
 * This class provides methods for setting and retrieving a state string,
 * which could be used to track the current game phase, player turn, or other
 * relevant server-side information.
 *
 */
public class StateMessage {

    private String state;

    public void setState(String state){
        this.state = state;
    }

    public String getState(){
        return state;
    }

}
