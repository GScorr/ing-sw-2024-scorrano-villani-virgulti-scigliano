package it.polimi.ingsw;

import it.polimi.ingsw.RMI_FINAL.RmiClientF;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET_FINAL.Client;


import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Common_Client {

    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException, ClassNotFoundException {

        Scanner scan = new Scanner(System.in);
        System.out.println("Scegli modalità server a cui connettersi : ");
        int choose = scan.nextInt();

        switch (choose) {
            case(0):
                Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
                VirtualServerF server = (VirtualServerF) registry.lookup("VirtualServer");
                new RmiClientF(server).run();
                break;

            case(1):
                String host = "127.0.0.1";
                int port = 12345;

                Socket serverSocket = new Socket(host, port);
                try{

                    ObjectOutputStream outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
                    ObjectInputStream inputStream = new ObjectInputStream(serverSocket.getInputStream());

                    new Client(inputStream, outputStream).run();
                }catch (IOException e) {
                    System.out.println("impossibile creare socket input / output");
                    return;
                }
                break;
        }
    }
}
