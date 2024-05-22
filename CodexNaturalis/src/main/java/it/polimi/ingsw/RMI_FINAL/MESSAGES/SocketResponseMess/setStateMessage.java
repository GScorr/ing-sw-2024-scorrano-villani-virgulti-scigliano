package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class setStateMessage extends ResponseMessage {
    private String state;

    public setStateMessage(String state) {
        this.state = state;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getState(){
        return state;
    }

    @Override
    public void action() {
        super.client.miniModel.setState(state);
    }
}
