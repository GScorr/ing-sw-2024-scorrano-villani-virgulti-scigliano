package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class clientSocket implements VirtualViewF {
    MiniModel miniModel = new MiniModel();
    @Override
    public void showUpdate(GameField game_field) throws RemoteException {

    }

    @Override
    public void reportError(String details) throws RemoteException {

    }

    @Override
    public void reportMessage(String details) throws RemoteException {

    }

    @Override
    public void showCard(PlayCard card) throws RemoteException {

    }

    @Override
    public void pushBack(ResponseMessage message) throws RemoteException {

    }

    @Override
    public void showField(GameField field) throws RemoteException {

    }

    @Override
    public void printString(String s) throws RemoteException {

    }

    @Override
    public void setGameField(List<GameField> games) throws RemoteException {

    }

    @Override
    public MiniModel getMiniModel() throws RemoteException {
        return null;
    }

    @Override
    public void setCards(List<PlayCard> cards) throws RemoteException {

    }

    @Override
    public void setNumToPlayer(HashMap<Integer, String> map) throws RemoteException {

    }

    @Override
    public void setState(String state) throws RemoteException {

    }

    @Override
    public void addChat(int idx, ChatMessage message) throws RemoteException {

    }

    @Override
    public void insertId(int id) throws RemoteException {

    }

    @Override
    public void insertNumPlayers(int numPlayersMatch) throws RemoteException {

    }

    @Override
    public void insertPlayer(Player player) throws RemoteException {

    }

    @Override
    public int checkName(String playerName) throws IOException, NotBoundException {
        return 0;
    }

    @Override
    public boolean areThereFreeGames() throws IOException, NotBoundException {
        return false;
    }

    @Override
    public void createGame(String gameName, int numplayers, String playerName) throws IOException, NotBoundException {

    }

    @Override
    public List<SocketRmiControllerObject> getFreeGames() throws RemoteException {
        return null;
    }

    @Override
    public boolean findRmiController(int id, String playerName) throws IOException {
        return false;
    }

    @Override
    public void connectGameServer() throws RemoteException, NotBoundException {

    }

    @Override
    public void startSendingHeartbeats() {

    }

    @Override
    public void setGameFieldMiniModel() throws RemoteException {

    }

    @Override
    public void startCheckingMessages() {

    }

    @Override
    public boolean isGoalCardPlaced() throws RemoteException {
        return false;
    }

    @Override
    public String getGoalPlaced() {
        return null;
    }

    @Override
    public String getFirstGoal() {
        return null;
    }

    @Override
    public String getSecondGoal() {
        return null;
    }

    @Override
    public void chooseGoal(int i) throws IOException {

    }

    @Override
    public void showStartingCard() throws IOException {

    }

    @Override
    public void chooseStartingCard(boolean b) throws IOException {

    }

    @Override
    public boolean isFirstPlaced() throws RemoteException {
        return false;
    }



    //help function
    private void buffering() throws IOException, InterruptedException{
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("/");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("|");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("\\");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("-");
    }
    private boolean menuChoice(int choice) throws IOException {
        Scanner scan = new Scanner(System.in);
        if ( choice < 0 || choice > 3 ) return false;
        switch ( choice ){
            case ( 0 ):
                miniModel.printNumToField();
                Integer i = scan.nextInt();
                miniModel.showGameField(i);
                break;
            case( 1 ):
                miniModel.showCards();
                break;
            case ( 2 ):
                //   miniModel.showChat();
            case ( 3 ):
                return true;}
        return true;
    }

    public void showCardInCenter(PlayCard card) throws IOException {

        Side front = card.getFrontSide();

        System.out.println(" You can Only See the FRONT SIDE\n----------------------------");

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

    public void showStartingCard(PlayCard card) throws IOException {
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


}
