package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.GameField;

import java.util.HashMap;

public class NumToPlayerResponse extends ResponseMessage{
    HashMap<Integer, String> map;

    public NumToPlayerResponse(HashMap<Integer, String> map) {
        this.map = map;
    }

    @Override
    public  void action(){
        super.miniModel.setNumToPlayer(map);
    }
}
