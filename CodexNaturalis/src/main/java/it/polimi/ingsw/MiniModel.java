package it.polimi.ingsw;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.RMI_FINAL.ChatIndexManager;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class MiniModel implements Serializable {
    List<GameField> game_fields ;
    private int not_read;
    private List<PlayCard> cards_in_hand;

    private HashMap<Integer, String > num_to_player;

    private String state;
    private List<String> menu = new LinkedList<>();

    private String turndecision;
    private Queue<ResponseMessage> messages = new LinkedList<>();
    private List<Chat> chat = new ArrayList<>();

    private ChatIndexManager chat_manager = new ChatIndexManager();
    public MiniModel() {
        this.state = "NOT_INITIALIZED";
        this.not_read = 0;
        this.menu.add("");
        this.menu.add("");
        this.menu.add("");
        this.menu.add("");
        this.menu.add("");
        this.menu.add("");
        for (int i=0; i<5; i++){
            chat.add(new Chat());
        }
    }

    public void addChat(int idx, ChatMessage message) {
        chat.get(idx).addMessage(message);
    }

    public void setNotReadMessages(int nr){ this.not_read = nr;}

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

    public void showGameField(int pos) throws RemoteException {
        showField(game_fields.get(pos));
    }

    public void setGameField(List<GameField> game){game_fields = game;}

    public void setCards(List<PlayCard> cards){
        cards_in_hand = cards;
    }


    public void showCards() throws RemoteException{
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
        for ( String m : menu ){
            System.out.println(m);
        }
    }

    public void setState( String state){ this.state = state;}

    public String getState( ){ return state;}

    public void setMenu() {
        this.menu.set(0, ("\n0- SHOW FIELD"));
        this.menu.set(1, "1- SHOW CARDS IN HAND" );
        this.menu.set(2, "2- OPEN CHAT -> Messages Received : ( " + not_read + " )");
        this.menu.set(3, "3- " + turndecision);
    }



    public void setChatMenu() {
        this.menu.set(0, ("\n0- CHAT 1"));
        this.menu.set(1, "1- CHAT 2" );
        this.menu.set(2, "2- CHAT 3");
        this.menu.set(3, "3- CHAT 4");
        this.menu.set(4, "4- PUBLIC CHAT");
        this.menu.set(5, "5- WRITE MESSAGE 1 PLAYER");
    }

    public void showCard(PlayCard card) throws RemoteException {
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

    private void showField(GameField field) throws RemoteException {
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
}
