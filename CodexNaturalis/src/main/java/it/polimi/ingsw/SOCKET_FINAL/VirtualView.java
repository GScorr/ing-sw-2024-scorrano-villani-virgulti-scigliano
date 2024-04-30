package it.polimi.ingsw.SOCKET_FINAL;

import java.io.Serializable;

public interface VirtualView extends Serializable {
    public void showValue(String message); //in questo caso la chiamo solo se passo un messaggio
    public void reportError(String details);
}
