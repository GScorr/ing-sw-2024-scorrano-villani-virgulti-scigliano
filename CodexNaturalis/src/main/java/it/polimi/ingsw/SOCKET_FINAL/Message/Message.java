package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;

public interface Message  {
    static int id = 0;
    void action() throws IOException, InterruptedException;

    void setServer(Server server);
    void setOutput(ObjectOutputStream output);

    void setCommonServer(Common_Server common);

    void setToken(String token);

    void setRmiController(VirtualGameServer rmi_controller);

}
