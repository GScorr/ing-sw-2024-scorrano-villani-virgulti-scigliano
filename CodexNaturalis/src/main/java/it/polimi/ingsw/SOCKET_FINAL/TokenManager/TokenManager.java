package it.polimi.ingsw.SOCKET_FINAL.TokenManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
    todo
        da eliminare i metodi che non vengono usati
 */

/**
 * A class responsible for managing client tokens.
 *
 */
 public class TokenManager implements Serializable {

    /**
     * Creates a new TokenManager object.
     */
    public TokenManager() {
    }

    public Map<String, String> tokens = new HashMap<>();

    /**
     * Generates a unique token for a client and associates it with the client's name.
     *
     * @param name the client's name
     * @return the generated token
     */
    public String generateToken(String name) {
        String token = UUID.randomUUID().toString(); // Generate a casual token
        tokens.put(token,name); // save client token association
        return token;
    }

    /**
     * Validates a client token by checking if it exists in the token map.
     *
     * @param token the client token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        return tokens.containsKey(token);
    }
}


