package it.polimi.ingsw.VIEW;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import static javafx.application.Application.launch;

public class GUI extends Application {

    /**
     * elementi che si adattano in base alla dimensione della pagina?
     * coordinate delle carte strane, da controllare
     */

    public static void main( String[] args ) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));

        System.out.println(loader.getLocation());
        Parent root;
        root = loader.load();

        primaryStage.setTitle("CodexNaturalis - Login");

        Scene scene = new Scene(root, 1024, 720); // Crea una nuova scena con il nodo radice
        primaryStage.setScene(scene); // Imposta la scena nel palcoscenico

        primaryStage.show();

    }

}