package it.polimi.ingsw.RMI;

import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.ENUM.Costraint;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {
    public void connect(VirtualView client) throws RemoteException;

    //functions in GameFieldController
    public void checkPlacingRMI(PlayCard card, int x, int y) throws RemoteException;
    public void checkGoldConstraintsRMI(Costraint val) throws  RemoteException;
    public void goldPointsCountRMI(GoldCard card, int x, int y) throws  RemoteException;
    public void resourcePointsCountRMI(ResourceCard card) throws RemoteException;
    public void resourcePointsChange(PlayCard card, int x, int y) throws  RemoteException;

}
