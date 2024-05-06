package it.polimi.ingsw.SOCKET_FINAL.TokenManager;

import it.polimi.ingsw.RMI_FINAL.RmiClientF;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

 public class TokenManager implements Serializable {

    public TokenManager() {
    }

    public Map<String, String> tokens = new HashMap<>();

    public String generateToken(String name) {
        String token = UUID.randomUUID().toString(); // Generate a casual token
        tokens.put(token,name); // save client token association
        return token;
    }

    public boolean validateToken(String token) {
        return tokens.containsKey(token);
    }


}


