package it.polimi.ingsw.VIEW;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import org.fusesource.jansi.AnsiConsole;
import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawCenter;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawGold;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawResource;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.clientSocket;
import it.polimi.ingsw.StringCostant;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class implements the Text User Interface (TUI) for the game.
 * It extends the `GraficInterterface` to provide text-based interaction with the game.
 *
 * @see GraficInterterface
 */
public class TUI implements Serializable, GraficInterterface {

    private final VirtualViewF client;

    private final StringCostant stringcostant = new StringCostant();
    private boolean newClient;
    private String token;
    public List<Integer> id_games = new ArrayList<>();

    public TUI(VirtualViewF client) throws NotBoundException, IOException, InterruptedException, ClassNotFoundException {
        this.client = client;

    }

    /**
     * print an error
     *
     * @param error
     */
    @Override
    public void printError(String error) {
        System.err.println(error);
    }

    @Override
    public void printUpdateMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void setUsername(String username) {

    }

    /**
     * Runs the command-line interface (CLI) for the game.
     *
     * @throws IOException  If an I/O error occurs during communication.
     * @throws InterruptedException If the connection thread is interrupted.
     * @throws NotBoundException   If the remote object is not bound (relevant for RMI communication).
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     */
    public void runCli() throws IOException, InterruptedException, NotBoundException, ClassNotFoundException {
        AnsiConsole.systemInstall();
        String player_name = selectNamePlayer();
        gameAccess(player_name);
        waitFullGame();
        chooseGoalState();
        chooseStartingCardState();
        manageGame();
    }

    /**
     * Waits for other players to join the game and sets the game field model.
     *
     * @throws IOException  If an I/O error occurs during communication.
     * @throws InterruptedException If the connection thread is interrupted.
     */
    public void waitFullGame() throws IOException, InterruptedException {
        if(client.getMiniModel().getState().equals("NOT_INITIALIZED")) {
            System.out.print("[WAIT FOR OTHER PLAYERS]\n");
            while (client.getMiniModel().getState().equals("NOT_INITIALIZED"))
                buffering();
            System.out.println("\n[GAME IS FULL, YOU ARE ABOUT TO START]!\n");
        }
        client.setGameFieldMiniModel();
        //startCheckingMessages();
    }

    /**
     * Prompts the user to select a player name and validates it with the serverSocket.
     *
     * @return The chosen player name as a String.
     * @throws IOException
     * @throws NotBoundException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public String selectNamePlayer() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        String player_name ;
        int flag;
        do{
            System.out.print(stringcostant.choose_name_player);

            player_name = scan.nextLine();

            flag = client.checkName(player_name);
            if(flag==0){
                System.out.println(stringcostant.name_is_not_valid);
                newClient=true;
            }
            else if(flag==2) {
                System.out.println(player_name + " RECONNECTED!");
                newClient=false;
            }
            else{
                newClient=true;
            }
        } while(flag==0);
        return player_name;
    }

    public void setNewClient(boolean newClient) {
        this.newClient = newClient;
    }

    @Override
    public boolean getIsAlone() {
        return false;
    }

    /**
     * Handles game access based on whether the player is new or reconnecting.
     *
     * @param player_name The chosen player name.
     * @throws IOException  If an I/O error occurs during communication.
     * @throws NotBoundException   If the remote object is not bound (relevant for RMI communication).
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the connection thread is interrupted.
     */
    public void gameAccess(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        if(newClient) {
            makeChoice(player_name);
            System.out.print("[SUCCESS] YOUR PLAYER HAS BEEN CREATED!\n");
        }
    }

    /**
     * Prompts the user to choose between creating a new game or joining an existing one.
     *
     * @param player_name The chosen player name.
     * @throws IOException  If an I/O error occurs during communication.
     * @throws NotBoundException   If the remote object is not bound (relevant for RMI communication).
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the connection thread is interrupted.
     */

    private void makeChoice(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        if (!client.areThereFreeGames()) {
            newGame(player_name,false);
            return;
        }
        Scanner scan = new Scanner(System.in);
        int done=0;
        while(done==0) {
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
        }
    }

    /**
     * Handles creating a new game on the serverSocket.
     *
     * @param player_name The chosen player name.
     * @param empty Whether there are existing games available (informs the user).
     * @throws IOException  If an I/O error occurs during communication.
     * @throws NotBoundException   If the remote object is not bound (relevant for RMI communication).
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the connection thread is interrupted.
     */
    public void newGame(String player_name, boolean empty) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        if( !empty ) System.out.println("\nTHERE AREN'T EXISTING GAMES");
        System.out.print("\nCHOOSE GAME NAME  > ");
        String game_name = scan.nextLine();
        int numplayers;
        boolean flag = false;
        do {
            System.out.print("\nCHOOSE NUMBER OF PLAYERS ( 2 - 4 ) > ");
            numplayers = scan.nextInt();
            if(numplayers>=2 && numplayers<=4){
                client.createGame(game_name,numplayers, player_name);
                flag=true;
            }
            else{
                System.out.println("[ERROR] WRONG INSERT");
            }
        } while(!flag);
    }

    /**
     * Prompts the user to choose an existing game to join.
     *
     * @param player_name The chosen player name.
     * @throws IOException  If an I/O error occurs during communication.
     * @throws NotBoundException   If the remote object is not bound (relevant for RMI communication).
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the connection thread is interrupted.
     */
    public void chooseMatch(String player_name) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        boolean check = true;
        System.out.println("\nEXISTING GAMES: ");
        List<SocketRmiControllerObject> games = client.getFreeGames();
        for (SocketRmiControllerObject r : games) {
            System.out.println(r.name + " ID:" + r.ID
                    + " " + r.num_player + "/" + r.max_num_player);
            id_games.add(r.ID);
        }
        do {
            if( !check ) {makeChoice(player_name); check = true;}
            else{System.out.println("\nINSERT GAME ID > ");
                int ID = scan.nextInt();
                check = client.findRmiController(ID, player_name);
                }
        }while(!check);

        client.connectGameServer();
    }

    @Override
    public boolean getInGame() {
        return false;
    }

    @Override
    public void endGame() {

    }

    /**
     * Waits for the "CHOOSE_GOAL" state and prompts the user to choose a goal card if necessary.
     *
     * @throws IOException  If an I/O error occurs during communication.
     * @throws InterruptedException If the connection thread is interrupted.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     */
    public void chooseGoalState() throws IOException, InterruptedException, ClassNotFoundException {
        while( client.getMiniModel().getState().equals("NOT_IN_A_GAME") ){ buffering();}
        if(client.getMiniModel().getState().equals("CHOOSE_GOAL")) {
            boolean checkGoal = client.isGoalCardPlaced();

            if(checkGoal){
                chooseGoal();
                System.out.println("\nYOU CHOOSE :" + client.getGoalPlaced());
            }
            while (client.getMiniModel().getState().equals("CHOOSE_GOAL")) {
                buffering();
            }
        }
    }

    /**
     * Prompts the user to choose a goal card from the two available options.
     *
     * @throws IOException  If an I/O error occurs during communication.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the connection thread is interrupted.
     */
    private void chooseGoal() throws IOException, ClassNotFoundException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        int done=0;
        while(done==0) {
            String first_goal = client.getFirstGoal();
            String second_goal = client.getSecondGoal();
            System.out.println("\nCHOOSE YOUR GOAL:\n 1-" + first_goal
                    + "\n 2-" + second_goal);
            String choice = scan.nextLine();
            if (choice.equals("1")) {
                done=1;
                client.chooseGoal(0);
            } else if (choice.equals("2")){
                done=1;
                client.chooseGoal(1);
            } else System.out.println("[ERROR] WRONG INSERT!");
        }
    }

    /**
     * Waits for the "CHOOSE_SIDE_FIRST_CARD" state and prompts the user to choose a starting card side if necessary.
     *
     * @throws IOException  If an I/O error occurs during communication.
     * @throws InterruptedException If the connection thread is interrupted.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     */
    public void chooseStartingCardState() throws IOException, InterruptedException, ClassNotFoundException {
        if(client.getMiniModel().getState().equals("CHOOSE_SIDE_FIRST_CARD")) {
            if(!client.isFirstPlaced()) {
                chooseStartingCard();
            }
            while (client.getMiniModel().getState().equals("CHOOSE_SIDE_FIRST_CARD")) {
                buffering();
            }
        }
    }

    /**
     * Prompts the user to choose a starting card side (front or back) if it's their turn.
     *
     * @throws IOException  If an I/O error occurs during communication.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     * @throws InterruptedException If the connection thread is interrupted.
     */
    private void chooseStartingCard() throws IOException, ClassNotFoundException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nCHOOSE STARTING CARD SIDE:\n");
        client.showStartingCard();
        int done=0;
        while(done==0){
            System.out.println("\n-'B' FOR BACK SIDE \n-'F' FOR FRONT SIDE:");
            String dec = scan.nextLine();
            if (dec.equals("F")||dec.equals("f")){
                done=1;
                client.chooseStartingCard(false);
            } else if (dec.equals("B")||dec.equals("b")){
                done=1;
                client.chooseStartingCard(true);
            }
            else System.out.println("[ERROR] WRONG INSERT!");
        }
    }

    /**
     * Manages the game loop, handling different game states and user interactions.
     *
     * @throws IOException  If an I/O error occurs during communication.
     * @throws InterruptedException If the connection thread is interrupted.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     */
    public void manageGame() throws IOException, InterruptedException, ClassNotFoundException {
        while( !client.getMiniModel().getState().equals("END_GAME") ){
            while (client.getMiniModel().getState().equals("WAIT_TURN")) {
                menuChoice("CONTINUE ", client.getMiniModel().getState());
                buffering();
            }

            if (client.getMiniModel().getState().equals("PLACE_CARD")) {
                selectAndInsertCard();}
            else if (client.getMiniModel().getState().equals("DRAW_CARD")) {
                menuChoice("DRAW CARD", client.getMiniModel().getState());
                drawCard();
                System.out.println("\n           END OF YOUR TURN !");
            }
            //System.out.println("YOU CURRENTLY HAVE [ " + client.getMiniModel().getMyGameField().getPlayerPoints() + " ] POINTS !");
            client.manageGame(false);
        }
        client.manageGame(true);
    }

    @Override
    public void startCountdown(String message, boolean still_alone, boolean win)  {

    }

    @Override
    public String getName() {
        return null;
    }

    /**
     * Handles selecting and placing a card from the user's deck during their turn.
     *
     * @throws IOException  If an I/O error occurs during communication.
     * @throws InterruptedException If the connection thread is interrupted.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     */
    private void selectAndInsertCard() throws IOException, InterruptedException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        int choice=-1,x,y;
        String flip;
        boolean flipped;
        boolean wrong = false;
        while ( client.getMiniModel().getState().equals("PLACE_CARD") ){
            menuChoice("PLACE CARD",client.getMiniModel().getState());
            do{
                if( wrong ) System.err.println("[ERROR] INCORRECT INSERT");
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
                wrong = !(choice>=1 && choice<=3) ||
                        !(flip.equals("B") || flip.equals("F")||flip.equals("b")||flip.equals("f") ) ||
                        !(x>=0 && x< Constants.MATRIXDIM && y>=0 && y<Constants.MATRIXDIM );
            }while( wrong );
            client.selectAndInsertCard(choice,x,y,flipped);
        }
    }

    /**
     * Handles drawing a card during the user's turn.
     *
     * @throws IOException  If an I/O error occurs during communication.
     * @throws InterruptedException If the connection thread is interrupted.
     * @throws ClassNotFoundException If a class used during communication cannot be found (relevant for RMI communication).
     */
    private void drawCard() throws IOException, InterruptedException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n DRAW A CARD FROM: ");
        boolean goldDeck_present = client.isGoldDeckPresent();
        boolean resourceDeck_present = client.isResourceDeckPresent();

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
            if(client instanceof clientSocket) token_client = client.getToken();
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
        client.drawCard(function);
    }

    /**
     * Displays a menu based on the current game state and handles user choices.
     *
     * @param message  The message to display at the top of the menu.
     * @param current_state The current state of the game as reported by the serverSocket.
     * @throws IOException If an I/O error occurs during communication.
     */
    private void menuChoice(String message, String current_state) throws IOException {
        Scanner scan = new Scanner(System.in);
            client.getMiniModel().printMenu(message);
            int choice = scan.nextInt();

            switch (choice) {
                case (0):
                    String player_name = client.getMiniModel().printNumToField();
                    if (player_name == null) System.out.println("Player not found");
                    else client.getMiniModel().showGameField(player_name);
                    break;
                case (1):
                    client.getMiniModel().showCards();
                    break;
                case (2):
                    client.getMiniModel().uploadChat();
                    int decision;
                    do {
                        decision = scan.nextInt();
                        scan.nextLine();
                    } while (!chatChoice(decision));
                    break;
                case (3):
                    if( client.getMiniModel().getState().equals(PlayerState.WAIT_TURN.toString()) ){
                        System.out.println(" IT'S NOT YOUR TURN YET, WAIT FOR THE NOTIFICATION OF END TURN ");
                        menuChoice(message, current_state);
                    }
                    return;
                default:
                    System.err.println("[ERROR] WRONG INSERT");
            }
            menuChoice(message, current_state);
    }

    /**
     * Handles user interaction within the chat sub-menu.
     *
     * @param decision The initial decision passed from the main menu choice.
     * @return True if the chat sub-menu is closed successfully, false otherwise.
     * @throws IOException If an I/O error occurs during communication.
     */
    private boolean chatChoice(int decision) throws IOException {
        client.getMiniModel().setStop_chat(false);
        Scanner scan = new Scanner(System.in);
        if(!client.getMiniModel().showchat(decision)){
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
                client.ChatChoice(message, decision);
                System.out.println("[SUCCESS] MESSAGE SENT!");
                choice = 0;
            } else {
                client.getMiniModel().setStop_chat(true);
                return true;}
        }
    }

    /**
     * This method attempts to display cards in the center of the user's screen.
     * The specific implementation depends on the type of client object.
     *
     * @throws IOException Signals an I/O exception that might occur during communication.
     * @throws ClassNotFoundException Indicates that a class needed for the operation could not be found.
     * @throws InterruptedException  If the calling thread is interrupted while waiting.
     */
    private void showCardsInCenter() throws IOException, ClassNotFoundException, InterruptedException {
        int i = 1;
        for( PlayCard p :  client.getMiniModel().getCards_in_center().getGold_list() ){
            System.out.println(i +" GOLD  ");
            client.getMiniModel().showCard(p);
            i++;
        }
         i = 1;
        for( PlayCard p :  client.getMiniModel().getCards_in_center().getResource_list() ){
            System.out.println(i +" RESOURCE  ");
            client.getMiniModel().showCard(p);
            i++;
        }

    }

    /**
     * This method simulates a basic buffering effect
     *
     * @throws InterruptedException  If the calling thread is interrupted while waiting.
     */
    public void buffering() throws  InterruptedException{
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("/");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("|");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("\\");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("-");
    }

    public void setToken(String token){this.token =token;}

}
