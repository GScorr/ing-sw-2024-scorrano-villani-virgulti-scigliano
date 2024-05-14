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
        printLogo();
        Scanner scan = new Scanner(System.in);
        int choose=-1;
        do{
            if(choose != -1 ) System.err.println("[INSERT ERROR]");
            System.out.println("CHOOSE A CONNECTION : \n 0 -> RMI \n 1 -> SOCKET ");
            choose = scan.nextInt();
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
        }while( choose != 0  && choose != 1);
    }




















    private static void printLogo() throws IOException {
        System.out.println("\n" +
                "                                 ___           ___           ___           ___           ___                            \n" +
                "                                /\\  \\         /\\  \\         /\\  \\         /\\  \\         |\\__\\                           \n" +
                "                               /::\\  \\       /::\\  \\       /::\\  \\       /::\\  \\        |:|  |                          \n" +
                "                              /:/\\:\\  \\     /:/\\:\\  \\     /:/\\:\\  \\     /:/\\:\\  \\       |:|  |                          \n" +
                "                             /:/  \\:\\  \\   /:/  \\:\\  \\   /:/  \\:\\__\\   /::\\~\\:\\  \\      |:|__|__                        \n" +
                "                            /:/__/ \\:\\__\\ /:/__/ \\:\\__\\ /:/__/ \\:|__| /:/\\:\\ \\:\\__\\ ____/::::\\__\\                       \n" +
                "                            \\:\\  \\  \\/__/ \\:\\  \\ /:/  / \\:\\  \\ /:/  / \\:\\~\\:\\ \\/__/ \\::::/~~/~                          \n" +
                "                             \\:\\  \\        \\:\\  /:/  /   \\:\\  /:/  /   \\:\\ \\:\\__\\    ~~|:|~~|                           \n" +
                "                              \\:\\  \\        \\:\\/:/  /     \\:\\/:/  /     \\:\\ \\/__/      |:|  |                           \n" +
                "                               \\:\\__\\        \\::/  /       \\::/  /       \\:\\__\\        |:|  |                           \n" +
                "                                \\/__/         \\/__/         \\/__/         \\/__/         \\|__|                           \n" +
                "      ___           ___           ___           ___           ___           ___           ___                   ___     \n" +
                "     /\\__\\         /\\  \\         /\\  \\         /\\__\\         /\\  \\         /\\  \\         /\\__\\      ___        /\\  \\    \n" +
                "    /::|  |       /::\\  \\        \\:\\  \\       /:/  /        /::\\  \\       /::\\  \\       /:/  /     /\\  \\      /::\\  \\   \n" +
                "   /:|:|  |      /:/\\:\\  \\        \\:\\  \\     /:/  /        /:/\\:\\  \\     /:/\\:\\  \\     /:/  /      \\:\\  \\    /:/\\ \\  \\  \n" +
                "  /:/|:|  |__   /::\\~\\:\\  \\       /::\\  \\   /:/  /  ___   /::\\~\\:\\  \\   /::\\~\\:\\  \\   /:/  /       /::\\__\\  _\\:\\~\\ \\  \\ \n" +
                " /:/ |:| /\\__\\ /:/\\:\\ \\:\\__\\     /:/\\:\\__\\ /:/__/  /\\__\\ /:/\\:\\ \\:\\__\\ /:/\\:\\ \\:\\__\\ /:/__/     __/:/\\/__/ /\\ \\:\\ \\ \\__\\\n" +
                " \\/__|:|/:/  / \\/__\\:\\/:/  /    /:/  \\/__/ \\:\\  \\ /:/  / \\/_|::\\/:/  / \\/__\\:\\/:/  / \\:\\  \\    /\\/:/  /    \\:\\ \\:\\ \\/__/\n" +
                "     |:/:/  /       \\::/  /    /:/  /       \\:\\  /:/  /     |:|::/  /       \\::/  /   \\:\\  \\   \\::/__/      \\:\\ \\:\\__\\  \n" +
                "     |::/  /        /:/  /     \\/__/         \\:\\/:/  /      |:|\\/__/        /:/  /     \\:\\  \\   \\:\\__\\       \\:\\/:/  /  \n" +
                "     /:/  /        /:/  /                     \\::/  /       |:|  |         /:/  /       \\:\\__\\   \\/__/        \\::/  /   \n" +
                "     \\/__/         \\/__/                       \\/__/         \\|__|         \\/__/         \\/__/                 \\/__/    \n" +
                "                                                                                                                        ");
    }
}
