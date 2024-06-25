package it.polimi.ingsw.RMI_FINAL.FUNCTION;

import it.polimi.ingsw.RMI_FINAL.GameServer;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.SOCKET_FINAL.Message.Message;

import java.io.Serializable;

/**
 * Interface for defining remote actions in the game serverSocket.
 *
 * This interface represents functionalities that can be executed remotely
 * on the game serverSocket. Implementations of this interface define specific actions
 * and handle their execution logic.

 */
public interface SendFunction extends Serializable {

    /**
     * Default empty method for action execution.
     *
     * @param server the game serverSocket instance
     * @return a response message containing the action result (optional)
     */
    public default ResponseMessage action(GameServer server){ return null;}

}
