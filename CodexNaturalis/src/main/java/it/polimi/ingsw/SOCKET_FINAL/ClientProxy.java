package it.polimi.ingsw.SOCKET_FINAL;

import java.io.BufferedWriter;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class ClientProxy implements VirtualView {
    final ObjectOutputStream output;

    public ClientProxy(ObjectOutputStream output) {
        this.output = output;
    }


    public void reportError(String errore){
        /*
        output.println("errore");
        output.println(errore);
        output.flush();

         */
    }
    public void showMessage(String message){
        /*
        output.println("update_message");
        output.println(message);
        output.flush();

         */
    }
    @Override
    public void showValue(String number) {
        /*
        output.println("update_number");
        output.println(number);
        output.flush();

         */
    }
}
