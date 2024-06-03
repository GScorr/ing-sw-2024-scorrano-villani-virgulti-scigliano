package it.polimi.ingsw.VIEW.GuiPackage;


import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.RMI_FINAL.RmiClientF;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.clientSocket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;


import static javafx.application.Application.launch;

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


    public void runGui(String[] args){
        launch(args);
    }

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


    @Override
    public void start(Stage primaryStage) throws IOException, NotBoundException, InterruptedException, ClassNotFoundException {
/*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login_scene.fxml"));

        System.out.println(loader.getLocation());
        Parent root;
        root = loader.load();

        primaryStage.setTitle("CodexNaturalis - Login");

        Scene scene = new Scene(root, 1024, 720); // Crea una nuova scena con il nodo radice
        primaryStage.setScene(scene); // Imposta la scena nel palcoscenico

        primaryStage.show();

 */


        this.stage = primaryStage;
        this.scene = new SceneController();
        scene.setStage(stage);
        GUISocketOrRmi();


    }

}