package it.polimi.ingsw;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.RMI_FINAL.RmiClientF;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET_FINAL.clientSocket;
import it.polimi.ingsw.VIEW.GuiPackage.Gui_Initialization;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * This class represents the entry point for the client application. It provides a user interface
 * to choose between a Text-based User Interface (TUI) and a Graphical User Interface (GUI). It
 * also establishes the connection with the server using either RMI or Socket communication based
 * on user selection.
 */
public class Common_Client {

    /**
     * This method prompts the user to choose between RMI and Socket connection for communication
     * with the server. It establishes the connection and starts the client functionality based on
     * the chosen option.
     *
     * @throws IOException Signals an I/O exception that might occur during communication or stream operations.
     * @throws NotBoundException Indicates that the specified name could not be found in the registry.
     * @throws InterruptedException  If the calling thread is interrupted while waiting.
     * @throws ClassNotFoundException  If a class needed for the operation could not be found.
     */
    private static void TUISocketOrRmi() throws IOException, NotBoundException, InterruptedException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        String choose="-1";
        do{
            if( !choose.equals("-1") ) System.err.println("[INSERT ERROR]");
            System.out.println("CHOOSE A CONNECTION : \n 0 -> RMI \n 1 -> SOCKET ");
            choose = scan.nextLine();
            switch (choose) {
                case("0"):
                    Registry registry = LocateRegistry.getRegistry(Constants.IPV4, 1234);
                    VirtualServerF server = (VirtualServerF) registry.lookup("VirtualServer");
                    new RmiClientF(server).run();
                    break;
                case("1"):
                    int port = 12345;
                    Socket serverSocket = new Socket(Constants.IPV4, port);
                    try{
                        ObjectOutputStream outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
                        ObjectInputStream inputStream = new ObjectInputStream(serverSocket.getInputStream());
                        new clientSocket(inputStream, outputStream, serverSocket).runTUI();
                    }catch (IOException e) {
                        System.err.println(e.getMessage());
                        return;
                    }
                    break;
            }
        }while( !choose.equals("0")  && !choose.equals("1"));
    }

    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException, ClassNotFoundException {

        printLogo();

        Scanner scan = new Scanner(System.in);
        String choose="-1";

        do{
            if( !choose.equals("-1") ) System.err.println("[INSERT ERROR]");
            System.out.println("CHOOSE A INTERFACE  : \n 0 -> TUI \n 1 -> Gui [FULL SCREEN IS NOT SUPPORTED] ");
            choose = scan.nextLine();
            switch (choose) {
                case("0"):
                    TUISocketOrRmi();
                    break;
                case("1"):
                    Gui_Initialization guiInitialization = new Gui_Initialization();
                    guiInitialization.runGui(null);
                    break;
            }
        }while( !choose.equals("0")  && !choose.equals("1"));

    }

    /**
     * print the logo in TUI
     *
     * @throws IOException
     */
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
