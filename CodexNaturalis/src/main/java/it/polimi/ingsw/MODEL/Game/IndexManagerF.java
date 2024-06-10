package it.polimi.ingsw.MODEL.Game;

/**
 * Utility class for generating unique sequential indices
 */
public class IndexManagerF {
    /**
     * Generates and returns the next unique index.
     *
     * @return the next unique integer index
     */
    private static int index_counter = 145231;

    public static synchronized int getNextIndex() {
        return index_counter++;
    }
}
