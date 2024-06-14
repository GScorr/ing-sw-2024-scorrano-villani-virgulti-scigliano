package it.polimi.ingsw.MODEL.Game;
/*
    todo
        questa classe sembra non essere usata
 */

/**
 *
 */
public class IndexRequestManagerF {
    /**
     *
     */
    private static Integer index_counter = 0;

    public static synchronized Integer getNextIndex() {
        return index_counter++;
    }
}
