package it.polimi.ingsw.VIEW.GuiPackage;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.RMI_FINAL.RmiClientF;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.clientSocket;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * Gui_Initialization is the main class for launching the JavaFX GUI application.
 * It handles user input for choosing between RMI or socket communication
 * and establishes the connection with the server.
 */
public class Gui_Initialization extends Application {

    VirtualViewF client;
    Stage stage;

    private static final String STR_ERROR = "ERROR";
    private static final String MENU_SCENE_FXML = "menu_scene.fxml";
    public SceneController scene;


    public Gui_Initialization() {

    }

    public void gameAcces(){

    }

    public static void main( String[] args ) {
        launch(args);
    }

    /**
     * Prompts user for connection type (RMI/Socket), establishes connection, and sets up client/scene.
     *
     * @throws IOException If an I/O error occurs during communication.
     * @throws NotBoundException If the RMI registry is not found.
     * @throws InterruptedException If the connection thread is interrupted.
     * @throws ClassNotFoundException If a class used during communication cannot be found.
     */
    public void runGui(String[] args){
        launch(args);
    }

    /**
     *
     * @throws IOException
     * @throws NotBoundException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    private void GUISocketOrRmi() throws IOException, NotBoundException, InterruptedException, ClassNotFoundException {
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

                    client = new RmiClientF(server);
                    scene.setClient(client);
                            client.runGUI(this.scene);
                    break;

                case("1"):
                    int port = 12345;
                    Socket serverSocket = new Socket(Constants.IPV4, port);
                    try{

                        ObjectOutputStream outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
                        ObjectInputStream inputStream = new ObjectInputStream(serverSocket.getInputStream());

                        client = new clientSocket(inputStream, outputStream);
                        scene.setClient(client);
                                client.runGUI(this.scene);
                    }catch (IOException e) {
                        System.err.println(e.getMessage());
                        return;
                    }
                    break;
            }
        }while( !choose.equals("0")  && !choose.equals("1"));
    }

    /**
     * Initializes the JavaFX application stage and scene controller.
     * Prompts the user to choose between RMI and socket communication,
     * establishes the connection with the server, and sets up the client and scene controller.
     *
     * @param primaryStage The primary stage of the JavaFX application.
     * @throws IOException If an I/O error occurs during communication.
     * @throws NotBoundException If the RMI registry is not found.
     * @throws InterruptedException If the connection thread is interrupted.
     * @throws ClassNotFoundException If a class used during communication cannot be found.
     */
    @Override
    public void start(Stage primaryStage) throws IOException, NotBoundException, InterruptedException, ClassNotFoundException {

        this.stage = primaryStage;
        this.scene = new SceneController();
        scene.setStage(stage);
        GUISocketOrRmi();
    }

}