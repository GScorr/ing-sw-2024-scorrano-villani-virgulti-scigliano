package it.polimi.ingsw.VIEW;

import java.io.IOException;
import java.rmi.NotBoundException;

public interface GraficInterterface {

    void runCli() throws IOException, InterruptedException, NotBoundException, ClassNotFoundException;
    String selectNamePlayer() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;
    void gameAccess(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException;
    void waitFullGame() throws IOException, InterruptedException;
    void chooseGoalState() throws IOException, InterruptedException, ClassNotFoundException;
    void chooseStartingCardState() throws IOException, InterruptedException, ClassNotFoundException;
    void manageGame() throws IOException, InterruptedException, ClassNotFoundException;


}
