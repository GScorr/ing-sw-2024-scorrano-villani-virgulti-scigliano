package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MiniModel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Objects;

public interface VirtualViewF extends Remote {
    //todo da modificare
    public void showUpdate(GameField game_field) throws RemoteException;
    public void reportError(String details) throws RemoteException;
    public void reportMessage(String details) throws  RemoteException;
    public void showCard(PlayCard card) throws RemoteException;

    public void showField(GameField field) throws RemoteException;

    public void printString(String s) throws RemoteException;


    public MiniModel getMiniModel() throws RemoteException;


}
