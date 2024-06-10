package it.polimi.ingsw;


import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.ChatIndexManager;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.io.IOException;
import java.io.Serializable;import java.util.*;

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

    public void addChat(int idx, ChatMessage message) {
        chat.get(idx).addMessage(message);
        not_read.set(idx,not_read.get(idx)+1);
        unread_total=0;
        for(Integer i : not_read){
            unread_total=unread_total+i;
        }
    }

    //public void setNotReadMessages(int nr){ this.not_read = nr;}


    public PlayCard getTop_resource() {
        return top_resource;
    }

    public PlayCard getTop_gold() {
        return top_gold;
    }

    public CenterCards getCards_in_center() {
        return cards_in_center;
    }

    public HashMap<Integer, String> getNum_to_player() {
        return num_to_player;
    }

    public Queue<ResponseMessage> getQueue(){ return messages; }

    public void pushBack(ResponseMessage mess){
        messages.add(mess);
    }

    public ResponseMessage popOut(){
        return messages.poll();
    }

    public void printNumToField(){
        System.out.println("WHICH PLAYER'S GAME FIELD YOU WANT TO SEE?");
        for( Integer i : num_to_player.keySet() ){
            System.out.println("-" + i + " Name:  " + num_to_player.get(i) );
        }
    }

    public void showGameField(int pos) throws IOException {
        pos = pos -1 ;
        System.out.println("#ANIMALS : " + game_fields.get(pos).getNumOfAnimal());
        System.out.println("#PLANTS : " + game_fields.get(pos).getNumOfPlant());
        System.out.println("#INSECTS : " + game_fields.get(pos).getNumOfInsect());
        System.out.println("#MUSHROOMS : " + game_fields.get(pos).getNumOfMushroom());
        System.out.println("###PAPERS : " + game_fields.get(pos).getNumOfPaper());
        System.out.println("###FEATHERS : " + game_fields.get(pos).getNumOfFeather());
        System.out.println("###INKS : " + game_fields.get(pos).getNumOfPen());
        showField(game_fields.get(pos));
    }

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

    public void showCards() throws IOException{
        for( PlayCard card : cards_in_hand) showCard(card);
    }

    public void printMenu(String turndecision){
        this.turndecision = turndecision;
        setMenu();
        for ( String m : menu ){
            System.out.println(m);
        }
    }

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
        this.menu.set(2, "2- OPEN CHAT(S)" + " - Unread messages (" + unread_total + ")");
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

    /* public void setChatMenu(){
        if( num_players == 2 ) this.chatmenu.set(2,1 + "- PUBLIC CHAT\n");
        else{
            for ( Integer i : num_to_player.keySet() ){
                if( i!=my_index ) this.chatmenu.set(i,i + "-CHAT WITH PLAYER " + num_to_player.get(i));
            }
            this.chatmenu.set(num_players,num_players + "- PUBLIC CHAT");
        }
    }*/

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
        //System.out.println( " | " + front.getAngleRightUp().toString().charAt(0) + " |\n " );
        System.out.println( " |       | " + front.getCentral_resource().toString().substring(0,2) + front.getCentral_resource2().toString().substring(0,2) + front.getCentral_resource3().toString().substring(0,2) + " |        |\n " );
        //System.out.println( " | " + front.getAngleLeftDown().toString().charAt(0) + " |       " );
        if ( card instanceof GoldCard ){
            System.out.println( " | " + front.getAngleLeftDown().toString().substring(0,2) + " | " +
                    "  " + card.getCostraint().toString()  + " | " + front.getAngleRightDown().toString().substring(0,2) + " |\n ");
        }else{
            System.out.println( " | " + front.getAngleLeftDown().toString().substring(0,2) + " |              " + " | " + front.getAngleRightDown().toString().substring(0,2) + " |\n " );
        }
        //System.out.println( " | " + front.getAngleRightDown().toString().charAt(0) + " |\n " );
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

    public void setStop_chat(boolean b){
        stop_chat = b;
    }
    private void keepCheckChat(Chat current_chat){
        new Thread(() -> {
            System.out.println("sono un nuovo thread");
            int in_size = current_chat.getChat().size();
            while (!stop_chat) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //in_size = current_chat.getChat().size();
                if ( current_chat.getChat().size() > in_size ){
                    for ( int i = in_size ; i < current_chat.getChat().size() ; ++i  )
                        System.out.println(current_chat.getChat().get(i).player.getName() + "- " + current_chat.getChat().get(i).message);
                    in_size = current_chat.getChat().size();
                }

            }
        }).start();
    }




}
