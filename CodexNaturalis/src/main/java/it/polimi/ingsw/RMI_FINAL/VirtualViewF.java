package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.VIEW.GraficInterterface;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.List;

/**
 * This interface defines methods for a virtual view used in a Remote Method Invocation (RMI) system.
 * The virtual view acts as a client-side interface, receiving updates and interacting with the game server.
 *
 */
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
    public void setCenterCards(CenterCards cards, PlayCard res , PlayCard gold) throws IOException;
    public void addChat(int idx, ChatMessage message) throws IOException;
    public void insertId(int id) throws IOException;
    public void insertNumPlayers(int numPlayersMatch) throws IOException;

    public void insertPlayer(Player player) throws IOException;

    public GraficInterterface getTerminal_interface() throws IOException;

    public int checkName(String playerName) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;

    public boolean areThereFreeGames () throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;

    public void createGame(String gameName, int numplayers, String playerName) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;
    public void manageGame(boolean endgame) throws IOException;
    public void selectAndInsertCard(int choice, int x, int y, boolean flipped) throws IOException, InterruptedException, ClassNotFoundException;
    public void drawCard(SendFunction function) throws IOException, InterruptedException;
    public void ChatChoice(String message, int decision) throws IOException;
    public List<SocketRmiControllerObject> getFreeGames() throws IOException, ClassNotFoundException, InterruptedException;
    public VirtualGameServer getGameServer() throws IOException;

    public void setGameFieldMiniModel() throws IOException;

    public boolean findRmiController(int id, String player_name) throws IOException, ClassNotFoundException, InterruptedException;

    public void connectGameServer() throws IOException, NotBoundException, InterruptedException;

    public boolean isGoalCardPlaced() throws IOException, ClassNotFoundException, InterruptedException;

    public String getGoalPlaced() throws IOException;

    public PlayCard showStartingCardGUI() throws IOException, ClassNotFoundException, InterruptedException;


    public String getFirstGoal() throws IOException, ClassNotFoundException, InterruptedException;


    public String getSecondGoal() throws IOException;

    public void chooseGoal(int i) throws IOException, InterruptedException;

    public Goal getFirstGoalCard() throws IOException, InterruptedException;
    public Goal getSecondGoalCard() throws IOException;


    public void showStartingCard() throws IOException, InterruptedException;


    public void chooseStartingCard(boolean b) throws IOException, InterruptedException;

    public boolean isFirstPlaced() throws IOException, InterruptedException;

    String getToken() throws InterruptedException, IOException;

    boolean isGoldDeckPresent() throws IOException, ClassNotFoundException, InterruptedException;

    boolean isResourceDeckPresent() throws IOException, ClassNotFoundException, InterruptedException;

    void showCardsInCenter() throws IOException, ClassNotFoundException, InterruptedException;

    void runGUI(SceneController scene) throws IOException, ClassNotFoundException, InterruptedException, NotBoundException;

    void disconect() throws IOException, ClassNotFoundException, InterruptedException, NotBoundException;


    void setLastTurn(boolean b) throws IOException;
}
