package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.List;

/**
 * Game list controller manages UI for available games
 * and handles player selection of a game to join.
 */
public class GameListController extends GenericSceneController{

    @FXML
    private VBox gameListContainer;

    private List<SocketRmiControllerObject> games;

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
        super.client.findRmiController(game.ID, super.client.getTerminal_interface().getName());
        super.client.connectGameServer();
        super.client.getTerminal_interface().waitFullGame();
    }

}
