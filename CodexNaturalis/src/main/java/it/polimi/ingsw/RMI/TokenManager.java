package it.polimi.ingsw.RMI;

import java.rmi.Remote;
import java.util.Map;

public interface TokenManager extends Remote {
    String generateToken(VirtualView clientId);
    boolean validateToken(RmiClient clientId, String token);
    public Map< String, VirtualView> getTokens();
}
