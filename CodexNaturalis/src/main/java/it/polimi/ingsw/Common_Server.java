package it.polimi.ingsw;

import it.polimi.ingsw.RMI_FINAL.RmiServerF;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Common_Server {

    private int num;
    public Common_Server(){
        this.num = 0;
    }

    public void setNum(int n){
        this.num = n;
    }

    public int getNum(){
        return this.num;
    }



    public static void main(String[] args) throws IOException, NotBoundException {

        Common_Server common = new Common_Server();

        final String serverName = "VirtualServer";
        VirtualServerF server = new RmiServerF(common);
        VirtualServerF stub = (VirtualServerF) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName,stub);
        System.out.println("[SUCCESSFUL] : RMI server connected. ");


        int port = 12345;
        ServerSocket listenSocket = new ServerSocket(port);
        System.out.println("[SUCCESSFUL] : SOCKET server is running...");
        new Server(listenSocket, stub, common).runServer();
    }
}


