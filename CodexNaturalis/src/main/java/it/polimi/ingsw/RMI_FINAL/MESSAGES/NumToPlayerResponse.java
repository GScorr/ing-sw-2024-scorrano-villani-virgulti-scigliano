package it.polimi.ingsw.RMI_FINAL.MESSAGES;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;

import java.util.HashMap;

public class NumToPlayerResponse extends ResponseMessage{
    private HashMap<Integer, String> map;
    public NumToPlayerResponse(HashMap<Integer, String> map) {
        this.map = map;
        for( Integer i : map.keySet() ){
            System.out.println("-" + i + " NOMEEEE:  " + map.get(i) );
        }
    }

    public HashMap<Integer, String> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer,String> map){ this.map = map;}

    @Override
    public  void action(){
        super.miniModel.setNumToPlayer(map);
    }
}
