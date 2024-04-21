package it.polimi.ingsw.MODEL.Game;

public class IndexManagerF {
    private static int index_counter = 145231;

    public static synchronized int getNextIndex() {
        return index_counter++;
    }
}
