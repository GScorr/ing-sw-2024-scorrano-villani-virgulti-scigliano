package it.polimi.ingsw.RMI_FINAL;

import java.io.Serializable;

/**
 * This class represents a controller object used within a Remote Method Invocation (RMI) system.
 * It encapsulates information about a game or application managed by RMI. The object can be
 * serialized and deserialized for transmission across a network.
 */
public class SocketRmiControllerObject implements Serializable {
    public String name;
    public int ID;
    public int num_player;
    public int max_num_player;

    public SocketRmiControllerObject(String name, int ID, int num_player, int max_num_player) {
        this.name = name;
        this.ID = ID;
        this.num_player = num_player;
        this.max_num_player = max_num_player;
    }
}
