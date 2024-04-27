package it.polimi.ingsw;

import it.polimi.ingsw.RMI_FINAL.RmiClientF;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;


import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException {

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
                int port = Integer.parseInt("4567");
                Socket serverSocket = new Socket(host, port);
                InputStreamReader socketRx = new InputStreamReader(serverSocket.getInputStream());
                OutputStreamWriter socketTx = new OutputStreamWriter(serverSocket.getOutputStream());
                //new ClientS(new BufferedReader(socketRx), new BufferedWriter(socketTx)).run();
                break;
        }
    }
}
