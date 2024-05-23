package it.polimi.ingsw.RMI_FINAL.MESSAGES;

public class CheckGoldDeckSize extends ResponseMessage{
    public boolean checkSize;

    public CheckGoldDeckSize(boolean check) {
        this.checkSize = check;
    }


    public void action(){
        super.client.checkSizeGoldDeck = checkSize;
        super.client.flag_check = false;
    }


}
