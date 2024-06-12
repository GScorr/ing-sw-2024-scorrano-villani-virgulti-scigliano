package it.polimi.ingsw.RMI_FINAL.MESSAGES;

/**
 * Response message for sending an error message to the client.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing an error message string. The client can display this message
 * to the user to inform them about an issue.
 *
 */
public class ErrorMessage extends ResponseMessage{
    private String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    /**
     * Prints the error message to the console.
     *
     */
    @Override
    public void action(){
        System.out.println(message);
    }

}
