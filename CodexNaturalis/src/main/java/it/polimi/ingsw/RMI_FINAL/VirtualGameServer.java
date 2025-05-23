package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;

import java.util.List;
import java.util.Map;

/**
 * This interface defines methods for a virtual game serverSocket used in a Remote Method Invocation (RMI) system.
 * The serverSocket manages game logic, player interactions, and communication for a game session.
 *
 */
public interface VirtualGameServer extends Remote, Serializable {
    public boolean getFull() throws IOException;

    public void connectSocket(VirtualViewF clientSocket) throws IOException;
    public void connectRMI(VirtualViewF client)throws IOException;
    public List<VirtualViewF> getClientsRMI() throws IOException;
    public Map<String, Player> getTtoP() throws IOException;
    public GameController getController() throws IOException;
    public Player createPlayer(String pla, String playerName, boolean b) throws IOException;
    public boolean addPlayer(String p_token, String name, VirtualViewF client, boolean is) throws IOException, InterruptedException;
    public void chooseGoal(String token, int index) throws IOException, InterruptedException;
    public void chooseStartingCard(String token, boolean flip) throws IOException, InterruptedException;
    public void checkQueue() throws IOException;
    public void addQueue(SendFunction function) throws IOException;
    public int getPort() throws IOException;
    public void showStartingCard(String token) throws IOException;

    public void insertCard(String token, int index, int x, int y, boolean flipped) throws IOException, ControllerException;

    public void peachFromGoldDeck(String token) throws IOException, InterruptedException;

    public void peachFromResourceDeck(String token) throws IOException, InterruptedException;


    public void showCardsInCenter(String token) throws IOException;


    public void wakeUp(String s, VirtualViewF client) throws IOException, InterruptedException;

    public void peachFromCardsInCenter(String token, int index) throws IOException, InterruptedException;

    public void getPoints(String token) throws IOException;

    public void getFinalStandings(String token) throws IOException, InterruptedException;

    public List<GameField> getGameFields(String token) throws IOException;
    boolean isGoldDeckPresent() throws IOException;
    boolean isResourceDeckPresent() throws IOException;


    public void chattingMoment(int i1, int i2, ChatMessage message) throws IOException;

    public Map<String, Integer> getToken_to_index() throws IOException;

    public void chattingGlobal(ChatMessage message) throws IOException;

    PlayCard showStartingCardGUI(String token)  throws IOException;

    public List<GameField> getGameFields() throws IOException;
}
