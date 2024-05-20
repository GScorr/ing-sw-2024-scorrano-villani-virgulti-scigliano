package it.polimi.ingsw.RMI_FINAL.MESSAGES;
import java.util.HashMap;

public class NumToPlayerResponse extends ResponseMessage{
    private final HashMap<Integer, String> map;

    public NumToPlayerResponse(HashMap<Integer, String> map) {
        this.map = map;
    }

    public HashMap<Integer, String> getMap() {
        return map;
    }

    @Override
    public  void action(){
        super.miniModel.setNumToPlayer(this.map);
    }
}
