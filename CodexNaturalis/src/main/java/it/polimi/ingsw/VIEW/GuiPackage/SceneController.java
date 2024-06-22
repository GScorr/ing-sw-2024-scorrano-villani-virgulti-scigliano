package it.polimi.ingsw.VIEW.GuiPackage;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER.*;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SceneController {



    //path has to change
    public static final String CARD_IMAGE_PREFIX = "/images/cards/...";
    private String last_fxml;
    private  Scene activeScene;
    private  Stage stage;
    private  GenericSceneController activeController;
    private VirtualViewF client;

    private HeaderController header_controller;

    private static MediaPlayer mediaPlayer;

    public SceneController() {
       // BackgroundMusic.initialize();
    }



    public  void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Returns the active scene.
     *
     * @return active scene.
     */
    public  Scene getActiveScene() {
        return activeScene;
    }

    /**
     * Returns the active controller.
     *
     * @return active controller.
     */
    public  GenericSceneController getActiveController() {
        return activeController;
    }

    /**
     * Changes the root panel of the scene argument.
     *
     * @param scene        the scene whose change the root panel. This will become the active scene.
     * @param fxml         the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     * @param <T>          this is the type parameter.
     * @return the controller of the new scene loaded by the FXMLLoader.
     */
    public  <T> T changeRootPane( Scene scene, String fxml) {
        T controller = null;

        try {
            last_fxml = fxml;
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();
            controller = loader.getController();
            activeController = (GenericSceneController) controller;
            activeController.setClient(this.client);
            activeScene = new Scene(root,1500,750);
            activeScene.setRoot(root);
            activeController.setController(this);
            activeController.startInitialize();
            stage.setScene(activeScene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return controller;
    }

    /**
     * Changes the root panel of the scene argument.
     *
     * @param event        the event which is happened into the scene.
     * @param fxml         the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     * @param <T>          this is the type parameter.
     * @return the controller of the new scene loaded by the FXMLLoader.
     */
    public  <T> T changeRootPane( Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        return changeRootPane(scene, fxml);
    }

    /**
     * Changes the root panel of the active scene.
     *
     * @param fxml         the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     * @param <T>          this is the type parameter.
     * @return the controller of the new scene loaded by the FXMLLoader.
     */
    public  <T> T changeRootPane(String fxml) {
        return changeRootPane(activeScene, fxml);
    }


    /**
     * Shows a custom message in a popup.
     *
     * @param title   the title of the popup.
     * @param message the message of the popup.
     */
    public void showAlert(String title, String message) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alert_scene.fxml"));
        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        AlertSceneController alertSceneController = loader.getController();
        alertSceneController.setAlertTitle(title);
        alertSceneController.setAlertMessage(message);

        Stage stage = new Stage();
        stage.setTitle(title);
        Scene scene = new Scene(parent);
        stage.setScene(scene);

        alertSceneController.setStage(stage);
        alertSceneController.setController(this);


        stage.showAndWait();
    }

    /**
     * Opens a new chat scene with a specified title, index, client reference, decision value, and header controller.
     *
     * @param title The title of the chat window.
     * @param idx An index (purpose unclear, requires clarification).
     * @param client The client object for communication with the server.
     * @param decision A decision value (purpose unclear, requires clarification).
     * @param header The header controller associated with the chat scene.
     * @throws IOException If an I/O error occurs during FXML loading.
     */
    public void showChat(String title, int idx, VirtualViewF client, int decision, HeaderController header) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chat_scene.fxml"));
        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ChatController chatController = loader.getController();
        chatController.setTitle(title);
        chatController.setHeader(header);

        Stage stage = new Stage();
        stage.setTitle(title);
        Scene scene = new Scene(parent);
        stage.setScene(scene);

        chatController.setStage(stage);
        chatController.setIdx(idx);
        chatController.setClient(client);
        chatController.setController(this);
        chatController.startInitialize();
        chatController.setDecision(decision);

        stage.show();
    }

    /**
     * Opens a new modal window displaying a message with a specified title.
     *
     * @param title The title of the message window.
     * @param message The message content to be displayed.
     */
    public void showMessage(String title, String message) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/message_scene.fxml"));
        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        MessageSceneController messageSceneController = loader.getController();
        messageSceneController.setMessageTitle(title);
        messageSceneController.setMessageMessage(message);

        Stage stage = new Stage();
        stage.setTitle(title);
        Scene scene = new Scene(parent);
        stage.setScene(scene);

        messageSceneController.setStage(stage);
        messageSceneController.setController(this);


        stage.show();
    }

    public void setHeader_controller(HeaderController header_controller) {
        this.header_controller = header_controller;
    }

    public HeaderController getHeader_controller() {
        return header_controller;
    }

    public void setClient(VirtualViewF client) {
        this.client = client;
    }

    /**
     * Reloads the current scene with the previously loaded FXML file.
     * Assumes `last_fxml` is a member variable storing the path to the last loaded FXML file.
     *
     * @throws NullPointerException If `last_fxml` is null.
     */
    public void reloadPage() {
        changeRootPane(last_fxml);
    }

    public VirtualViewF getClient() {
        return client;
    }
}

/*
public class SceneController {

    //path has to change
    public static final String CARD_IMAGE_PREFIX = "/images/cards/...";
    private String last_fxml;
    private  Scene activeScene;
    private  Stage stage;
    private  GenericSceneController activeController;
    private VirtualViewF client;

    private HeaderController header_controller;


    public  void setStage(Stage stage) {
        this.stage = stage;
    }


    public  Scene getActiveScene() {
        return activeScene;
    }


    public  GenericSceneController getActiveController() {
        return activeController;
    }


    public  <T> T changeRootPane( Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        return changeRootPane(scene, fxml);
    }


    public <T> T changeRootPane(Scene scene, String fxml) {
        T controller = null;

        try {
            last_fxml = fxml;
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();
            controller = loader.getController();
            activeController = (GenericSceneController) controller;
            activeController.setClient(this.client);

            activeScene = new Scene(root, 1500, 750);
            activeScene.setRoot(root);
            activeController.setController(this);
            activeController.startInitialize();
            stage.setScene(activeScene);

            // set full screen
            stage.setMaximized(true);
            stage.setFullScreen(true);

            // hide message
            stage.setFullScreenExitHint("");

            // press esc to exit from full screen
            activeScene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    stage.setFullScreen(false);
                }
            });


            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return controller;
    }

    public <T> T changeRootPane(String fxml) {
        return changeRootPane(activeScene, fxml);
    }

    public void showAlert(String title, String message) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alert_scene.fxml"));
        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        AlertSceneController alertSceneController = loader.getController();
        alertSceneController.setAlertTitle(title);
        alertSceneController.setAlertMessage(message);

        Stage stage = new Stage();
        stage.setTitle(title);
        Scene scene = new Scene(parent);
        stage.setScene(scene);

        alertSceneController.setStage(stage);
        alertSceneController.setController(this);


        stage.showAndWait();
    }


    public void showChat(String title, int idx, VirtualViewF client, int decision, HeaderController header) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chat_scene.fxml"));
        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ChatController chatController = loader.getController();
        chatController.setTitle(title);
        chatController.setHeader(header);

        Stage stage = new Stage();
        stage.setTitle(title);
        Scene scene = new Scene(parent);
        stage.setScene(scene);

        chatController.setStage(stage);
        chatController.setIdx(idx);
        chatController.setClient(client);
        chatController.setController(this);
        chatController.startInitialize();
        chatController.setDecision(decision);

        stage.show();
    }

    public void showMessage(String title, String message) {
        // Carica il file FXML del messaggio
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/message_scene.fxml"));
        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Ottieni il controller del messaggio
        MessageSceneController messageSceneController = loader.getController();
        messageSceneController.setMessageTitle(title);
        messageSceneController.setMessageMessage(message);

        // Crea un Dialog personalizzato con pulsanti di default
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.initOwner(stage); // Imposta la finestra principale come owner per non bloccare l'interazione

        // Configura il DialogPane con la scena caricata dal FXML
        DialogPane dialogPane = new DialogPane();
        dialogPane.setContent(parent);

        // Aggiungi un pulsante "OK" al DialogPane
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().add(okButtonType);

        dialog.setDialogPane(dialogPane);

        // Gestione dell'azione quando si preme "OK"
        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButtonType) {
                // Azioni da eseguire quando si preme "OK"
                // Puoi aggiungere qui eventuali azioni da eseguire prima della chiusura del dialogo
                // ad esempio aggiornare la finestra principale se necessario
                return null; // Chiude il dialogo
            }
            return null;
        });

        // Mostra il dialogo senza bloccare l'interazione con la finestra principale
        dialog.show();
    }

    public void setHeader_controller(HeaderController header_controller) {
        this.header_controller = header_controller;
    }

    public HeaderController getHeader_controller() {
        return header_controller;
    }

    public void setClient(VirtualViewF client) {
        this.client = client;
    }

    public void reloadPage() {
        changeRootPane(last_fxml);
    }

    public VirtualViewF getClient() {
        return client;
    }
}

 */
