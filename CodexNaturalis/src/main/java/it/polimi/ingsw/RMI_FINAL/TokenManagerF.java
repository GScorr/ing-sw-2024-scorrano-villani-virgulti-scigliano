package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.rmi.Remote;
import java.util.Map;

/**
 * This interface defines methods for managing tokens used in a Remote Method Invocation (RMI) system.
 * Tokens are used for authentication and authorization purposes to control access to remote services.
 */
public interface TokenManagerF extends Remote {
    String generateToken(VirtualViewF clientId);
    public String generateTokenSocket( VirtualView client);
    boolean validateToken(RmiClientF clientId, String token);
    public boolean validateTokenSocket(String name, String token);
    public Map< String, VirtualViewF> getTokens();
    public Map< String, VirtualView> getSocketTokens();
    public void deleteVW(String token);
    public void putPair(String s, VirtualViewF client);
    public VirtualViewF getVal(String tok);
}
