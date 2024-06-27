package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.util.List;

/**
 * Game list controller manages UI for available games
 * and handles player selection of a game to join.
 */
public class GameListController extends GenericSceneController{

    public ImageView backgroundImage;
    @FXML
    private VBox gameListContainer;

    private List<SocketRmiControllerObject> games;

    public void initialize() {
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("BackGroundImaging/BackGround.png");

        // Set the background image
        Image image = new Image(resourceStream);
        backgroundImage.setImage(image);

        // Bind the background image size to the scene size
        backgroundImage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                backgroundImage.fitHeightProperty().bind(newScene.heightProperty());
                backgroundImage.fitWidthProperty().bind(newScene.widthProperty());
            }
        });
    }

    /**
     * Starts a thread to periodically retrieve available games and update the UI.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public void startInitialize() throws IOException, ClassNotFoundException, InterruptedException {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (super.client != null) {
                        games = client.getFreeGames();
                        Platform.runLater(() -> updateGameList(games));
                        break;
                    };
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Updates the game list UI with information from the provided list of games.
     *
     * @param games List of SocketRmiControllerObject objects (presumably representing game info)
     */
    private void updateGameList(List<SocketRmiControllerObject> games) {
        gameListContainer.getChildren().clear(); // Clear existing buttons
        for (SocketRmiControllerObject game : games) {
            Button button = new Button(game.name + " - Players: " + game.num_player + "/" + game.max_num_player);
            button.setPrefSize(150, 50); // Set the preferred width and height
            button.setOnAction(event -> {
                try {
                    handleGameButtonClick(game);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            gameListContainer.getChildren().add(button);
        }
    }

    /**
     * Handles the click event on a game button. Attempts to join the selected game.
     *
     * @param game SocketRmiControllerObject representing the selected game
     * @throws IOException
     * @throws InterruptedException
     * @throws NotBoundException
     * @throws ClassNotFoundException
     */
    private void handleGameButtonClick(SocketRmiControllerObject game) throws IOException, InterruptedException, NotBoundException, ClassNotFoundException {
        boolean check = client.findRmiController(game.ID, super.client.getTerminal_interface().getName());
        if(check) {
            super.client.connectGameServer();
            super.client.getTerminal_interface().waitFullGame();
        }else{
            showLoadingPopup();

        }


    }

    private void showLoadingPopup() {
        // Create an INFORMATION type alert for loading
        Alert loadingAlert = new Alert(Alert.AlertType.INFORMATION, "Game is full, you are being redirect to the Entry Page ...", ButtonType.OK);
        loadingAlert.setHeaderText(null);
        loadingAlert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false); // Nasconde il pulsante OK

        // show alert
        loadingAlert.show();

        // Create a timeline to close the alert after 2 second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            try {
                client.getTerminal_interface().manageGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            loadingAlert.close();
        }));

        // Start the timeline
        timeline.setCycleCount(1);
        timeline.play();
    }

}
