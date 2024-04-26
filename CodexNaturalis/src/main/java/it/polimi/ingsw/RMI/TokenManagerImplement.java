package it.polimi.ingsw.RMI;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenManagerImplement implements TokenManager, Serializable {

    public Map<String, VirtualView> tokens = new HashMap<>();
    @Override
    public String generateToken(VirtualView clientId) {
        String token = UUID.randomUUID().toString(); // Generate a casual token
        tokens.put(token,clientId); // save client token association
        return token;
    }

    @Override
    public boolean validateToken(RmiClient clientId, String token) {
        return tokens.containsKey(clientId) && tokens.get(clientId).equals(token);
    }

    public Map<String, VirtualView> getTokens(){
        return this.tokens;
    }

}
