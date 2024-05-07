package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
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
    public Map<Integer,Wrapper> request_to_wrap = new HashMap<>();
    private int port;

    public RmiController(String name, int numPlayer, int port) throws RemoteException {
        this.controller = new GameController(name, numPlayer);
        checkQueue();
        //checkDisconnected();
        playDisconnected();
        this.port = port;
    }

    public int getPort(){
        return port;
    }

    private void checkDisconnected() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);// Controlla i player disconnected ogni 5 secondi
                    for(Player p : controller.getPlayer_list()) {
                        if(p.isDisconnected() && p.getActual_state().getNameState().equals("CHOOSE_GOAL") && p.getGoalCard()==null) {
                            controller.playerChooseGoal(p, 0);
                        }
                        if(p.isDisconnected() && p.getActual_state().getNameState().equals("CHOOSE_SIDE_FIRST_CARD") && !p.isFirstPlaced()) {
                            controller.playerSelectStartingCard(p, true);
                        }
                        if(p.isDisconnected() && (p.getActual_state().getNameState().equals("PLACE_CARD") ||
                                p.getActual_state().getNameState().equals("DRAW_CARD"))){
                            controller.nextStatePlayer();
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }

    @Override
    public synchronized void connect(VirtualViewF client)throws RemoteException{
        this.clients.add(client);
    }
    public void checkQueue() throws RemoteException {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(250); // Controlla le functions ogni 0.25 secondi
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
                break;
            case "createPlayer":
                returns.put(request,createPlayer((String) request_to_wrap.get(request).obj1,
                        (String) request_to_wrap.get(request).obj2, (boolean) request_to_wrap.get(request).obj3));
                break;
           /* case "addPlayer":
                returns.put(request,addPlayer((String) request_to_wrap.get(request).obj1,
                        (String) request_to_wrap.get(request).obj2));
                break;*/
            case "getIndexGame":
                returns.put(request, getController().getGame().getIndex_game());
                break;
                
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
    public synchronized boolean addPlayer(String p_token, String name, VirtualViewF client, boolean isFirst ) throws RemoteException {
        if(controller.getFull() )
        {String error = "\nGame is Full\n";
            token_manager.getTokens().get(p_token).reportError(error);
            return false;}
        createPlayer(p_token, name, isFirst);
        token_manager.getTokens().put(p_token,client);
        controller.checkNumPlayer();
        return true;
    }
    public synchronized boolean addPlayerSocket(String p_token, String name, boolean isFirst ) throws RemoteException {
        if(controller.getFull() )
            return false;
        createPlayer(p_token, name, isFirst);
        token_manager.getSocketTokens().put(p_token, name);
        controller.checkNumPlayer();
        return true;
    }
    @Override
    public void chooseGoal(String token, int index) throws RemoteException {
        controller.playerChooseGoal(token_to_player.get(token), index);
    }

    @Override
    public synchronized void chooseStartingCard(String token, boolean flip) throws RemoteException {
        controller.playerSelectStartingCard(token_to_player.get(token), flip);
    }
    public void addtoQueue(String function, Integer idRequest, Wrapper wrap) throws RemoteException{
        callQueue.add(idRequest);
        request_to_function.put(idRequest, function);
        request_to_wrap.put(idRequest,wrap);
        returns.put(idRequest,"no return");
    }

    public Object getAnswer(Integer idRequest) throws RemoteException{
        Object wait = null;
        do{
            wait = returns.get(idRequest);
        }while(wait.equals("no return"));
        return wait;
    }

    @Override
    public void showStartingCard(String token) throws RemoteException {
        PlayCard card = token_to_player.get(token).getStartingCard();
        token_manager.getTokens().get(token).showCard(card);
    }
    public void showCard(PlayCard card, String token) throws RemoteException{
        token_manager.getTokens().get(token).showCard(card);
    }
    public synchronized void insertCard(String token, int index, int x, int y, boolean flipped) throws RemoteException, ControllerException {
        PlayCard card = token_to_player.get(token).getCardsInHand().get(index);
        token_to_player.get(token).getCardsInHand().get(index).flipCard(flipped);
        controller.statePlaceCard(token_to_player.get(token), index, x, y);
    }
    @Override
    public void showGameField(String token) throws RemoteException {
        GameField field = controller.getField_controller().get(token_to_player.get(token)).getPlayer_field();
        token_manager.getTokens().get(token).showField(field);
        /*
        GameField field = token_to_player.get(token).getGameField();
        token_manager.getTokens().get(token).showField(field);
        */
    }

    public synchronized void peachFromGoldDeck(String token) throws RemoteException{
        controller.playerPeachCardFromGoldDeck(token_to_player.get(token));
    }

    public synchronized void peachFromResourceDeck(String token) throws RemoteException{
        controller.playerPeachCardFromResourcesDeck(token_to_player.get(token));
    }

    public void showPlayerCards(String token) throws RemoteException{
        token_manager.getTokens().get(token).printString("\nLe tue carte: ");
        token_manager.getTokens().get(token).printString("\n1:");
        token_manager.getTokens().get(token).showCard(token_to_player.get(token).getCardsInHand().get(0));
        token_manager.getTokens().get(token).printString("\n2:");
        token_manager.getTokens().get(token).showCard(token_to_player.get(token).getCardsInHand().get(1));
        token_manager.getTokens().get(token).printString("\n3:");
        token_manager.getTokens().get(token).showCard(token_to_player.get(token).getCardsInHand().get(2));
    }

    public synchronized void showCardsInCenter(String token) throws RemoteException{
        token_manager.getTokens().get(token).printString("\nCarte oro: ");
        int i = 1;
        for(PlayCard c : controller.getGame().getCars_in_center().getGold_list()){
            token_manager.getTokens().get(token).printString(String.valueOf(i));
            token_manager.getTokens().get(token).showCard(c);
            i++;
        }
        token_manager.getTokens().get(token).printString("\nCarte risorsa: ");
        for(PlayCard c : controller.getGame().getCars_in_center().getResource_list()){
            token_manager.getTokens().get(token).printString(String.valueOf(i));
            token_manager.getTokens().get(token).showCard(c);
            i++;
        }
    }

    public synchronized void peachFromCardsInCenter(String token, int index) throws RemoteException{
        controller.playerPeachFromCardsInCenter(token_to_player.get(token), index);
    }

    public void getPoints(String token) throws RemoteException{
        token_manager.getTokens().get(token).printString("Totale punti:" + token_to_player.get(token).getPlayerPoints());
    }


    private  void playDisconnected() throws RemoteException {
        new Thread(() -> {
            Player tmp;
            while(true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (this) {
                    for (String s : token_to_player.keySet()) {
                        tmp = token_to_player.get(s);
                        if (tmp.isDisconnected()) {
                            if ( tmp.getActual_state().getNameState().equals("CHOOSE_GOAL") && tmp.getGoalCard()==null ) {
                                try {
                                    chooseGoal(s, 1);
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            if ( tmp.getActual_state().getNameState().equals("CHOOSE_SIDE_FIRST_CARD") && !tmp.isFirstPlaced()) {
                                try {
                                    chooseStartingCard(s, true);
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            if (tmp.getActual_state().getNameState().equals("PLACE_CARD")) controller.nextStatePlayer();
                            if (tmp.getActual_state().getNameState().equals("DRAW_CARD")) controller.nextStatePlayer();
                        }
                    }
                }
                }
        }).start();
    }

    public void getFinalStandings(String token) throws RemoteException{
            int i = 1;
            for(Player p : controller.getPlayer_list()){
                token_manager.getTokens().get(token).printString(i + "- " + p.getName());
                i++;
            }
    }
}



