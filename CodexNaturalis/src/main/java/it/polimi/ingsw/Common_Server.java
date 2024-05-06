package it.polimi.ingsw;

import it.polimi.ingsw.RMI_FINAL.RmiServerF;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Common_Server {


    public static void main(String[] args) throws IOException {

        final String serverName = "VirtualServer";
        VirtualServerF server = new RmiServerF();
        VirtualServerF stub = (VirtualServerF) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName,stub);
        System.out.println("[SUCCESSFUL] : RMI server connected. ");

        String host = "127.0.0.1";
        int port = 12345;
        ServerSocket listenSocket = new ServerSocket(port);
        System.out.println("[SUCCESSFUL] : SOCKET server is running...");
        new Server(listenSocket, new Controller()).runServer();
    }
}


