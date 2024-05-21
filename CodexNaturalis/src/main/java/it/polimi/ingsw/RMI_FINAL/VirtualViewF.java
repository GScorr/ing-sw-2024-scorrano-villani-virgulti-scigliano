package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.IOException;
import java.util.HashMap;
import java.util.List;

public interface VirtualViewF extends Remote {
    public void showUpdate(GameField game_field) throws IOException;
    public void reportError(String details) throws IOException;
    public void reportMessage(String details) throws  IOException;
    public void showCard(PlayCard card) throws IOException;
    public void pushBack(ResponseMessage message) throws IOException;
    public void showField(GameField field) throws IOException;
    public void printString(String s) throws IOException;
    public void setGameField(List<GameField> games) throws IOException;
    public MiniModel getMiniModel() throws IOException;
    public void setCards(List<PlayCard> cards) throws IOException;
    public void setNumToPlayer(HashMap<Integer, String> map) throws IOException;
    public void setState(String state) throws IOException;
    public void addChat(int idx, ChatMessage message) throws IOException;
    public void insertId(int id) throws IOException;
    public void insertNumPlayers(int numPlayersMatch) throws IOException;

    public void insertPlayer(Player player) throws IOException;

    //public int selectNamePlayer() throws IOException, NotBoundException;

    public int checkName(String playerName) throws IOException, NotBoundException;

    public boolean areThereFreeGames () throws IOException, NotBoundException;

    public void createGame(String gameName, int numplayers, String playerName) throws IOException, NotBoundException;
    public void manageGame(boolean endgame) throws IOException;
    public void selectAndInsertCard(int choice, int x, int y, boolean flipped) throws IOException, InterruptedException;
    public void drawCard(SendFunction function) throws IOException, InterruptedException;
    public void ChatChoice(String message, int decision) throws IOException;
    public List<SocketRmiControllerObject> getFreeGames() throws IOException;
    public VirtualGameServer getGameServer() throws IOException;
    public String getToken() ;

    public void startSendingHeartbeats();
    public void setGameFieldMiniModel() throws IOException;
    public void startCheckingMessages();

    public boolean findRmiController(int id, String player_name) throws IOException;

    public void connectGameServer() throws IOException, NotBoundException;

    public boolean isGoalCardPlaced() throws IOException;

    public String getGoalPlaced() throws IOException;


    public String getFirstGoal() throws IOException;


    public String getSecondGoal() throws IOException;

    public void chooseGoal(int i) throws IOException;


    public void showStartingCard() throws IOException;


    public void chooseStartingCard(boolean b) throws IOException;

    public boolean isFirstPlaced() throws IOException;

}
