package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.util.HashMap;

public class NumToPlayerResponse extends ResponseMessage {
    private  HashMap<Integer, String> actual_map = new HashMap<>();

    public NumToPlayerResponse(HashMap<Integer, String> map) {
        for (int i : map.keySet()){
            actual_map.put(i,map.get(i));
        }
    }

    @Override
    public  void action(){
        super.client.miniModel.setNumToPlayer(actual_map);
    }
}
