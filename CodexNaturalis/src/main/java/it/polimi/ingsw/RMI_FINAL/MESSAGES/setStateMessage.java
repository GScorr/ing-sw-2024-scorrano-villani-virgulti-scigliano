package it.polimi.ingsw.RMI_FINAL.MESSAGES;

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
        super.miniModel.setState(state);
    }
}
