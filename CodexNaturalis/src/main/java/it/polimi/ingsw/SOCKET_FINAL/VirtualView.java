package it.polimi.ingsw.SOCKET_FINAL;

public interface VirtualView {
    public void showValue(String message); //in questo caso la chiamo solo se passo un messaggio
    public void reportError(String details);
}
