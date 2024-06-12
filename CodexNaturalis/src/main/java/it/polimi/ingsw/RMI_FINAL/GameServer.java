package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Card.PlayCard;
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
 *
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
    private int id = 0;
    public ChatIndexManager chatmanager = new ChatIndexManager();
    public Map<String,Integer> token_to_index = new HashMap<>();
    public Map<Integer,String> index_to_token = new HashMap<>();
    public HashMap<Integer,String> index_to_name = new HashMap<>();
    private Common_Server server;
    //private int id_game_server = 0; da eliminare


    public GameServer(String name, int numPlayer, int port, Common_Server commonServer) throws IOException {
        this.controller = new GameController(name, numPlayer);
        checkQueue();
        playDisconnected();
        checkDeadline();
        this.port = port;
        this.server = commonServer;
    }


    //GAME FLOW

    /**
     * Adds a player to the game with the specified token, name, and client reference.
     *
     * This method attempts to add a new player to the game, performing the following steps:
     * - Checks if the game is full.
     * - Creates the player object with the provided token, name, and "isFirst" flag (meaning unclear).
     * - Associates the player's token with the client reference.
     * - Updates the game state with the new player information.
     * - Broadcasts the updated game state to all players.
     * - Adds the client reference to a list of connected clients (likely for RMI communication).
     *
     * @param p_token the player's unique token
     * @param name the player's name
     * @param client the client's virtual view reference
     * @param isFirst boolean indicate if the player is the first player
     * @return true if the player is added successfully, false otherwise
     * @throws IOException  if an I/O error occurs during communication
     * @throws InterruptedException  if the thread is interrupted while waiting
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
        clientsRMI.add(client);
        return true;
    }

    /**
     * Creates a new player object with the specified name and flag.
     *
     * This method delegates the player creation to the associated controller object.
     * It then stores a mapping between the player's token and the created player object
     *
     * @param p_token the player's unique token
     * @param playerName the player's name
     * @param b
     * @return the newly created player object
     * @throws IOException  if an I/O error occurs during communication with the controller
     */
    public synchronized Player createPlayer(String p_token,String playerName, boolean b) throws IOException{
        Player p = controller.createPlayer(playerName,b);
        token_to_player.put(p_token , p);
        return p;
    }

    //questa funzione non dovrebbe essere più usata
    //fake questa funzione viene ancora usata

    /**
     * Adds a player to the game using a socket connection, with the specified token, name, and client reference.
     *
     * It performs the following actions:
     * - Checks if the game is full.
     * - Creates the player object.
     * - Associates the player's token with the client reference.
     * - Updates the game state with the new player information
     * - Broadcasts the updated game state to all players
     * - Adds the client reference to a list of connected clients
     *
     * @param p_token the player's unique token
     * @param name the player's name
     * @param client the client's virtual view reference
     * @param isFirst
     * @return true if the player is added successfully, false otherwise (e.g., game full)
     * @throws IOException  if an I/O error occurs during communication
     * @throws InterruptedException  if the thread is interrupted while waiting
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
     * Forwards the player's chosen goal selection to the game controller.
     *
     *
     * @param token the player's unique token
     * @param index the index of the chosen goal
     * @throws IOException  if an I/O error occurs during communication
     * @throws InterruptedException  if the thread is interrupted while waiting
     */
    @Override
    public void chooseGoal(String token, int index) throws IOException, InterruptedException {
        controller.playerChooseGoal(token_to_player.get(token), index);
        setAllStates();
    }

    /**
     * Informs the game controller about the player's choice of starting card and updates client information.
     *
     * @param token the player's unique token
     * @param flip
     * @throws IOException  if an I/O error occurs during communication
     * @throws InterruptedException  if the thread is interrupted while waiting
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
            }
            num_to_player.put(index, token_to_player.get(t).getName() );
            index++;
        }
        setAllStates();
    }

    /**
     * Attempts to place a card on the game board for the specified player.
     *
     * This method performs the following actions:
     * - Flips the card in the player's hand based on the `flipped` flag.
     * - Delegates the card placement logic to the game controller, providing the player object, card index, and grid coordinates.
     * - Potentially updates the player's hand information on the client-side (implementation details not shown).
     *
     * @param token the player's unique token
     * @param index the index of the card in the player's hand
     * @param x the x-coordinate of the target grid position
     * @param y the y-coordinate of the target grid position
     * @param flipped indicates whether to flip the card
     * @throws IOException  if an I/O error occurs during communication
     * @throws ControllerException  if the game controller detects an invalid placement
     */
    public synchronized void insertCard(String token, int index, int x, int y, boolean flipped) throws IOException, ControllerException {
        token_to_player.get(token).getCardsInHand().get(index).flipCard(flipped);
        controller.statePlaceCard(token_to_player.get(token), index, x, y);
        token_manager.getVal(token).setCards( token_to_player.get(token).getCardsInHand() );
    }

    /**
     * Starts a background thread to continuously process messages from a queue.
     *
     * This method creates a new thread that runs indefinitely. The thread loops continuously
     * performing the following actions:
     * - Sleeps for 100 milliseconds.
     * - Checks if the message queue is not empty.
     * - If messages are present, it dequeues a message, executes its action method
     *   passing the current object as an argument, and broadcasts the result.
     * - Catches potential interruptions and I/O exceptions during processing.
     *
     * @throws IOException  if an I/O error occurs during message broadcasting
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
     * Broadcasts a response message to all connected RMI clients.
     *
     * @param message the response message to be broadcasted
     * @throws IOException  if an I/O error occurs during communication with a client
     */
    private void broadcastMessage(ResponseMessage message) throws IOException {
        for (VirtualViewF c : clientsRMI){
            c.pushBack(message);}
    }

    /**
     * Adds a function object to the message queue for later execution.
     *
     * @param function the function object to be queued for execution
     */
    public void addQueue(SendFunction function) {
        functQueue.add(function);
    }

    /**
     * Retrieves and sends the final game standings to the specified player.
     *
     * This method iterates through the player list retrieved from the game controller
     * and constructs a formatted string containing the player's rank and name. It then
     * sends this information to the client associated with the provided token.
     * Then it calls the `endConnection` method to close the connection.
     *
     * @param token the player's unique token
     * @throws IOException  if an I/O error occurs during communication with the client
     */
    public void getFinalStandings(String token) throws IOException {
        int i = 1;
        for(Player p : controller.getPlayer_list()){
            token_manager.getTokens().get(token).printString(i + "- " + p.getName());
            i++;
        }
        endConnection();
    }

    //DISCONNECTION

    /**
     * Attempts to re-establish a connection for a disconnected player.
     *
     * This method searches for a player with the matching name in the `token_to_player` map.
     * If found, it performs the following actions:
     * - Updates the associated token with the new client reference.
     * - Sends a reconnection message.
     * - Reconnects the player object.
     * - Adds the client reference to the list of connected RMI clients.
     * - Updates player information on the client-side.
     * - Sets a mapping between player index and name.
     *
     * @param name the name of the player attempting to reconnect
     * @param client the new client reference for the reconnected player
     * @throws IOException  if an I/O error occurs during communication
     */
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
                }
        }
    }

    /**
     * Starts a background thread to handle disconnected players.
     *
     * This method creates a thread that continuously checks for disconnected players.
     * If a player is disconnected and in specific game states (CHOOSE_GOAL, CHOOSE_SIDE_FIRST_CARD, PLACE_CARD, DRAW_CARD),
     * it performs actions on their behalf (e.g., choosing default goal, starting card, or advancing the game state).
     * Potential errors during communication or state updates are caught and logged.
     *
     * @throws IOException  if an I/O error occurs during communication
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
     * Starts a background thread to handle game deadlines and disconnections.
     *
     * This method creates a thread that monitors the following conditions:
     * - If the maximum number of players is reached and only one player remains connected (due to disconnects).
     *   - If so, it initiates a 45-second countdown, notifying the remaining player and checking for reconnects.
     *  - If the player reconnects during the countdown, the game resumes.
     *  - If the countdown finishes and the player remains disconnected, they are declared the winner
     *    due to disconnects (assuming other players are also disconnected).
     *
     * The method catches potential runtime exceptions and continues execution.
     *
     * @throws RuntimeException  if unexpected errors occur during communication
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

    //DRAW CARD METHODS

    /**
     * draw card methods
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
     * getter
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

    /**
     * Facilitates private chat communication between two players.
     *
     * This method retrieves tokens for the provided player IDs (`id1` and `id2`).
     * It then performs the following actions:
     * - Inserts the chat message into the game controller's chat storage.
     * - Updates the private chat information for both players on the client-side.
     * - Sends a notification to the recipient player indicating a received message from the sender's name.
     *
     * @param id1 the ID of the first player
     * @param id2 the ID of the second player
     * @param message the chat message object containing the content
     * @throws IOException  if an I/O error occurs during communication with clients
     */
    public synchronized void chattingMoment(int id1, int id2, ChatMessage message) throws IOException {
        String t1 = index_to_token.get(id1);
        String t2 = index_to_token.get(id2);
        controller.insertMessageinChat(chatmanager.getChatIndex(id1,id2),message);
        updatePrivateChats(t1, t2, chatmanager.getChatIndex(id1,id2), message);
        token_manager.getVal(t2).printString( "                                 YOU RECEIVED A MESSAGE FROM [ " +  token_to_player.get(t1).getName() + " ]");
    }

    /**
     * Broadcasts a global chat message to all players.
     *
     * This method inserts the chat message into the game controller's chat storage for the global chat channel.
     * It then calls the `updatePublicChats` method.
     *
     * @param message the chat message object containing the content
     * @throws IOException  if an I/O error occurs during communication with clients
     */
    public synchronized void chattingGlobal(ChatMessage message) throws IOException {
        controller.insertMessageinChat(6,message);
        updatePublicChats(message);
    }

    /**
     *
     * @param message
     * @throws IOException
     */
    private void updatePublicChats(ChatMessage message) throws IOException {
        for (String t : token_to_player.keySet()){
                token_manager.getTokens().get(t).addChat(6, message);
                if( !message.player.getName().equals(token_to_player.get(t).getName()) ) token_manager.getVal(t).printString( "                                 YOU RECEIVED A MESSAGE IN GLOBAL CHAT");
        }
    }

    /**
     * Updates the private chat information for the sender and receiver of a private message.
     *
     * This method iterates through all players:
     * - If the player's token matches the sender (`token1`) or receiver (`token2`),
     *   it adds the chat message to their private chat history for the specific conversation (identified by index `idx`).
     *
     * @param token1 the token of the message sender
     * @param token2 the token of the message receiver
     * @param idx the index of the private chat conversation
     * @param message the chat message object containing the content
     * @throws IOException  if an I/O error occurs during communication with clients
     */
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

    private void setAllStates() throws IOException, InterruptedException {
        for (String t : token_to_player.keySet()){
            if( token_manager.getTokens().containsKey(t) && token_to_player.containsKey(t) ) {
                if( token_to_player.get(t).getPlayerState().equals(PlayerState.PLACE_CARD) ) token_manager.getVal(t).printString(" [ IT'S YOUR TURN TO PLACE A CARD ]");
                token_manager.getVal(t).setCenterCards(controller.getGame().getCars_in_center(),
                                                        controller.getGame().getResources_deck().cards.getFirst() ,
                                                         controller.getGame().getGold_deck().cards.getFirst());
                token_manager.getVal(t).setState(token_to_player.get(t).getActual_state().getNameState());
                token_manager.getVal(t).setNumToPlayer(index_to_name);
                //if ( token_manager.getVal(t).getTerminal_interface().getInGame() ) token_manager.getVal(t).getTerminal_interface().guiManageGame();
            }

        }
    }

    /**
     * Sends information about the center cards to the specified player.
     *
     * This method checks if the player is connected via RMI or sockets. It then retrieves
     * the list of gold and resource cards from the game controller's central storage.
     * Finally, it iterates through the cards and sends their details
     * to the player for display.
     *
     * @param token the player's unique token
     * @throws IOException  if an I/O error occurs during communication with the client
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
     * Sends the player's chosen starting card information to the client.
     *
     * This method retrieves the player's chosen starting card from the internal player storage.
     * It then checks if the player is connected via RMI or sockets and sends the card details
     * to the client for display.
     *
     * @param token the player's unique token
     * @throws IOException  if an I/O error occurs during communication with the client
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
     * Retrieves the player's chosen starting card information.
     *
     * This method retrieves the player's chosen starting card from the internal player storage.
     *
     * @param token the player's unique token
     * @return the player's chosen starting card object
     * @throws IOException  if an I/O error occurs during internal data access
     */
    public PlayCard showStartingCardGUI(String token) throws IOException{
        return token_to_player.get(token).getStartingCard();
    }

    /**
     * Checks for disconnected players and removes the game server if all are disconnected.
     *
     * @throws NoSuchObjectException  (exception handling details omitted)
     */
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

    /**
     *
     * @param clientSocket
     * @throws IOException
     */
    public synchronized void connectSocket(VirtualViewF clientSocket)throws IOException{
        this.clientsRMI.add(clientSocket);
    }

    /**
     *
     * @param client
     * @throws IOException
     */
    @Override
    public synchronized void connectRMI(VirtualViewF client)throws IOException{
        this.clientsRMI.add(client);
    }


}



