package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.rmi.Remote;
import java.util.Map;

public interface TokenManagerF extends Remote {
    String generateToken(VirtualViewF clientId);
    public String generateTokenSocket( VirtualView client);
    boolean validateToken(RmiClientF clientId, String token);
    public boolean validateTokenSocket(String name, String token);
    public Map< String, VirtualViewF> getTokens();
    public Map< String, VirtualView> getSocketTokens();
    public void deleteVW(String token);
}
