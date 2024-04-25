package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RmiController implements VirtualRmiController, Serializable {
    public List<VirtualViewF> clients = new ArrayList<>();
    public TokenManagerF token_manager = new TokenManagerImplementF();
    public Map<String, Player> token_to_player = new HashMap<>();

    public GameController controller;

    public RmiController(String name, int numPlayer) {
        this.controller = new GameController(name, numPlayer);
    }

    @Override
    public synchronized void connect(VirtualViewF client)throws RemoteException{
        this.clients.add(client);
    }

    public  boolean getFull()  throws RemoteException {
        return controller.getFull();
    }

    public synchronized List<VirtualViewF> getClients() throws RemoteException{
        return clients;
    }


    public synchronized Map<String, Player> getTtoP() throws RemoteException{
        return token_to_player;
    }

    public synchronized GameController getController() throws RemoteException{
        return controller;
    }

    public synchronized Player createPlayer(String p_token,String playerName, boolean b) throws RemoteException{
        Player p = controller.createPlayer(playerName,b);
        token_to_player.put(p_token , p);
        return p;
    }

    @Override
    public synchronized boolean addPlayer(String p_token, String name) throws RemoteException {
        if(controller.getFull() )
        {String error = "\nGame is Full\n";
            token_manager.getTokens().get(p_token).reportError(error);
            return false;}
        createPlayer(p_token, name, false);
        controller.checkNumPlayer();
        return true;
    }
    

}



