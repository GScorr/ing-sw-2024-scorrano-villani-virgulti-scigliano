package it.polimi.ingsw.MODEL.Game;

public class IndexRequestManagerF {
    private static Integer index_counter = 0;

    public static synchronized Integer getNextIndex() {
        return index_counter++;
    }
}
