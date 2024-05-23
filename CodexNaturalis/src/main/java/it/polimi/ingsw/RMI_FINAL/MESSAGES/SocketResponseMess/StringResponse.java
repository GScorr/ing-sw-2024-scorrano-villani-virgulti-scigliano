package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class StringResponse extends ResponseMessage {
    public String string;

    public StringResponse(String string) {
        this.string = string;
    }



    public void action(){
        System.out.println(string);
    }
}
