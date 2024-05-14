package it.polimi.ingsw.SOCKET_FINAL.ResponseMessage;

import java.io.IOException;

public class ErrorMessage implements ResponseMessage{
    String err;

    public ErrorMessage(String err) {
        this.err = err;
    }

    @Override
    public void action() throws IOException {

    }

    public String getErr(){return err;}

}
