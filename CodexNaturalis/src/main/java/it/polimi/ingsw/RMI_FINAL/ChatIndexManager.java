package it.polimi.ingsw.RMI_FINAL;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages chat indexes for efficient retrieval of chat conversations between players.
 *
 * This class uses a HashMap to store a mapping between player IDs. Given two player IDs,
 * it efficiently retrieves the index of the corresponding chat conversation for those players.
 *
 */
public class ChatIndexManager {

    private final Map<Integer, Map<Integer, Integer>> chatIndexMap = new HashMap<>();

    public ChatIndexManager() {
        Map<Integer, Integer> mapFor1 = new HashMap<>();
        mapFor1.put(2, 0);
        mapFor1.put(3, 1);
        mapFor1.put(4, 2);
        chatIndexMap.put(1, mapFor1);

        Map<Integer, Integer> mapFor2 = new HashMap<>();
        mapFor2.put(1, 0);
        mapFor2.put(3, 3);
        mapFor2.put(4, 4);
        chatIndexMap.put(2, mapFor2);

        Map<Integer, Integer> mapFor3 = new HashMap<>();
        mapFor3.put(1, 1);
        mapFor3.put(2, 3);
        mapFor3.put(4, 5);
        chatIndexMap.put(3, mapFor3);

        Map<Integer, Integer> mapFor4 = new HashMap<>();
        mapFor4.put(1, 2);
        mapFor4.put(2, 4);
        mapFor4.put(3, 5);
        chatIndexMap.put(4, mapFor4);
    }

    public Integer getChatIndex(int id1, int id2) {
        if (chatIndexMap.containsKey(id1) && chatIndexMap.get(id1).containsKey(id2)) {
            return chatIndexMap.get(id1).get(id2);
        }
        return null;
    }

}