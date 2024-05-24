package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenManagerImplementF implements TokenManagerF, Serializable {

    public Map<String, VirtualViewF> tokens = new HashMap<>();
    public  Map<String, VirtualView> Socket_tokens = new HashMap<>();
    @Override
    public String generateToken(VirtualViewF clientId) {
        String token = UUID.randomUUID().toString(); // Generate a casual token
        tokens.put(token,clientId); // save client token association
        return token;
    }

    //Socket usage
    public String generateTokenSocket(VirtualView client){
        String token = UUID.randomUUID().toString(); // Generate a casual token
        Socket_tokens.put(token,client); // save client token association
        return token;
    }

    @Override
    public boolean validateToken(RmiClientF clientId, String token) {
        return tokens.containsKey(clientId) && tokens.get(clientId).equals(token);
    }

    public boolean validateTokenSocket(String name, String token){
        return Socket_tokens.containsKey(name) && Socket_tokens.get(token).equals(name);
    }

    public void deleteVW(String token){
        if( tokens.containsKey(token) ) {tokens.remove(token);}
        else if ( Socket_tokens.containsKey(token) ) { Socket_tokens.remove(token);}
    }

    public void putPair(String s, VirtualViewF client){
        tokens.put(s,client);
    }
    public Map<String, VirtualViewF> getTokens(){
        return this.tokens;
    }
    public Map<String,VirtualView> getSocketTokens(){return this.Socket_tokens;
    }
}
