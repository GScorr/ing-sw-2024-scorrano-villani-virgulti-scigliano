package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
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

/**
 * This class represents a game server for a multiplayer game.
 * It manages players, the game state, and communication between clients.
 */
public class GameServer implements VirtualGameServer, Serializable {

    public List<VirtualViewF> clientsRMI = new ArrayList<>();
    public List<VirtualView> clientsSocket = new ArrayList<>();
    public TokenManagerF token_manager = new TokenManagerImplementF();
    public HashMap<Integer, String> num_to_player = new HashMap<>();
    public Map<String, Player> token_to_player = new HashMap<>();
    public GameController controller;
    public Queue<SendFunction> functQueue = new LinkedList<>();
    private final int port;
    private boolean called = false;
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


    /**
     * Adds a player to the game server.
     *
     * @param p_token The player's token.
     * @param name The player's name.
     * @param client The client's virtual view.
     * @param isFirst True if the player is the first player to join, false otherwise.
     * @return True if the player was added successfully, false otherwise.
     * @throws IOException If there is an IO error.
     * @throws InterruptedException If the thread is interrupted.
     */
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

    /**
     * Creates a player and adds it to the game.
     *
     * @param p_token The player's token.
     * @param playerName The player's name.
     * @param b A flag indicating whether the player is the first player to join.
     * @return The created player object.
     * @throws IOException If there is an IO error.
     */
    public synchronized Player createPlayer(String p_token,String playerName, boolean b) throws IOException{
        Player p = controller.createPlayer(playerName,b);
        token_to_player.put(p_token , p);
        return p;
    }

    /**
     * Adds a player to the game server using a socket connection.
     *
     * @param p_token The player's token.
     * @param name The player's name.
     * @param client The client's virtual view.
     * @param isFirst True if the player is the first player to join, false otherwise.
     * @return True if the player was added successfully, false otherwise (if the game is already full).
     * @throws IOException If there is an IO error.
     * @throws InterruptedException If the thread is interrupted.
     */
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

    /**
     * Sets the chosen goal card for a player.
     *
     * @param token The player's token.
     * @param index The index of the chosen goal card in the player's hand (1-based indexing).
     * @throws IOException If there is an IO error.
     * @throws InterruptedException If the thread is interrupted.
     */
    @Override
    public void chooseGoal(String token, int index) throws IOException, InterruptedException {
        controller.playerChooseGoal(token_to_player.get(token), index);
        setAllStates();
    }

    /**
     * Sets the starting card for a player and updates the game state for all players.
     *
     * @param token The player's token.
     * @param flip True if the card should be flipped, false otherwise.
     * @throws IOException If there is an IO error.
     * @throws InterruptedException If the thread is interrupted.
     */
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

    /**
     * Inserts a card into the game field for a player.
     *
     * @param token The player's token.
     * @param index The index of the card in the player's hand (0-based indexing).
     * @param x The x coordinate of the card on the game field.
     * @param y The y coordinate of the card on the game field.
     * @param flipped True if the card should be flipped before placing, false otherwise.
     * @throws IOException If there is an IO error.
     * @throws ControllerException If there is a controller exception (e.g., invalid card placement).
     */
    public synchronized void insertCard(String token, int index, int x, int y, boolean flipped) throws IOException, ControllerException {
        token_to_player.get(token).getCardsInHand().get(index).flipCard(flipped);
        controller.statePlaceCard(token_to_player.get(token), index, x, y);
        token_manager.getVal(token).setCards( token_to_player.get(token).getCardsInHand() );
    }

    /**
     * Starts a thread to continuously process messages from a queue.
     *
     * @throws IOException If there is an IO error.
     */
    public void checkQueue() throws IOException {
        new Thread(() -> {while (true) {
                try {
                    Thread.sleep(100);
                    while (!functQueue.isEmpty()) {broadcastMessage(functQueue.poll().action(this));}
                }catch (InterruptedException | IOException e) {e.printStackTrace();}
            }}).start();
    }

    /**
     * Broadcasts a message to all connected clients.
     *
     * @param message The message to broadcast.
     * @throws IOException If there is an IO error.
     */
    private void broadcastMessage(ResponseMessage message) throws IOException {
        for (VirtualViewF c : clientsRMI){
            c.pushBack(message);}
    }

    /**
     * Adds a function to a queue for later processing.
     *
     * @param function The function to be added.
     */
    public void addQueue(SendFunction function) {
        functQueue.add(function);
    }

    /**
     * Sends the final standings of the game to a player.
     *
     * @param token The player's token.
     * @throws IOException If there is an IO error.
     */
    public synchronized void getFinalStandings(String token) throws IOException {

        if( !called ){
            called = true;

            for( VirtualViewF vw : clientsRMI ){
                int i = 1;
                vw.printString("\n[END OF THE GAME]!\nFINAL SCORES:\n" );
                    for(Player p : controller.getPlayer_list()){
                        vw.printString(i + "- " + p.getName() );
                        i++;
                    }
            }
            endConnection();
        }
    }

    /**
     * Attempts to reconnect a previously disconnected player.
     *
     * @param name The name of the player to reconnect.
     * @param client The client's virtual view object.
     * @throws IOException If there is an IO error.
     * @throws InterruptedException If the thread is interrupted.
     */
    public void wakeUp(String name, VirtualViewF client) throws IOException, InterruptedException {
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
                token_manager.getVal(token).setCards( token_to_player.get(token).getCardsInHand() );
                setAllStates();
                //token_manager.getVal(token).setNumToPlayer(index_to_name);
                }
        }
    }

    /**
     * Starts a thread to handle disconnected players during gameplay.
     *
     * @throws IOException If there is an IO error.
     */
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
                                } catch (IOException | InterruptedException  e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            if ( tmp.getActual_state().getNameState().equals("CHOOSE_SIDE_FIRST_CARD") && !tmp.isFirstPlaced()) {
                                try {
                                    chooseStartingCard(s, true);
                                    setAllStates();
                                } catch (IOException | InterruptedException  e) {}
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
                                } catch (IOException | InterruptedException  e) {}
                            }
                        }
                    }
                }
                }
        }).start();
    }

    /**
     * Starts a thread to check for deadlines and handle potential game end scenarios.
     */
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
                            } catch (IOException | InterruptedException  e) {
                                throw new RuntimeException(e);
                            }
                        }}catch (RuntimeException ignored){} catch (NoSuchObjectException e) {
                            throw new RuntimeException(e);
                        }

                }
            }
        }).start();
    }

    /**
     * Draw cards methods
     */
    public synchronized void peachFromGoldDeck(String token) throws IOException, InterruptedException {
        controller.playerPeachCardFromGoldDeck(token_to_player.get(token));
        setAllStates();
    }
    public synchronized void peachFromResourceDeck(String token) throws IOException, InterruptedException {controller.playerPeachCardFromResourcesDeck(token_to_player.get(token));
        setAllStates();
    }
    public synchronized void peachFromCardsInCenter(String token, int index) throws IOException, InterruptedException {controller.playerPeachFromCardsInCenter(token_to_player.get(token), index);
        setAllStates();
    }

    /**
     * Getter
     */
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

    public List<GameField> getGameFields() throws IOException{
        List<GameField> list = new ArrayList<>();
        for ( String t : token_to_player.keySet() )
            list.add(token_to_player.get(t).getGameField());

        return list;
    }

    /**
     * Handles private chat messages between two players.
     *
     * @param id1 The ID of the first player.
     * @param id2 The ID of the second player.
     * @param message The chat message to be sent.
     * @throws IOException If there is an IO error.
     */
    public synchronized void chattingMoment(int id1, int id2, ChatMessage message) throws IOException {
        String t1 = index_to_token.get(id1);
        String t2 = index_to_token.get(id2);
        controller.insertMessageinChat(chatmanager.getChatIndex(id1,id2),message);
        updatePrivateChats(t1, t2, chatmanager.getChatIndex(id1,id2), message);
        token_manager.getVal(t2).printString( "                                 YOU RECEIVED A MESSAGE FROM [ " +  token_to_player.get(t1).getName() + " ]");
    }

    /**
     * Handles a global chat message sent to all players.
     *
     * @param message The chat message to be broadcast.
     * @throws IOException If there is an IO error.
     */
    public synchronized void chattingGlobal(ChatMessage message) throws IOException {
        controller.insertMessageinChat(6,message);
        updatePublicChats(message);
    }

    /**
     * Updates the public chat state for all players after a global chat message is received.
     *
     * @param message The chat message received.
     * @throws IOException If there is an IO error.
     */
    private void updatePublicChats(ChatMessage message) throws IOException {
        for (String t : token_to_player.keySet()){
                token_manager.getTokens().get(t).addChat(6, message);
                if( !message.player.getName().equals(token_to_player.get(t).getName()) ) token_manager.getVal(t).printString( "                                 YOU RECEIVED A MESSAGE IN GLOBAL CHAT");
        }
    }

    /**
     * Updates the private chat state for the two players involved in a private chat message.
     *
     * @param token1 The token of the first player.
     * @param token2 The token of the second player.
     * @param idx The chat history index for the private chat between the two players.
     * @param message The chat message received.
     * @throws IOException If there is an IO error.
     */
    private void updatePrivateChats(String token1, String token2, int idx, ChatMessage message) throws IOException {
        for (String t : token_to_player.keySet()){
            token_to_index.get(t);
            if(t.equals(token1) || t.equals(token2)){
                token_manager.getTokens().get(t).addChat(idx, message);
            }
        }
    }

    /**
     * Returns a mapping of player tokens to their corresponding indices.
     *
     * @return A map where the key is the player's token and the value is the player's index.
     * @throws IOException If there is an IO error.
     */
    public Map<String, Integer> getToken_to_index() throws IOException{
        return token_to_index;
    }

    /**
     * Setter
     */

    private void setAllStates() throws IOException, InterruptedException {
        for (String t : token_to_player.keySet()){
            if( token_manager.getTokens().containsKey(t) && token_to_player.containsKey(t) ) {
                if( token_to_player.get(t).getPlayerState().equals(PlayerState.PLACE_CARD) ) token_manager.getVal(t).printString(" [ IT'S YOUR TURN TO PLACE A CARD ]");
                token_manager.getVal(t).setCenterCards(controller.getGame().getCars_in_center(),
                                                        controller.getGame().getResources_deck().cards.getFirst() ,
                                                         controller.getGame().getGold_deck().cards.getFirst());
                token_manager.getVal(t).setState(token_to_player.get(t).getActual_state().getNameState());
                token_manager.getVal(t).setNumToPlayer(index_to_name);
                token_manager.getVal(t).setGameField(getGameFields(t));
            }

        }
    }

    /**
     * Shows the cards in the center of the game board to a specific player.
     *
     * @param token The player's token.
     * @throws IOException If there is an IO error.
     */
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

    /**
     * Shows the player's starting card to the player.
     *
     * @param token The player's token.
     * @throws IOException If there is an IO error.
     */
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

    /**
     * Gets the player's starting card information for GUI display.
     *
     * @param token The player's token.
     * @return A representation of the card suitable for displaying in the GUI
     *         (e.g., card name, image path).
     * @throws IOException If there is an IO error.
     */
    public PlayCard showStartingCardGUI(String token) throws IOException{
        return token_to_player.get(token).getStartingCard();
    }

    /**
     * Checks for disconnected players and potentially ends the game if all players disconnect.
     *
     * @throws NoSuchObjectException If an object referenced during the check is not found.
     */
    public void checkEndDisconnect() throws NoSuchObjectException {
        int last_man_standing = 0;
        for(String s: token_to_player.keySet() ) {if( !token_to_player.get(s).isDisconnected() ) last_man_standing++;  }
        if ( last_man_standing == 0 ) {
            server.removeGameServer(this);
        }
    }

    /**
     *
     * @throws NoSuchObjectException
     */
    public synchronized void endConnection() throws NoSuchObjectException {
            server.removeGameServer(this);
    }

    /**
     * Registers a client connected via a socket with the game server.
     *
     * @param clientSocket The virtual view object representing the client's connection.
     * @throws IOException If there is an IO error.
     */
    public synchronized void connectSocket(VirtualViewF clientSocket)throws IOException{
        this.clientsRMI.add(clientSocket);
    }

    /**
     * Registers a client connected via RMI with the game server.
     *
     * @param client The virtual view object representing the client's connection.
     * @throws IOException If there is an IO error.
     */
    @Override
    public synchronized void connectRMI(VirtualViewF client)throws IOException{
        this.clientsRMI.add(client);
    }

}



