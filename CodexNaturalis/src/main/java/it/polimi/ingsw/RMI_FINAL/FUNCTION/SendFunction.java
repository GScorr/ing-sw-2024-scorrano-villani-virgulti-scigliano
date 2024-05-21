package it.polimi.ingsw.RMI_FINAL.FUNCTION;

import it.polimi.ingsw.RMI_FINAL.GameServer;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.SOCKET_FINAL.Message.Message;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface SendFunction extends Serializable {

    public default ResponseMessage action(GameServer server) throws RemoteException{ return null;}

}
