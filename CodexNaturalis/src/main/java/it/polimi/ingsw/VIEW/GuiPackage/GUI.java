package it.polimi.ingsw.VIEW.GuiPackage;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.VIEW.GraficInterterface;
import javafx.application.Platform;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class GUI implements GraficInterterface {

    public boolean newClient;
    SceneController scene;
    String username;
    private String token;

    public GUI(SceneController scene) {
        this.scene = scene;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void printError(String a) {
        scene.showAlert("ERROR!",a);
    }

    @Override
    public void runCli() throws IOException, InterruptedException, NotBoundException, ClassNotFoundException {
        selectNamePlayer();
    }

    public void setNewClient(boolean newClient) {
        this.newClient = newClient;
    }

    @Override
    public String selectNamePlayer() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        Platform.runLater(() -> scene.changeRootPane("login_scene.fxml"));
        return null;
    }

    @Override
    public void gameAccess(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        System.out.println(player_name + " questo è l'username");
        username = player_name;
        if(newClient) {
            makeChoice(player_name);
          //  scene.showMessage("Success","YOUR PLAYER HAS BEEN CREATED!");
        }
    }

    private void makeChoice(String player_name) throws NotBoundException, IOException, ClassNotFoundException, InterruptedException {
        if (!scene.getClient().areThereFreeGames()) {
            newGame(player_name,false);
            return;
        }
        int done=0;
        Platform.runLater(() -> scene.changeRootPane("choose_new_or_old.fxml"));

        /*while(done==0) {
            System.out.println("\n-'new' CREATE NEW GAME \n-'old' JOIN EXISTING GAME");
            String decision = scan.nextLine();
            if (decision.equalsIgnoreCase("old")) {
                done = 1;
                chooseMatch(player_name);
            } else if (decision.equalsIgnoreCase("new")) {
                done=1;
                newGame(player_name,true);
            } else {
                System.out.println("\n[ERROR] WRONG INSERT!");
            }
        }*/
    }

    public void newGame(String player_name, boolean empty) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        if( !empty ) scene.showMessage("Message","There aren't existing games!");
        Platform.runLater(() -> scene.changeRootPane("game_access.fxml"));
    }

    @Override
    public void chooseMatch(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        Platform.runLater(() -> scene.changeRootPane("game_list_scene.fxml"));
    }


    @Override
    public void waitFullGame() throws IOException, InterruptedException {
        Platform.runLater(() -> scene.changeRootPane("waiting_player.fxml"));
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

    @Override
    public String getName() {
        return this.username;
    }

    public void setToken(String token){this.token =token;}

    public void buffering() throws  InterruptedException{}
}
