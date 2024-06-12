package it.polimi.ingsw.VIEW.GuiPackage;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawCenter;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawGold;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawResource;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.clientSocket;
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
        }else{
            this.waitFullGame();
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
    public boolean getInGame() {
        return false;
    }


    @Override
    public void waitFullGame() throws IOException, InterruptedException {
        Platform.runLater(() -> scene.changeRootPane("waiting_player.fxml"));
    }

    @Override
    public void chooseGoalState() throws IOException, InterruptedException, ClassNotFoundException {
        if(scene.getClient().getMiniModel().getState().equals("CHOOSE_GOAL")) {
            boolean checkGoal = scene.getClient().isGoalCardPlaced();
                chooseGoal();
        }else{
            this.chooseStartingCardState();
        }
    }
    private void chooseGoal() throws IOException, ClassNotFoundException, InterruptedException {
        Platform.runLater(() -> scene.changeRootPane("choose_goal_state.fxml"));
    }

    @Override
    public void chooseStartingCardState() throws IOException, InterruptedException, ClassNotFoundException {
        if(scene.getClient().getMiniModel().getState().equals("CHOOSE_SIDE_FIRST_CARD")) {
            chooseStartingCard();
        }else{
            this.manageGame();
        }
    }

    private void chooseStartingCard() throws IOException, ClassNotFoundException, InterruptedException {
        Platform.runLater(() -> scene.changeRootPane("starting_card.fxml"));
    }

    public void place_card(){
        Platform.runLater(() -> scene.changeRootPane("game2.fxml"));
    }

    public void wait_turn(){
        Platform.runLater(() -> scene.changeRootPane("game_wait.fxml"));
    }


    @Override
    public void manageGame() throws IOException, InterruptedException, ClassNotFoundException {
        if(!scene.getClient().getMiniModel().getState().equals("END_GAME") ){
           if( scene.getClient().getMiniModel().getState().equals("PLACE_CARD")  || scene.getClient().getMiniModel().getState().equals("DRAW_CARD") ) this.place_card();
           else this.wait_turn();
        }else{
            System.out.println("[END OF THE GAME]!\nFINAL SCORES:\n");
        }

//        while( !scene.getClient().getMiniModel().getState().equals("END_GAME") ){
//
//
//            while (scene.getClient().getMiniModel().getState().equals("WAIT_TURN")) {
//                menuChoice("GO IN WAITING MODE", scene.getClient().getMiniModel().getState());
//                buffering();
//            }
//            if (scene.getClient().getMiniModel().getState().equals("PLACE_CARD")) {selectAndInsertCard();}
//            else if (scene.getClient().getMiniModel().getState().equals("DRAW_CARD")) {
//                /*
//                    menuChoice("DRAW CARD", scene.getClient().getMiniModel().getState());
//                    drawCard();
//                 */
//                Platform.runLater(() -> scene.changeRootPane("game2.fxml"));
//                while ( scene.getClient().getMiniModel().getState().equals("DRAW_CARD")){
//                    Thread.sleep(1000);
//                }
//        }
//            System.out.println("\nEND OF YOUR TURN !");
//            scene.getClient().manageGame(false);
//        }

 //       System.out.println("[END OF THE GAME]!\nFINAL SCORES:\n");
  //      scene.getClient().manageGame(true);
        /*while( !client.getMiniModel().getState().equals("END_GAME") ){
            while (client.getMiniModel().getState().equals("WAIT_TURN")) {
                menuChoice("GO IN WAITING MODE", client.getMiniModel().getState());
                buffering();
            }
            if (client.getMiniModel().getState().equals("PLACE_CARD")) {selectAndInsertCard();}
            else if (client.getMiniModel().getState().equals("DRAW_CARD")) {
                menuChoice("DRAW CARD", client.getMiniModel().getState());
                drawCard();
            }
            System.out.println("\nEND OF YOUR TURN !");
            client.manageGame(false);
        }
        System.out.println("[END OF THE GAME]!\nFINAL SCORES:\n");
        client.manageGame(true);*/
    }

    private void menuChoice(String message, String current_state) throws IOException {
        Scanner scan = new Scanner(System.in);
        scene.getClient().getMiniModel().printMenu(message);
        int choice = scan.nextInt();
        switch (choice) {
            case (0):
                scene.getClient().getMiniModel().printNumToField();
                int i = scan.nextInt();
                scene.getClient().getMiniModel().showGameField(i);
                break;
            case (1):
                scene.getClient().getMiniModel().showCards();
                break;
            case (2):
                scene.getClient().getMiniModel().uploadChat();
                int decision;
                do {
                    decision = scan.nextInt();
                    scan.nextLine();
                } while (!chatChoice(decision));
                break;
            case (3):
                return;
            default:
                System.err.println("[ERROR] WRONG INSERT");
        }
        menuChoice(message, current_state);
    }

    private boolean chatChoice(int decision) throws IOException {
        scene.getClient().getMiniModel().setStop_chat(false);
        Scanner scan = new Scanner(System.in);
        if(!scene.getClient().getMiniModel().showchat(decision)){
            return false;
        }
        int choice = 0;
        while(true) {
            while (choice < 1 || choice > 2) {
                System.out.println("DO YOU WANT TO SEND A MESSAGE?     1- YES      2- NO [CLOSE CHAT] ");
                choice = scan.nextInt();
                scan.nextLine();
            }
            if (choice == 1) {
                System.out.println("INSERT MESSAGE: ");
                String message = scan.nextLine();
                scene.getClient().ChatChoice(message, decision);
                System.out.println("[SUCCESS] MESSAGE SENT!");
                choice = 0;
            } else {
                scene.getClient().getMiniModel().setStop_chat(true);
                return true;}
        }

    }


    private void selectAndInsertCard() throws IOException, InterruptedException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        int choice=-1,x,y;
        String flip;
        boolean flipped;
        while ( scene.getClient().getMiniModel().getState().equals("PLACE_CARD") ){
            menuChoice("PLACE CARD",scene.getClient().getMiniModel().getState());
            do{
                if(choice != -1) System.err.println("[ERROR] INCORRECT INSERT");
                System.out.println("\nCHOOSE CARD FROM YOUR DECK (1,2,3): ");
                String choicestring = scan.nextLine();
                choice = Integer.parseInt(choicestring);
                System.out.println("\nCHOOSE SIDE (B,F): ");
                flip = scan.nextLine();
                flipped = (flip.equals("B") || flip.equals("b"));
                System.out.println("\nCHOOSE COORDINATES: ");
                x = scan.nextInt();
                y = scan.nextInt();
                scan.nextLine();
            }while( !(choice>=1 && choice<=3) ||
                    !(flip.equals("B") || flip.equals("F")||flip.equals("b")||flip.equals("f") ) ||
                    !(x>=0 && x< Constants.MATRIXDIM && y>=0 && y<Constants.MATRIXDIM ));
            scene.getClient().selectAndInsertCard(choice,x,y,flipped);
        }
    }

    private void drawCard() throws IOException, InterruptedException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n DRAW A CARD FROM: ");
        boolean goldDeck_present = scene.getClient().isGoldDeckPresent();
        boolean resourceDeck_present = scene.getClient().isResourceDeckPresent();

        if(goldDeck_present){System.out.println("1. GOLD DECK");}
        if (resourceDeck_present){System.out.println("2. RESOURCE DECK");}
        System.out.println("3. CENTRAL CARDS DECK");
        int num = -1;
        SendFunction function = null;
        String token_client;
        do{
            if(num != -1) System.err.println("[ERROR] OUT OF BOUND");
            String numstring = scan.nextLine();
            num = Integer.parseInt(numstring);
            if(scene.getClient() instanceof clientSocket) token_client = scene.getClient().getToken();
            else token_client = token;
            switch (num) {
                case (1):
                    function = new SendDrawGold(token_client);
                    break;
                case (2):
                    function = new SendDrawResource(token_client);
                    break;
                case (3):
                    showCardsInCenter();
                    System.out.println("CHOSE CARD FROM CENTER (1/2/3 ) : ");
                    String choicestr = scan.nextLine();
                    int index = Integer.parseInt(choicestr);
                    function = new SendDrawCenter(token_client, index - 1);
                    break;
            }

        }while ( num < 0 || num > 3);
        scene.getClient().drawCard(function);
    }

    private void showCardsInCenter() throws IOException, ClassNotFoundException, InterruptedException {
        if(scene.getClient() instanceof clientSocket){
            scene.getClient().showCardsInCenter();
        }
        else{
            scene.getClient().getGameServer().showCardsInCenter(token);}
    }

    @Override
    public String getName() {
        return this.username;
    }

    public void setToken(String token){this.token =token;}

    public void buffering() throws  InterruptedException{}
}
