package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET.ServerS;
import it.polimi.ingsw.SOCKET_FINAL.Server;

public interface Message  {
    static int id = 0;
    void action();
    void setController(Controller controller);
    void setServer(Server server);
}
