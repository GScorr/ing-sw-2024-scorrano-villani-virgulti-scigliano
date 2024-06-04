package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class GameListController extends GenericSceneController{



    @FXML
    private VBox gameListContainer;

    private List<SocketRmiControllerObject> games;

    public void initialize() throws IOException, ClassNotFoundException, InterruptedException {


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

    // Create a button for each game and add it to the VBox
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
                }
            });
            gameListContainer.getChildren().add(button);
        }
    }

    private void handleGameButtonClick(SocketRmiControllerObject game) throws IOException, InterruptedException {
        super.client.getTerminal_interface().waitFullGame();
    }

}
