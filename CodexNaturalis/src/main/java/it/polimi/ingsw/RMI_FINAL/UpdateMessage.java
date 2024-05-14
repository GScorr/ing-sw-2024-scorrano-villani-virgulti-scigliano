package it.polimi.ingsw.RMI_FINAL;

public class UpdateMessage extends ResponseMessage{
    private String message;
    public UpdateMessage(String string) {
        this.message = string;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
