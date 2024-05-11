package it.polimi.ingsw;

import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.*;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Common_Server {

    private TokenManagerF token_manager = new TokenManagerImplementF();
    private List<VirtualViewF> clients = new ArrayList<>();
    private Map<String, Player> token_to_player = new HashMap<>();
    private Map<String, GameServer>  token_to_rmi = new HashMap<>();
    private Map<Integer , GameServer> rmi_controllers = new HashMap<>();

    public Common_Server(){

    }

    public String createToken(VirtualViewF client) throws RemoteException {return token_manager.generateToken(client);}
    public String createTokenSocket(String name) throws RemoteException {return token_manager.generateTokenSocket(name);}


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


