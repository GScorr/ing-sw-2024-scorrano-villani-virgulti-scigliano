package it.polimi.ingsw.SOCKET.Token;
/*
import it.polimi.ingsw.RMI_FINAL.RmiClientF;
import it.polimi.ingsw.RMI_FINAL.TokenManagerF;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET.VirtualViewS;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenManagerS{

    public Map<String, VirtualViewS> tokens = new HashMap<>();

    public String generateToken(VirtualViewS clientId) {
        String token = UUID.randomUUID().toString(); // Generate a casual token
        tokens.put(token,clientId); // save client token association
        return token;
    }

    public boolean validateToken(RmiClientF clientId, String token) {
        return tokens.containsKey(clientId) && tokens.get(clientId).equals(token);
    }

    public Map<String, VirtualViewS> getTokens(){
        return this.tokens;
    }

}
*/

