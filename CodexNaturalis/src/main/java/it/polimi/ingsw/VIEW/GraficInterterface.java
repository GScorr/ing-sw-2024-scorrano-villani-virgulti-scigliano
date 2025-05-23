package it.polimi.ingsw.VIEW;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;

/**
 * This interface defines methods for graphical user interface (GUI) interaction within the game.
 * It extends `Serializable` to allow objects implementing this interface to be passed
 * through streams.
 *
 */
public interface GraficInterterface extends Serializable {

    void printError(String a);
    void printUpdateMessage(String message);

    public void setUsername(String username);
    void runCli() throws IOException, InterruptedException, NotBoundException, ClassNotFoundException;
    String selectNamePlayer() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;
    void gameAccess(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;
    void waitFullGame() throws IOException, InterruptedException;
    void chooseGoalState() throws IOException, InterruptedException, ClassNotFoundException;
    void chooseStartingCardState() throws IOException, InterruptedException, ClassNotFoundException;
    void manageGame() throws IOException, InterruptedException, ClassNotFoundException;
    void startCountdown(String message, boolean still_alone, boolean win) throws InterruptedException, NotBoundException, IOException, ClassNotFoundException;
    String getName();
    void setToken(String token);
    public void setNewClient(boolean newClient);
    public boolean getIsAlone();
    public void makeChoice(String player_name) throws NotBoundException, IOException, ClassNotFoundException, InterruptedException;

    void buffering() throws  InterruptedException;
    public void newGame(String player_name, boolean empty) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;
    public void chooseMatch(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;
    public boolean getInGame();

    void endGame();
    // public void guiManageGame() throws IOException, InterruptedException;

}
