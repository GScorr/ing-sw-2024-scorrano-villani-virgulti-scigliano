package it.polimi.ingsw.RMI_FINAL.MESSAGES;

public class CheckResourcesDeckSize extends ResponseMessage{
    public boolean checkSize;

    public CheckResourcesDeckSize(boolean check) {
        this.checkSize = check;
    }


    @Override
    public String getMessage() {
        return message;
    }
}
