package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MODEL.Player.State.EndGame;
import it.polimi.ingsw.MODEL.Player.State.PState;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.UpdateMessage;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NoSuchObjectException;
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
    public ChatIndexManager chatmanager = new ChatIndexManager();
    public Map<String,Integer> token_to_index = new HashMap<>();
    public Map<Integer,String> index_to_token = new HashMap<>();
    public HashMap<Integer,String> index_to_name = new HashMap<>();
    private Common_Server server;
    private int id_game_server = 0;


    //CONSTRUCTOR
    public GameServer(String name, int numPlayer, int port, Common_Server commonServer) throws IOException {
        this.controller = new GameController(name, numPlayer);
        checkQueue();
        playDisconnected();
        checkDeadline();
        this.port = port;
        this.server = commonServer;
    }


    //GAME FLOW
    @Override
    public synchronized boolean addPlayer(String p_token, String name, VirtualViewF client, boolean isFirst ) throws IOException, InterruptedException {
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
        index_to_name.put(id,name);
        token_manager.getTokens().get(p_token).insertId(id);
        token_manager.getTokens().get(p_token).insertNumPlayers(getNumPlayersMatch());
        token_manager.getTokens().get(p_token).insertPlayer(token_to_player.get(p_token));
        setAllStates();
        //--riga aggiunta da Fra
        clientsRMI.add(client);
        return true;
    }
    public synchronized Player createPlayer(String p_token,String playerName, boolean b) throws IOException{
        Player p = controller.createPlayer(playerName,b);
        token_to_player.put(p_token , p);
        return p;
    }

    //questa funzione non dovrebbe essere più usata
    public synchronized boolean addPlayerSocket(String p_token, String name, VirtualView client,boolean isFirst ) throws IOException, InterruptedException {
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
    public void chooseGoal(String token, int index) throws IOException, InterruptedException {
        controller.playerChooseGoal(token_to_player.get(token), index);
        setAllStates();
    }

    @Override
    public synchronized void chooseStartingCard(String token, boolean flip) throws IOException, InterruptedException {
        controller.playerSelectStartingCard(token_to_player.get(token), flip);
        Integer index = 0;
        if( token_manager.getTokens().containsKey(token) ){token_manager.getTokens().get(token).setCards(token_to_player.get(token).getCardsInHand());}
        if(token_manager.getSocketTokens().containsKey(token)){token_manager.getSocketTokens().get(token).setCards(token_to_player.get(token).getCardsInHand());}
        for (String t : token_to_player.keySet()){
            if( token_manager.getTokens().containsKey(t) ){
                token_manager.getTokens().get(t).setGameField(getGameFields(t));
                //token_manager.getTokens().get(t).insertId(index);
            }
            num_to_player.put(index, token_to_player.get(t).getName() );
            index++;
        }
        setAllStates();
    }

    public synchronized void insertCard(String token, int index, int x, int y, boolean flipped) throws IOException, ControllerException {
        token_to_player.get(token).getCardsInHand().get(index).flipCard(flipped);
        controller.statePlaceCard(token_to_player.get(token), index, x, y);
        token_manager.getVal(token).setCards( token_to_player.get(token).getCardsInHand() );
    }

    //QUEUE FUNCTIONS
    public void checkQueue() throws IOException {
        new Thread(() -> {while (true) {
                try {
                    Thread.sleep(100);
                    while (!functQueue.isEmpty()) {broadcastMessage(functQueue.poll().action(this));}
                }catch (InterruptedException | IOException e) {e.printStackTrace();}
            }}).start();
    }

    private void broadcastMessage(ResponseMessage message) throws IOException {
        for (VirtualViewF c : clientsRMI){
            c.pushBack(message);}
    }
    public void addQueue(SendFunction function) {functQueue.add(function);}

    //END GAME
    public void getFinalStandings(String token) throws IOException {
        int i = 1;
        for(Player p : controller.getPlayer_list()){
            token_manager.getTokens().get(token).printString(i + "- " + p.getName());
            i++;
        }
        endConnection();
    }

    //DISCONNECTION
    public void wakeUp(String name, VirtualViewF client) throws IOException {
        for( String s : token_to_player.keySet()){
            String token = s;
            if( token_to_player.get(token).getName().equals(name) )  {
                token_manager.putPair(token,client);
                System.out.println("Reconnect");
                token_to_player.get(token).connect();
                clientsRMI.add(client);
                token_manager.getVal(token).insertId(id);
                token_manager.getVal(token).insertNumPlayers(getNumPlayersMatch());
                token_manager.getVal(token).insertPlayer(token_to_player.get(token));
                //token_manager.getVal(token).setNumToPlayer(index_to_name);
                }
        }
    }

  private  void playDisconnected() throws IOException {
          new Thread(() -> {
            Player tmp;
            while(true) {
                try {
                    Thread.sleep(400);
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
                                } catch (IOException | InterruptedException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            if ( tmp.getActual_state().getNameState().equals("CHOOSE_SIDE_FIRST_CARD") && !tmp.isFirstPlaced()) {
                                try {
                                    chooseStartingCard(s, true);
                                    setAllStates();
                                } catch (IOException | InterruptedException e) {}
                            }
                            if (tmp.getActual_state().getNameState().equals("PLACE_CARD")) {
                                controller.nextStatePlayer();
                                try {
                                    setAllStates();
                                } catch (IOException | InterruptedException e) {}
                            }
                            if (tmp.getActual_state().getNameState().equals("DRAW_CARD")){
                                controller.nextStatePlayer();
                                try {
                                    setAllStates();
                                } catch (IOException | InterruptedException e) {}
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
                if(token_to_player.size() >= controller.getGame().getMax_num_player()) {

                        int last_man_standing = 0;
                        try{if (controller.isAlone()) {
                            checkEndDisconnect();
                            try {
                                int countdown = 45;
                                broadcastMessage(new UpdateMessage("YOU ARE THE ONLY ONE IN LOBBY: \nCOUNTDOWN STARTED! " + controller.isAlone() + " " + countdown));
                                Thread.sleep(1500);
                                while( countdown > 0 && controller.isAlone()) {
                                    broadcastMessage(new UpdateMessage(countdown + " SECONDS LEFT"));
                                    checkEndDisconnect();
                                    countdown--;
                                    Thread.sleep(1000);
                                }
                                if ( countdown == 0 ) {
                                    for (String t : token_to_player.keySet()) {
                                        PState end_game = new EndGame(token_to_player.get(t));
                                        token_to_player.get(t).setPlayer_state(end_game);
                                        if ( !token_to_player.get(t).isDisconnected() ) broadcastMessage(new UpdateMessage(token_to_player.get(t).getName() + " , YOU ARE THE WINNER DUE TO DISCONNECTIONS!"));
                                        end = false;
                                        endConnection();
                                    }
                                }else{
                                    setAllStates();
                                    broadcastMessage(new UpdateMessage("PLAYER RECONNECTED CONTINUE YOUR GAME")); }
                            } catch (IOException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }}catch (RuntimeException ignored){} catch (NoSuchObjectException e) {
                            throw new RuntimeException(e);
                        }

                }
            }
        }).start();
    }



    //DRAW CARD METHODS
    public synchronized void peachFromGoldDeck(String token) throws IOException{controller.playerPeachCardFromGoldDeck(token_to_player.get(token));}
    public synchronized void peachFromResourceDeck(String token) throws IOException{controller.playerPeachCardFromResourcesDeck(token_to_player.get(token));}
    public synchronized void peachFromCardsInCenter(String token, int index) throws IOException{controller.playerPeachFromCardsInCenter(token_to_player.get(token), index);}



    //GETTER
    public void getPoints(String token) throws IOException {
        if(token_manager.getTokens().get(token)!=null) {
            token_manager.getTokens().get(token).printString("  TOTAL POINTS    :" + token_to_player.get(token).getPlayerPoints());
        }
    }
    public synchronized List<VirtualViewF> getClientsRMI() throws IOException{return clientsRMI;}
    public synchronized Map<String, Player> getTtoP() throws IOException{return token_to_player;}
    public synchronized GameController getController() throws IOException{return controller;}
    public  boolean getFull()  throws IOException {return controller.getFull();}
    public int getPort(){return port;}

    public int getNumPlayersMatch(){
        return controller.getGame().getMax_num_player();
    }
    public List<GameField> getGameFields(String token) throws IOException{
        List<GameField> list = new ArrayList<>();
        for ( String t : token_to_player.keySet() )
            list.add(token_to_player.get(t).getGameField());

        return list;
    }

    public synchronized void chattingMoment(int id1, int id2, ChatMessage message) throws IOException {
        String t1 = index_to_token.get(id1);
        String t2 = index_to_token.get(id2);
        controller.insertMessageinChat(chatmanager.getChatIndex(id1,id2),message);
        updatePrivateChats(t1, t2, chatmanager.getChatIndex(id1,id2), message);
    }

    public synchronized void chattingGlobal(ChatMessage message) throws IOException {
        controller.insertMessageinChat(6,message);
        updatePublicChats(message);
    }

    private void updatePublicChats(ChatMessage message) throws IOException {
        for (String t : token_to_player.keySet()){
                token_manager.getTokens().get(t).addChat(6, message);
        }
    }

    private void updatePrivateChats(String token1, String token2, int idx, ChatMessage message) throws IOException {
        for (String t : token_to_player.keySet()){
            token_to_index.get(t);
            if(t.equals(token1) || t.equals(token2)){
                token_manager.getTokens().get(t).addChat(idx, message);
            }
        }
    }

    public Map<String, Integer> getToken_to_index() throws IOException{
        return token_to_index;
    }

    //SETTER
    private void setAllStates() throws IOException, InterruptedException {
        CentralEnum topres;
        CentralEnum topgold;
        for (String t : token_to_player.keySet()){
            if( token_manager.getTokens().containsKey(t) && token_to_player.containsKey(t) ) {
                token_manager.getVal(t).setState(token_to_player.get(t).getActual_state().getNameState());
                token_manager.getVal(t).setNumToPlayer(index_to_name);
                token_manager.getVal(t).setCenterCards(controller.getGame().getCars_in_center(),  controller.getGame().getResources_deck().cards.getFirst() , controller.getGame().getGold_deck().cards.getFirst());
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

    public PlayCard showStartingCardGUI(String token) throws IOException{
        return token_to_player.get(token).getStartingCard();
    }

    public void checkEndDisconnect() throws NoSuchObjectException {
        int last_man_standing = 0;
        for(String s: token_to_player.keySet() ) {if( !token_to_player.get(s).isDisconnected() ) last_man_standing++;  }
        if ( last_man_standing == 0 ) {
            server.removeGameServer(this);
        }
    }

    public void endConnection() throws NoSuchObjectException {
            server.removeGameServer(this);
    }

    //CONNECT
    public synchronized void connectSocket(VirtualViewF clientSocket)throws IOException{this.clientsRMI.add(clientSocket);}
    @Override
    public synchronized void connectRMI(VirtualViewF client)throws IOException{this.clientsRMI.add(client);}


}



