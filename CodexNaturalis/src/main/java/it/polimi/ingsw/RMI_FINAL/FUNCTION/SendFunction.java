package it.polimi.ingsw.RMI_FINAL.FUNCTION;

import it.polimi.ingsw.RMI_FINAL.GameServer;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.SOCKET_FINAL.Message.Message;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SendFunction implements Serializable {
    public GameServer server;

    public SendFunction(GameServer server) {
        this.server = server;
    }

    public ResponseMessage action(String token, int index, int x, int y, boolean flipped) throws RemoteException{ return null;}
}
