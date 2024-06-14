package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.util.HashMap;

/**
 * Response message for mapping player IDs to usernames.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a mapping between player IDs (integers) and their corresponding usernames (strings).
 */
public class NumToPlayerResponse extends ResponseMessage {

    private  HashMap<Integer, String> actual_map = new HashMap<>();

    public NumToPlayerResponse(HashMap<Integer, String> map) {
        for (int i : map.keySet()){
            actual_map.put(i,map.get(i));
        }
    }

    /**
     * Updates the client's model with the player ID to username mapping.
     *
     * This method delegates setting the mapping to the client's miniModel object
     */
    @Override
    public  void action(){
        super.client.miniModel.setNumToPlayer(actual_map);
    }

}
