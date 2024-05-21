package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
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
}
