package it.polimi.ingsw.RMI_FINAL;

import java.rmi.Remote;
import java.util.Map;

public interface TokenManagerF extends Remote {
    String generateToken(VirtualViewF clientId);
    boolean validateToken(RmiClientF clientId, String token);
    public Map< String, VirtualViewF> getTokens();
}
