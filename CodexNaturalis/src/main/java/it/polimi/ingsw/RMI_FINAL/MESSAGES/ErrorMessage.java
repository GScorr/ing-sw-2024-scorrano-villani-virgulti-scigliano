package it.polimi.ingsw.RMI_FINAL.MESSAGES;

public class ErrorMessage extends ResponseMessage{
    private String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
