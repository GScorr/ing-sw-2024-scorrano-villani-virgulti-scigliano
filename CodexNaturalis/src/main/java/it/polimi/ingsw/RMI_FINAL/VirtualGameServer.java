package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VirtualGameServer extends Remote {
    public boolean getFull() throws RemoteException;

    public void connectSocket(VirtualView clientSocket) throws RemoteException;
    public void connectRMI(VirtualViewF client)throws RemoteException;
    public List<VirtualViewF> getClientsRMI() throws RemoteException;
    public Map<String, Player> getTtoP() throws RemoteException;
    public GameController getController() throws RemoteException;
    public Player createPlayer(String pla, String playerName, boolean b) throws RemoteException;
    public boolean addPlayer(String p_token, String name, VirtualViewF client, boolean is) throws RemoteException;
    public void chooseGoal(String token, int index) throws RemoteException;
    public void chooseStartingCard(String token, boolean flip) throws RemoteException;
    public void checkQueue() throws RemoteException;
    public void executeCall(Integer function) throws RemoteException;
    public void addtoQueue(String token, String string,Integer idRequest, Wrapper wrap) throws RemoteException;
    public void addQueue(SendFunction function) throws RemoteException;
    public int getPort() throws RemoteException;
    public void showStartingCard(String token) throws RemoteException;
    public void showGameField(String token) throws RemoteException;
    public void showCard(PlayCard card, String token) throws RemoteException;
    public void insertCard(String token, int index, int x, int y, boolean flipped) throws RemoteException, ControllerException;

    public void peachFromGoldDeck(String token) throws RemoteException;

    public void peachFromResourceDeck(String token) throws RemoteException;

    public void showPlayerCards(String token) throws RemoteException;

    public void showCardsInCenter(String token) throws RemoteException;


    public void wakeUp(String s, VirtualViewF client)throws RemoteException;

    public void peachFromCardsInCenter(String token, int index) throws RemoteException;

    public void getPoints(String token) throws RemoteException;

    public void getFinalStandings(String token) throws RemoteException;

    public List<GameField> getGameFields(String token) throws RemoteException;
}
