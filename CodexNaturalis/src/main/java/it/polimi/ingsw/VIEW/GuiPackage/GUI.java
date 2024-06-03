package it.polimi.ingsw.VIEW.GuiPackage;

import it.polimi.ingsw.VIEW.GraficInterterface;
import javafx.application.Platform;

import java.io.IOException;
import java.rmi.NotBoundException;

public class GUI implements GraficInterterface {

    SceneController scene;

    public GUI(SceneController scene) {
        this.scene = scene;
    }

    @Override
    public void runCli() throws IOException, InterruptedException, NotBoundException, ClassNotFoundException {
        selectNamePlayer();
    }

    @Override
    public String selectNamePlayer() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        Platform.runLater(() -> scene.changeRootPane("login_scene.fxml"));
        return null;
    }

    @Override
    public void gameAccess(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {

    }

    @Override
    public void waitFullGame() throws IOException, InterruptedException {

    }

    @Override
    public void chooseGoalState() throws IOException, InterruptedException, ClassNotFoundException {

    }

    @Override
    public void chooseStartingCardState() throws IOException, InterruptedException, ClassNotFoundException {

    }

    @Override
    public void manageGame() throws IOException, InterruptedException, ClassNotFoundException {

    }
}
