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
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface VirtualViewF extends Remote {
    public void showUpdate(GameField game_field) throws RemoteException;
    public void reportError(String details) throws RemoteException;
    public void reportMessage(String details) throws  RemoteException;
    public void showCard(PlayCard card) throws RemoteException;
    public void pushBack(ResponseMessage message) throws RemoteException;
    public void showField(GameField field) throws RemoteException;
    public void printString(String s) throws RemoteException;
    public void setGameField(List<GameField> games) throws RemoteException;
    public MiniModel getMiniModel() throws RemoteException;
    public void setCards(List<PlayCard> cards) throws RemoteException;
    public void setNumToPlayer(HashMap<Integer, String> map) throws RemoteException;
    public void setState(String state) throws RemoteException;
    public void addChat(int idx, ChatMessage message) throws RemoteException;
    public void insertId(int id) throws RemoteException;
    public void insertNumPlayers(int numPlayersMatch) throws RemoteException;

    public void insertPlayer(Player player) throws RemoteException;

    //public int selectNamePlayer() throws IOException, NotBoundException;

    public int checkName(String playerName) throws IOException, NotBoundException;
    public void manageGame(boolean endgame) throws IOException, InterruptedException;
    public boolean areThereFreeGames () throws IOException, NotBoundException;

    public void createGame(String gameName, int numplayers, String playerName) throws IOException, NotBoundException;
    public VirtualGameServer getGameServer() throws IOException;
    public List<SocketRmiControllerObject> getFreeGames() throws RemoteException;
    public void ChatChoice(String message, int decision) throws IOException;
    public void selectAndInsertCard(int choice, int x, int y, boolean flipped) throws IOException, InterruptedException;
    public void drawCard(SendFunction function) throws IOException, InterruptedException;
    public String getToken() throws IOException;
}
