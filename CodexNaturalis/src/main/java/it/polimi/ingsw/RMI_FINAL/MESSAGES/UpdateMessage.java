package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.GameField;

public class UpdateMessage extends ResponseMessage{
    private String message;

    private GameField gamefield;
    public UpdateMessage(String string) {
        this.message = string;
    }
    public UpdateMessage(GameField gamefield) {
        this.gamefield = gamefield;
    }



    @Override
    public void action(){
        System.out.println(message);
    }
}
