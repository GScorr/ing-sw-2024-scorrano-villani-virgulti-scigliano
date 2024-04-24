package it.polimi.ingsw.RMI_FINAL;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenManagerImplementF implements TokenManagerF, Serializable {

    public Map<String, VirtualViewF> tokens = new HashMap<>();
    @Override
    public String generateToken(VirtualViewF clientId) {
        String token = UUID.randomUUID().toString(); // Generate a casual token
        tokens.put(token,clientId); // save client token association
        return token;
    }

    @Override
    public boolean validateToken(RmiClientF clientId, String token) {
        return tokens.containsKey(clientId) && tokens.get(clientId).equals(token);
    }

    public Map<String, VirtualViewF> getTokens(){
        return this.tokens;
    }

}
