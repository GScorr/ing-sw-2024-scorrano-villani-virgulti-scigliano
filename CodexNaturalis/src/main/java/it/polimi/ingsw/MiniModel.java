package it.polimi.ingsw;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.ChatIndexManager;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.io.IOException;
import java.io.Serializable;import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a mini-model containing information relevant to the client's view of the game state.
 *
 */
public class MiniModel implements Serializable {


    int unread_total = 0;
    int my_index;
    int num_players;
    public List<GameField> game_fields ;
    private List<Integer> not_read = new ArrayList<>();
    private List<PlayCard> cards_in_hand;
    private HashMap<Integer, String > num_to_player;
    private String state;
    private List<String> menu = new LinkedList<>();
    private List<String> chatmenu = new LinkedList<>();

    public List<PlayCard> getCards_in_hand() {
        return cards_in_hand;
    }

    private String turndecision;
    private Queue<ResponseMessage> messages = new LinkedList<>();

    private PlayCard top_resource;

    private PlayCard top_gold;

    private CenterCards cards_in_center;

    private Player my_player;
    private List<Chat> chat = new ArrayList<>();

    private ChatIndexManager chat_manager = new ChatIndexManager();

    private boolean final_state = false;

    public MiniModel() {
        this.state = "NOT_IN_A_GAME";
        this.menu.add("");
        this.menu.add("");
        this.menu.add("");
        this.menu.add("");
        this.chatmenu.add("");
        this.chatmenu.add("");
        this.chatmenu.add("");
        this.chatmenu.add("");
        this.chatmenu.add("");
        this.chatmenu.add("");
        for (int i=0; i<7; i++){
            chat.add(new Chat());
        }
        for (int i=0; i<7; i++){
            not_read.add(0);
        }
    }

    /**
     * Adds a new chat message to the specified chat conversation.
     *
     * @param idx The index of the chat conversation to add the message to.
     *            - 0: Public chat
     *            - 1 to num_players: Private chat with a specific player
     * @param message The ChatMessage object representing the message to be added.
     *
     * This method updates the chat history for the specified conversation, increments the unread message count for that conversation,
     * and recalculates the total number of unread messages across all chats.
     */
    public void addChat(int idx, ChatMessage message) {
        chat.get(idx).addMessage(message);
        not_read.set(idx,not_read.get(idx)+1);
        unread_total=0;
        for(Integer i : not_read){
            unread_total=unread_total+i;
        }
    }

    public PlayCard getTop_resource() {
        return top_resource;
    }

    public PlayCard getTop_gold() {
        return top_gold;
    }

    public CenterCards getCards_in_center() {
        return cards_in_center;
    }

    public Queue<ResponseMessage> getQueue(){ return messages; }

    /**
     * Adds a response message to the message queue.
     *
     * @param mess The ResponseMessage object representing the message to be added to the queue.
     */
    public void pushBack(ResponseMessage mess){
        messages.add(mess);
    }

    /**
     * Retrieves and removes the oldest message from the message queue.
     *
     * @return The ResponseMessage object representing the oldest message in the queue, or null if the queue is empty.
     */
    public ResponseMessage popOut(){
        return messages.poll();
    }

    public String printNumToField(){
        boolean wrong = false;
            while(!wrong){
            System.out.println("WHICH PLAYER'S GAME FIELD YOU WANT TO SEE?");
            // Create a list of player descriptions from the map
            List<String> playerDescriptions = getNum_to_player().entrySet().stream()
                    .map(entry -> "-" + entry.getKey() + " Name: " + entry.getValue())
                    .collect(Collectors.toList());

            // Print each player description
            playerDescriptions.forEach(System.out::println);

            // Read input from the user
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the player's ID:");
            int playerId = 0;
            try{
                    String pi = scanner.nextLine();
                    playerId = Integer.parseInt(pi);
                    // Return the name of the selected player
                    String playerName = num_to_player.get(playerId);
                    return playerName;
                }
            catch (NumberFormatException | InputMismatchException e){
                wrong = false;
            }
        }
        return null;
    }


    /**
     * Displays the game field of a specified player.
     *
     * @param playerName The name of the player whose game field to show.
     * @throws IOException If an I/O error occurs while printing to the console.
     */
    public void showGameField(String playerName) throws IOException {

        // Check if the opponent field popup is already open
        int playerNumber = 5;

        for(int i = 0; i <  this.getGame_fields().size() ; i++){
            if(playerName.compareTo( getGame_fields().get(i).getPlayer().getName()) == 0) playerNumber = i;
        }
        System.out.println("#ANIMALS : " + game_fields.get(playerNumber).getNumOfAnimal());
        System.out.println("#PLANTS : " + game_fields.get(playerNumber).getNumOfPlant());
        System.out.println("#INSECTS : " + game_fields.get(playerNumber).getNumOfInsect());
        System.out.println("#MUSHROOMS : " + game_fields.get(playerNumber).getNumOfMushroom());
        System.out.println("###PAPERS : " + game_fields.get(playerNumber).getNumOfPaper());
        System.out.println("###FEATHERS : " + game_fields.get(playerNumber).getNumOfFeather());
        System.out.println("###INKS : " + game_fields.get(playerNumber).getNumOfPen());
        showField(game_fields.get(playerNumber));
    }

    /**
     * Retrieves the game field associated with the current player.
     *
     * @return The game field object representing the current player's game state, or null if the player is not found.
     * @throws IOException If an I/O error occurs during retrieval.
     */
    public GameField getMyGameField() throws IOException {
        GameField field = null;
        for(GameField g : game_fields){
            if(g.getPlayer().getName().equals(my_player.getName())){
                field = g;
            }
        }
        return field;
    }

    public void setGameField(List<GameField> game){game_fields = game;}

    public void setCardsInCenter( CenterCards cards_in_center , PlayCard res , PlayCard gold){
        this.cards_in_center = cards_in_center;
        this.top_resource = res;
        this.top_gold = gold;
    }

    public void setCards(List<PlayCard> cards){
        cards_in_hand = cards;
    }

    public void setMy_player(Player my_player) {
        this.my_player = my_player;
    }

    public Player getMy_player() {
        return my_player;
    }

    /**
     * Displays all cards in the player's hand on the client-side.
     * @throws IOException If an I/O error occurs during communication.
     */
    public void showCards() throws IOException{
        for( PlayCard card : cards_in_hand) showCard(card);
    }

    /**
     * Prints the available menu options based on the current turn decision.
     *
     * @param turndecision The current turn decision string that determines the menu options.
     */
    public void printMenu(String turndecision){
        this.turndecision = turndecision;
        setMenu();
        for ( String m : menu ){
            System.out.println(m);
        }
    }

    /**
     * Displays the chat menu options to the user.
     *
     */
    public void uploadChat(){
        setChatMenu();
        for ( String m : chatmenu ){
            if(!m.equals("")) {
                System.out.println(m);
            }
        }
    }

    public void setState( String state){ this.state = state;}

    public String getState( ){ return state;}

    public void setMenu() {
        this.menu.set(0, ("\n0- SHOW FIELD"));
        this.menu.set(1, "1- SHOW CARDS IN HAND" );
        this.menu.set(2, "2- OPEN CHAT(S)" );
        this.menu.set(3, "3- " + turndecision);
    }

    public void setChatMenu() {
        int i=1;
        if(num_players > 2){
            for( i = 1; i<=num_players; ++i){
                if( i!=my_index ) this.chatmenu.set(i,i + "-CHAT WITH PLAYER " + num_to_player.get(i) + " - New messages (" + not_read.get(chat_manager.getChatIndex(my_index,i)) + ")");}
            this.chatmenu.set(i,i + "- PUBLIC CHAT" + " - New messages (" + not_read.get(6) + ")");
        }else{ this.chatmenu.set(i,i + "- PUBLIC CHAT" + " - New messages (" + not_read.get(6) + ")"); }

        //this.chatmenu.set(5, "5- WRITE MESSAGE 1 PLAYER");
    }

    /**
     * Displays the details of a play card on the client-side, including its front and back sides.
     *
     * @param card The play card to be displayed.
     * @throws IOException If an I/O error occurs during display.
     */
    public void showCard(PlayCard card) throws IOException {
        Side back = card.getBackSide();
        Side front = card.getFrontSide();

        System.out.println("BACK SIDE\n----------------------------");
        System.out.println( " | " + back.getAngleLeftUp().toString().substring(0,2)  +   " |               "+ " | " + back.getAngleRightUp().toString().substring(0,2) + " |\n " );
        //System.out.println( " | " + back.getAngleRightUp().toString().charAt(0) + " |\n " );
        System.out.println( " |       | " + back.getCentral_resource().toString().substring(0,2) + back.getCentral_resource2().toString().substring(0,2) + back.getCentral_resource3().toString().substring(0,2) + " |         |\n " );
        System.out.println( " | " + back.getAngleLeftDown().toString().substring(0,2) +  " |               " + " | " + back.getAngleRightDown().toString().substring(0,2) + " |\n " );
        //System.out.println(  );
        System.out.println("----------------------------\n\n");

        System.out.println("FRONT SIDE\n----------------------------");

        if(card instanceof ResourceCard) {
            System.out.println(" | " + card.getPoint() + " | ");
            if (card instanceof GoldCard) {
                System.out.println(" | " + ((GoldCard) card).getPointBonus().toString().substring(0, 2) + " | " + "             | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
            } else {
                System.out.println(" | " + front.getAngleLeftUp().toString().substring(0, 2) + " | " + "              | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
            }
        }
        else {
            System.out.println(" | " + front.getAngleLeftUp().toString().substring(0, 2) + " | " + "              | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
        }
        System.out.println( " |       | " + front.getCentral_resource().toString().substring(0,2) + front.getCentral_resource2().toString().substring(0,2) + front.getCentral_resource3().toString().substring(0,2) + " |        |\n " );
        if ( card instanceof GoldCard ){
            System.out.println( " | " + front.getAngleLeftDown().toString().substring(0,2) + " | " +
                    "  " + card.getCostraint().toString()  + " | " + front.getAngleRightDown().toString().substring(0,2) + " |\n ");
        }else{
            System.out.println( " | " + front.getAngleLeftDown().toString().substring(0,2) + " |              " + " | " + front.getAngleRightDown().toString().substring(0,2) + " |\n " );
        }
        System.out.println("----------------------------\n\n");

    }

    public void setNumToPlayer(HashMap<Integer, String> map){
        this.num_to_player = map;
    }

    public int getMy_index() {
        return my_index;
    }

    public int getNum_players() {
        return num_players;
    }

    /**
     * Displays the state of a game field on the client-side in a text-based format.
     *
     * This method analyzes the game field and identifies rows and columns that contain at least one filled cell.
     * It then prints a grid-like representation of the field, including row and column indices, and displays the short value
     * of each filled cell. Empty cells are represented by spaces.
     *
     * @param field The game field object to be displayed.
     * @throws IOException If an I/O error occurs during display.
     */
    private void showField(GameField field) throws IOException {

        boolean[] nonEmptyRows = new boolean[Constants.MATRIXDIM];
        boolean[] nonEmptyCols = new boolean[Constants.MATRIXDIM];

        for (int i = 0; i < Constants.MATRIXDIM; i++) {
            for (int j = 0; j < Constants.MATRIXDIM; j++) {
                if (field.getCell(i, j, Constants.MATRIXDIM).isFilled()) {
                    nonEmptyRows[i] = true;
                    nonEmptyCols[j] = true;
                    if (i > 0) nonEmptyRows[i - 1] = true;
                    if (i < Constants.MATRIXDIM - 1) nonEmptyRows[i + 1] = true;
                    if (j > 0) nonEmptyCols[j - 1] = true;
                    if (j < Constants.MATRIXDIM - 1) nonEmptyCols[j + 1] = true;
                }
            }
        }

        System.out.print("   ");
        for (int k = 0; k < Constants.MATRIXDIM; k++) {
            if (nonEmptyCols[k]) {
                System.out.print(k + " ");
            }
        }
        System.out.print("\n");

        for (int i = 0; i < Constants.MATRIXDIM; i++) {
            if (nonEmptyRows[i]) {
                System.out.print(i + " ");
                for (int j = 0; j < Constants.MATRIXDIM; j++) {
                    if (nonEmptyCols[j]) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).isFilled()) {
                            System.out.print(field.getCell(i, j, Constants.MATRIXDIM).getShort_value() + " ");
                        } else {
                            System.out.print("   ");
                        }
                    }
                }
                System.out.print("\n");
            }
        }
    }


    /*
    da eliminare
     */
    public void showFirstChat() {
        for(ChatMessage c : chat.get(0).getChat()){
            System.out.println(c.message);
        }
    }

    public void setMy_index(int my_index) {
        this.my_index = my_index;
    }

    public void setNum_players(int num_players) {
        this.num_players = num_players;
    }

    /**
     * Displays chat messages based on a user's decision.
     *
     * This method validates the user's decision (which player's chat to view) and displays
     * unread messages from the chosen chat channel. It also updates the unread message count.
     *
     * @param decision The index of the player whose chat to view (or a special index for general chat).
     * @return True if messages were displayed successfully, false otherwise.
     */
    public boolean showchat(int decision) {
        if(num_players!=2){
            if(decision>num_players+1 || decision==my_index){
                System.out.println("Wrong choice, insert again");
                return false;
            }
            if(decision==num_players+1){
                not_read.set(6,0);
                unread_total=0;
                for(Integer i : not_read){
                    unread_total=unread_total+i;
                }
                for(ChatMessage c : chat.get(6).getChat()){
                    System.out.println(c.player.getName() + "- " + c.message);
                }
                keepCheckChat(chat.get(6));
            }
            else {
                not_read.set(chat_manager.getChatIndex(my_index, decision), 0);
                unread_total=0;
                for(Integer i : not_read){
                    unread_total=unread_total+i;
                }
                for (ChatMessage c : chat.get(chat_manager.getChatIndex(my_index, decision)).getChat()) {
                    System.out.println(c.player.getName() + "- " + c.message);
                }
                keepCheckChat(chat.get(chat_manager.getChatIndex(my_index, decision)));
            }
        }
        else{
            not_read.set(6,0);
            unread_total=0;
            for(Integer i : not_read){
                unread_total=unread_total+i;
            }
            for(ChatMessage c : chat.get(6).getChat()){
                System.out.println(c.player.getName() + "- " + c.message);
            }
            keepCheckChat(chat.get(6));
        }
        return true;
    }

    private boolean stop_chat;

    /**
     * Sets a flag to control whether the chat should be stopped.
     *
     * @param b The boolean value to set the `stop_chat` flag. True indicates stopping the chat, false allows it to continue.
     */
    public void setStop_chat(boolean b){
        stop_chat = b;
    }

    /**
     * Continuously checks for new messages in a specified chat and displays them if found.
     *
     * This method creates a new thread that periodically checks the size of the provided chat's message list.
     * If the list size has increased since the last check, it iterates through the new messages and prints them to the console.
     * The checking process continues until the `stop_chat` flag is set to true.
     *
     * @param current_chat The chat object to monitor for new messages.
     */
    public void keepCheckChat(Chat current_chat){
        new Thread(() -> {
            int in_size = current_chat.getChat().size();
            while (!stop_chat) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if ( current_chat.getChat().size() > in_size ){
                    for ( int i = in_size ; i < current_chat.getChat().size() ; ++i  )
                        System.out.println(current_chat.getChat().get(i).player.getName() + "- " + current_chat.getChat().get(i).message);
                    in_size = current_chat.getChat().size();
                }

            }
        }).start();
    }

    public int getUnread_total() {
        return unread_total;
    }

    public List<GameField> getGame_fields() {
        return game_fields;
    }

    public List<Integer> getNot_read() {
        return not_read;
    }

    public List<String> getMenu() {
        return menu;
    }

    public List<String> getChatmenu() {
        return chatmenu;
    }

    public String getTurndecision() {
        return turndecision;
    }

    public Queue<ResponseMessage> getMessages() {
        return messages;
    }

    public List<Chat> getChat() {
        return chat;
    }

    public ChatIndexManager getChat_manager() {
        return chat_manager;
    }


    public boolean isStop_chat() {
        return stop_chat;
    }

    public void setUnread_total(int unread_total) {
        this.unread_total = unread_total;
    }

    public void setGame_fields(List<GameField> game_fields) {
        this.game_fields = game_fields;
    }

    public void setNot_read(List<Integer> not_read) {
        this.not_read = not_read;
    }

    public void setCards_in_hand(List<PlayCard> cards_in_hand) {
        this.cards_in_hand = cards_in_hand;
    }

    public void setNum_to_player(HashMap<Integer, String> num_to_player) {
        this.num_to_player = num_to_player;
    }

    public void setMenu(List<String> menu) {
        this.menu = menu;
    }

    public void setChatmenu(List<String> chatmenu) {
        this.chatmenu = chatmenu;
    }

    public void setTurndecision(String turndecision) {
        this.turndecision = turndecision;
    }

    public void setMessages(Queue<ResponseMessage> messages) {
        this.messages = messages;
    }

    public void setTop_resource(PlayCard top_resource) {
        this.top_resource = top_resource;
    }

    public void setTop_gold(PlayCard top_gold) {
        this.top_gold = top_gold;
    }

    public void setCards_in_center(CenterCards cards_in_center) {
        this.cards_in_center = cards_in_center;
    }

    public void setChat(List<Chat> chat) {
        this.chat = chat;
    }

    public void setChat_manager(ChatIndexManager chat_manager) {
        this.chat_manager = chat_manager;
    }

    public HashMap<Integer, String> getNum_to_player() {
        return num_to_player;
    }

    public boolean isFinal_state() {
        return final_state;
    }

    public void setFinal_state(boolean final_state) {
        this.final_state = final_state;
    }
}
