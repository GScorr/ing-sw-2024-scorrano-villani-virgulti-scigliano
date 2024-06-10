package it.polimi.ingsw.VIEW;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;

public interface GraficInterterface extends Serializable {
    void printError(String a);

    public void setUsername(String username);
    void runCli() throws IOException, InterruptedException, NotBoundException, ClassNotFoundException;
    String selectNamePlayer() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;
    void gameAccess(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;
    void waitFullGame() throws IOException, InterruptedException;
    void chooseGoalState() throws IOException, InterruptedException, ClassNotFoundException;
    void chooseStartingCardState() throws IOException, InterruptedException, ClassNotFoundException;
    void manageGame() throws IOException, InterruptedException, ClassNotFoundException;

    String getName();
    void setToken(String token);
    public void setNewClient(boolean newClient);

    void buffering() throws  InterruptedException;
    public void newGame(String player_name, boolean empty) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;
    public void chooseMatch(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;
    public void guiGoManageGame() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;
}
