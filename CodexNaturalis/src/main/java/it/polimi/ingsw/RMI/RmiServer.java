package it.polimi.ingsw.RMI;

import it.polimi.ingsw.CONTROLLER.GameFieldController;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.ENUM.Costraint;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;
import it.polimi.ingsw.MODEL.Player.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RmiServer implements VirtualServer{

    final GameFieldController field_controller;
    final List<VirtualView> clients = new ArrayList<>();

    public RmiServer(GameFieldController fieldController) {
        field_controller = fieldController;
    }

    final BlockingQueue<GameFieldSingleCell[][]> updates = new ArrayBlockingQueue<>(20);
    private void broadcastUpdateThread() throws InterruptedException, RemoteException {

        while (true){
            GameFieldSingleCell[][] update = updates.take();
            synchronized (this.clients){
                for(var c: this.clients){
                    c.showUpdate(update);
                }
            }
        }
    }

    @Override
    public void connect(VirtualView client) throws RemoteException {
            System.err.println("NEW CLIENT CONNECTED");
            //todo data race ??
            this.clients.add(client);
    }

    @Override
    public void checkPlacingRMI(PlayCard card, int x, int y) throws RemoteException {
        System.err.println("insert request received");
        this.field_controller.checkPlacing(card, x, y);
        GameFieldSingleCell[][] current_state = this.field_controller.getCurrent();
        try
        {
            updates.put(current_state);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void checkGoldConstraintsRMI(Costraint val) throws RemoteException {

    }

    @Override
    public void goldPointsCountRMI(GoldCard card, int x, int y) throws RemoteException {

    }

    @Override
    public void resourcePointsCountRMI(ResourceCard card) throws RemoteException {

    }

    @Override
    public void resourcePointsChange(PlayCard card, int x, int y) throws RemoteException {

    }



}
