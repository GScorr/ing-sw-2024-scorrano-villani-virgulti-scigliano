package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import java.io.Serializable;

public abstract class ResponseMessage implements Serializable {
    String message;

    public String getMessage() {
        return message;
    }


}
