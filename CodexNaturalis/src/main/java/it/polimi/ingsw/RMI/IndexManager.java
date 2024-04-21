package it.polimi.ingsw.RMI;

public class IndexManager {
    private static int index_counter = 100000;

    public static synchronized int getNextIndex() {
        return index_counter++;
    }
}
