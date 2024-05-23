package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class getTokenResponse extends ResponseMessage {
    private String token;

    public getTokenResponse(String token) {
        this.token = token;
    }

    public void setState(String token){
        this.token = token;
    }

    public String getState(){
        return token;
    }

    @Override
    public void action() {
        super.client.token = token;
        super.client.flag_check = false;
    }
}
