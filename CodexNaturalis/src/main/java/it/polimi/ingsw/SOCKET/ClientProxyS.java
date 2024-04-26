package it.polimi.ingsw.SOCKET;

import java.io.BufferedWriter;
import java.io.PrintWriter;

public class ClientProxyS implements VirtualViewS{
    final PrintWriter output;

    public ClientProxyS(BufferedWriter output) {
        this.output = new PrintWriter(output);
    }


    public void reportError(String errore){
        output.println("errore");
        output.println(errore);
        output.flush();
    }
    public void showMessage(String message){
        output.println("update_message");
        output.println(message);
        output.flush();
    }
    @Override
    public void showValue(String number) {

    }
}
