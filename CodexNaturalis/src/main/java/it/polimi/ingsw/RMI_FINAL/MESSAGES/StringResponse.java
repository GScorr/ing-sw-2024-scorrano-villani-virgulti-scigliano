package it.polimi.ingsw.RMI_FINAL.MESSAGES;

public class StringResponse extends ResponseMessage{
    public String string;

    public StringResponse(String string) {
        this.string = string;
    }


    @Override
    public String getMessage() {
        return message;
    }

    public void action(){
        System.out.println(string);
    }
}
