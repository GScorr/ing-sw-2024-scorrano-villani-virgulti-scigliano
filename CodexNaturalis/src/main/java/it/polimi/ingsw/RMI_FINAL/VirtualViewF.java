package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

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
}
