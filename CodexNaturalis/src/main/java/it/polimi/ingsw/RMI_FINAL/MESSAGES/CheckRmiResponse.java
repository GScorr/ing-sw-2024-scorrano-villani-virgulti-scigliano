package it.polimi.ingsw.RMI_FINAL.MESSAGES;

public class CheckRmiResponse extends ResponseMessage{
    public boolean check;

    public CheckRmiResponse(boolean check) {
        this.check = check;
    }


    @Override
    public String getMessage() {
        return message;
    }
}
