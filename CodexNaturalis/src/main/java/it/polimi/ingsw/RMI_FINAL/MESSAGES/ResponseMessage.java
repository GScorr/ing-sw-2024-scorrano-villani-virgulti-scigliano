package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MiniModel;

import java.io.Serializable;

public abstract class ResponseMessage implements Serializable {
    String message;
    MiniModel miniModel;
    public String getMessage() {
        return message;
    }
    public void setMiniModel(MiniModel miniModel){
        this.miniModel = miniModel;
    }
    public void action(){};

}
