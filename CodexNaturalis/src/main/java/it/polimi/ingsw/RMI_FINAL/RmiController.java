package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public class RmiController implements VirtualRmiController, Serializable {
    public List<VirtualViewF> clients = new ArrayList<>();
    public TokenManagerF token_manager = new TokenManagerImplementF();
    public Map<String, Player> token_to_player = new HashMap<>();

    public GameController controller;
    public Queue<Integer> callQueue = new LinkedList<>();
    public Map<Integer, Object> returns = new HashMap<>();
    public Map<Integer,String> request_to_function = new HashMap<>();

    public RmiController(String name, int numPlayer) throws RemoteException {
        this.controller = new GameController(name, numPlayer);
        checkQueue();
    }

    @Override
    public synchronized void connect(VirtualViewF client)throws RemoteException{
        this.clients.add(client);
    }
    public void checkQueue() throws RemoteException {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(250); // Controlla le functions ogni 0.5 secondi
                    while (!callQueue.isEmpty()) {
                        Integer request = callQueue.poll();
                        executeCall(request);
                    }
                } catch (InterruptedException | RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void executeCall(Integer request) throws RemoteException {
        String function = request_to_function.get(request);
        switch (function) {
            case "getFull":
                returns.put(request,getFull());
        }
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

    @Override
    public void chooseGoal(String token, int index) throws RemoteException {
        controller.playerChooseGoal(token_to_player.get(token), index);
    }

    @Override
    public void chooseStartingCard(String token, boolean flip) throws RemoteException {
        controller.playerSelectStartingCard(token_to_player.get(token), flip);
    }
    public void addtoQueue(String function, Integer idRequest) throws RemoteException{
        callQueue.add(idRequest);
        request_to_function.put(idRequest, function);
        returns.put(idRequest,"no return");
    }

    public Object getAnswer(Integer idRequest) throws RemoteException{
        Object wait = null;
        do{
            wait = returns.get(idRequest);
        }while(wait.equals("no return"));
        return wait;
    }
}



