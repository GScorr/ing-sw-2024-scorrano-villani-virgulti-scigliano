package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MODEL.Player.State.EndGame;
import it.polimi.ingsw.MODEL.Player.State.PState;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ErrorMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.GameFieldMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.UpdateMessage;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class GameServer implements VirtualGameServer, Serializable {
    public List<VirtualViewF> clientsRMI = new ArrayList<>();
    public List<VirtualView> clientsSocket = new ArrayList<>();
    public TokenManagerF token_manager = new TokenManagerImplementF();
    public HashMap<Integer, String> num_to_player = new HashMap<>();
    public Map<String, Player> token_to_player = new HashMap<>();

    public GameController controller;
    public Queue<SendFunction> functQueue = new LinkedList<>();
    private final int port;

    private int id = 0;
    //public Map<String, PState> token_to_state_deadline = new HashMap<>();ù

    public ChatIndexManager chatmanager = new ChatIndexManager();


    public Map<String,Integer> token_to_index = new HashMap<>();
    public Map<Integer,String> index_to_token = new HashMap<>();



    //CONSTRUCTOR
    public GameServer(String name, int numPlayer, int port) throws RemoteException {
        this.controller = new GameController(name, numPlayer);
        checkQueue();
        playDisconnected();
        checkDeadline();
        this.port = port;
    }


    //GAME FLOW
    @Override
    public synchronized boolean addPlayer(String p_token, String name, VirtualViewF client, boolean isFirst ) throws IOException {
        if(controller.getFull() )
        {String error = "\nGAME IS FULL\n";
            token_manager.getTokens().get(p_token).reportError(error);
            return false;}
        createPlayer(p_token, name, isFirst);
        token_manager.getTokens().put(p_token,client);
        controller.checkNumPlayer();
        id++;
        token_to_index.put(p_token,id);
        index_to_token.put(id, p_token);
        token_manager.getTokens().get(p_token).insertId(id);
        token_manager.getTokens().get(p_token).insertNumPlayers(getNumPlayersMatch());
        token_manager.getTokens().get(p_token).insertPlayer(token_to_player.get(p_token));
        setAllStates();
        return true;
    }
    public synchronized Player createPlayer(String p_token,String playerName, boolean b) throws RemoteException{
        Player p = controller.createPlayer(playerName,b);
        token_to_player.put(p_token , p);
        return p;
    }
    public synchronized boolean addPlayerSocket(String p_token, String name, VirtualView client,boolean isFirst ) throws IOException {
        if(controller.getFull() )
            return false;
        createPlayer(p_token, name, isFirst);
        token_manager.getSocketTokens().put(p_token, client);
        controller.checkNumPlayer();
        setAllStates();
        clientsSocket.add(client);
        return true;
    }
    @Override
    public void chooseGoal(String token, int index) throws IOException {
        controller.playerChooseGoal(token_to_player.get(token), index);
        setAllStates();
    }

    @Override
    public synchronized void chooseStartingCard(String token, boolean flip) throws IOException {
        controller.playerSelectStartingCard(token_to_player.get(token), flip);
        Integer index = 0;
        if( token_manager.getTokens().containsKey(token) ){token_manager.getTokens().get(token).setCards(token_to_player.get(token).getCardsInHand());}
        if(token_manager.getSocketTokens().containsKey(token)){token_manager.getSocketTokens().get(token).setCards(token_to_player.get(token).getCardsInHand());}
        for (String t : token_to_player.keySet()){
            if( token_manager.getTokens().containsKey(t) ){
            token_manager.getTokens().get(t).setGameField(getGameFields(t));
            }
            if(token_manager.getSocketTokens().containsKey(t)) token_manager.getSocketTokens().get(t).setGameField(getGameFields(t));
            num_to_player.put(index, token_to_player.get(t).getName() );
            index++;
        }
        setAllStates();
    }

    public synchronized void insertCard(String token, int index, int x, int y, boolean flipped) throws RemoteException, ControllerException {
        token_to_player.get(token).getCardsInHand().get(index).flipCard(flipped);
        controller.statePlaceCard(token_to_player.get(token), index, x, y);
    }

    //QUEUE FUNCTIONS
    public void checkQueue() throws RemoteException {
        new Thread(() -> {while (true) {
                try {
                    Thread.sleep(100);
                    while (!functQueue.isEmpty()) {broadcastMessage(functQueue.poll().action(this));}
                }catch (InterruptedException | IOException e) {e.printStackTrace();}
            }}).start();
    }

    private void broadcastMessage(ResponseMessage message) throws IOException {
        for (VirtualViewF c : clientsRMI){ c.pushBack(message);}
        System.out.println("Sono inseriti : "+clientsSocket.size());
        for (VirtualView c : clientsSocket){

            c.pushBack(message);}}
    public void addQueue(SendFunction function) throws RemoteException{functQueue.add(function);}

    //END GAME
    public void getFinalStandings(String token) throws RemoteException{
        int i = 1;
        for(Player p : controller.getPlayer_list()){
            token_manager.getTokens().get(token).printString(i + "- " + p.getName());
            i++;
        }
    }

    //DISCONNECTION
    public void wakeUp(String name, VirtualViewF client){
        for( String s : token_to_player.keySet()){
            if(token_to_player.get(s).getName().equals(name) )  {
                token_to_player.get(s).connect();
                //token_manager.getTokens().remove(s);
                token_manager.getTokens().put(s, client );}}
    }

  private  void playDisconnected() throws RemoteException {
          new Thread(() -> {
            Player tmp;
            while(true) {
                try {
                    Thread.sleep(500);
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
                                    setAllStates();
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            if ( tmp.getActual_state().getNameState().equals("CHOOSE_SIDE_FIRST_CARD") && !tmp.isFirstPlaced()) {
                                try {
                                    chooseStartingCard(s, true);
                                    setAllStates();
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            if (tmp.getActual_state().getNameState().equals("PLACE_CARD")) {
                                controller.nextStatePlayer();
                                try {
                                    setAllStates();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            if (tmp.getActual_state().getNameState().equals("DRAW_CARD")){
                                controller.nextStatePlayer();
                                try {
                                    setAllStates();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
                }
        }).start();
    }

    private void checkDeadline() {
        new Thread(() -> {
            String tokenalive;
            boolean end = true;
            while(end) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(token_to_player.size()>=controller.getGame().getMax_num_player()) {

                    synchronized (this) {
                        try{if (controller.isAlone()) {
                            for (String t : token_to_player.keySet()) {
                                if (!token_to_player.get(t).isDisconnected()) {
                                    tokenalive = t;
                                }
                            }
                            try {
                                broadcastMessage(new UpdateMessage("YOU ARE THE ONLY ONE IN LOBBY: \nCOUNTDOWN STARTED!"));
                                int countdown = 15;
                                while( countdown > 0 && controller.isAlone()) {
                                    broadcastMessage(new UpdateMessage("\b"+countdown + "SECONDS LEFT"));
                                    countdown--;
                                    Thread.sleep(1000);
                                }
                                if ( countdown == 0 ) {
                                    for (String t : token_to_player.keySet()) {
                                        PState end_game = new EndGame(token_to_player.get(t));
                                        token_to_player.get(t).setPlayer_state(end_game);
                                        if ( token_to_player.get(t).isDisconnected() ) broadcastMessage(new UpdateMessage(token_to_player.get(t).getName() + " , YOU ARE THE WINNER DUE TO DISCONNECTIONS!"));
                                        end = false;
                                    }
                                }
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }}catch (RuntimeException e){}
                    }
                }
            }
        }).start();
    }



    //DRAW CARD METHODS
    public synchronized void peachFromGoldDeck(String token) throws RemoteException{controller.playerPeachCardFromGoldDeck(token_to_player.get(token));}
    public synchronized void peachFromResourceDeck(String token) throws RemoteException{controller.playerPeachCardFromResourcesDeck(token_to_player.get(token));}
    public synchronized void peachFromCardsInCenter(String token, int index) throws RemoteException{controller.playerPeachFromCardsInCenter(token_to_player.get(token), index);}



    //GETTER
    public void getPoints(String token) throws IOException {
        if(token_manager.getTokens().get(token)!=null) {
            token_manager.getTokens().get(token).printString("Totale punti:" + token_to_player.get(token).getPlayerPoints());
        }
        else{
            token_manager.getSocketTokens().get(token).printString("Totale punti:" + token_to_player.get(token).getPlayerPoints());
        }
    }
    public synchronized List<VirtualViewF> getClientsRMI() throws RemoteException{return clientsRMI;}
    public synchronized Map<String, Player> getTtoP() throws RemoteException{return token_to_player;}
    public synchronized GameController getController() throws RemoteException{return controller;}
    public  boolean getFull()  throws RemoteException {return controller.getFull();}
    public int getPort(){return port;}

    public int getNumPlayersMatch(){
        return controller.getGame().getMax_num_player();
    }
    public List<GameField> getGameFields(String token) throws RemoteException{
        List<GameField> list = new ArrayList<>();
        list.add(0, token_to_player.get(token).getGameField());
        for ( String t : token_to_player.keySet() ){
            if( !t.equals(token)) list.add(token_to_player.get(t).getGameField());
        }
        return list;
    }

    public synchronized void chattingMoment(int id1, int id2, ChatMessage message) throws RemoteException {
        String t1 = index_to_token.get(id1);
        String t2 = index_to_token.get(id2);
        controller.insertMessageinChat(chatmanager.getChatIndex(id1,id2),message);
        updatePrivateChats(t1, t2, chatmanager.getChatIndex(id1,id2), message);
    }

    public synchronized void chattingGlobal(ChatMessage message) throws RemoteException{
        controller.insertMessageinChat(6,message);
        updatePublicChats(message);
    }

    private void updatePublicChats(ChatMessage message) throws RemoteException{
        for (String t : token_to_player.keySet()){
                token_manager.getTokens().get(t).addChat(6, message);
        }
    }

    private void updatePrivateChats(String token1, String token2, int idx, ChatMessage message) throws RemoteException {
        for (String t : token_to_player.keySet()){
            token_to_index.get(t);
            if(t.equals(token1) || t.equals(token2)){
                token_manager.getTokens().get(t).addChat(idx, message);
            }
        }
    }

    public Map<String, Integer> getToken_to_index() throws RemoteException{
        return token_to_index;
    }

    //SETTER
    private void setAllStates() throws IOException {
        for (String t : token_to_player.keySet()){
            if(token_manager.getTokens().containsKey(t)) {
                token_manager.getTokens().get(t).setState(token_to_player.get(t).getActual_state().getNameState());
                token_manager.getTokens().get(t).setNumToPlayer(num_to_player);
            }
            else if( token_manager.getSocketTokens().containsKey(t)){
                token_manager.getSocketTokens().get(t).setState(token_to_player.get(t).getActual_state().getNameState());
                token_manager.getSocketTokens().get(t).setNumToPlayer(num_to_player);
            }
        }
    }

    //SEND TO CLIENT
    public synchronized void showCardsInCenter(String token) throws IOException {
        if(token_manager.getTokens().containsKey(token)) {
            token_manager.getTokens().get(token).printString("\nCarte oro: ");
        }
        else if(token_manager.getSocketTokens().containsKey(token)){
            token_manager.getSocketTokens().get(token).printString("\nCarte oro: ");
        }
        int i = 1;
        for(PlayCard c : controller.getGame().getCars_in_center().getGold_list()){
            if(token_manager.getTokens().containsKey(token)) {
                token_manager.getTokens().get(token).printString(String.valueOf(i));
                token_manager.getTokens().get(token).showCard(c);
            }
            else{
                token_manager.getSocketTokens().get(token).printString(String.valueOf(i));
                token_manager.getSocketTokens().get(token).showCard(c);
            }
            i++;
        }
        if(token_manager.getTokens().containsKey(token)) {
            token_manager.getTokens().get(token).printString("\nCarte risorsa :");
        }
        else if(token_manager.getSocketTokens().containsKey(token)){
            token_manager.getSocketTokens().get(token).printString("\nCarte risorsa :");
        }

        for(PlayCard c : controller.getGame().getCars_in_center().getResource_list()){
            if(token_manager.getTokens().containsKey(token)) {
                token_manager.getTokens().get(token).printString(String.valueOf(i));
                token_manager.getTokens().get(token).showCard(c);
            }
            else if(token_manager.getSocketTokens().containsKey(token)){
                token_manager.getSocketTokens().get(token).printString(String.valueOf(i));
                token_manager.getSocketTokens().get(token).showCard(c);
            }
            i++;
        }
    }

    @Override
    public void showStartingCard(String token) throws IOException {
        PlayCard card = token_to_player.get(token).getStartingCard();
        if(token_manager.getTokens().get(token)!=null) {
            token_manager.getTokens().get(token).showCard(card);
        }
        else{
            token_manager.getSocketTokens().get(token).showCard(card);
        }
    }



    //CONNECT
    public synchronized void connectSocket(VirtualView clientSocket)throws RemoteException{this.clientsSocket.add(clientSocket);}
    @Override
    public synchronized void connectRMI(VirtualViewF client)throws RemoteException{this.clientsRMI.add(client);}


}



