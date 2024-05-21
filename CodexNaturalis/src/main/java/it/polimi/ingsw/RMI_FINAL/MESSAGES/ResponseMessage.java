package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MiniModel;

import java.io.Serializable;
import java.rmi.RemoteException;

public abstract class ResponseMessage implements Serializable {
    String message;
    MiniModel miniModel;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public void setMiniModel(MiniModel miniModel){
        this.miniModel = miniModel;
    }
    public void action() throws RemoteException {};

}
