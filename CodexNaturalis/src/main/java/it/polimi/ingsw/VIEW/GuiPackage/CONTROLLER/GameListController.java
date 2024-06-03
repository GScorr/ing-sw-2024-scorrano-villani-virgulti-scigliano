package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
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
        while(client == null){
            Thread.sleep(500);
            System.out.println("ciao");
        }
        System.out.println("esco");
        // Call the client method to get the list of free games
        games = client.getFreeGames();

        // Create a button for each game and add it to the VBox
        for (SocketRmiControllerObject game : games) {
            Button button = new Button(game.name + " - Players: " + game.num_player + "/" + game.max_num_player);
            button.setOnAction(event -> handleGameButtonClick(game));
            gameListContainer.getChildren().add(button);
        }
    }

    private void handleGameButtonClick(SocketRmiControllerObject game) {
        // Handle button click, e.g., join the selected game
        System.out.println("Joining game: " + game.name);
        // You can add more logic here to join the selected game
    }
}
