package it.polimi.ingsw;

import it.polimi.ingsw.RMI_FINAL.RmiServerF;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
//import it.polimi.ingsw.SOCKET.ServerS;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    public static void main(String[] args) throws RemoteException{

        final String serverName = "VirtualServer";
        VirtualServerF server = new RmiServerF();
        VirtualServerF stub = (VirtualServerF) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName,stub);
        System.out.println("[SUCCESSFUL] : RMI server connected. ");

        String host = "127.0.0.1";
        int port = Integer.parseInt("4567");
        ServerSocket listenSocket;
        try {
            listenSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /*try {
            System.out.println("[SUCCESSFUL] : SOCKET server connected. ");
            new ServerS(listenSocket, new Controller()).runServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }
}
