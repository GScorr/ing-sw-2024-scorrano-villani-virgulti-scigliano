package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.*;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.StringCostant;
import it.polimi.ingsw.VIEW.GraficInterterface;
import it.polimi.ingsw.VIEW.GuiPackage.GUI;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import it.polimi.ingsw.VIEW.TUI;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

/**
 * Connects to the RMI server and provides an interface for client-side game interactions.
 */
public class RmiClientF extends UnicastRemoteObject implements VirtualViewF {
    final VirtualServerF server;
    private String token;
    private VirtualGameServer rmi_controller;
    private final MiniModel miniModel =  new MiniModel();
    private final StringCostant stringcostant = new StringCostant();
    private TUI tui;
    private GraficInterterface terminal_interface;

    @Override
    public void runGUI(SceneController scene) throws IOException, ClassNotFoundException, InterruptedException, NotBoundException {
        this.server.connect(this);
        terminal_interface = new GUI(scene);
        terminal_interface.runCli();
    }

    public RmiClientF(VirtualServerF server) throws IOException {
        this.server = server;
    }

    public void run() throws IOException, InterruptedException, NotBoundException, ClassNotFoundException {
        this.server.connect(this);
        terminal_interface = new TUI(this);
        runCli();
    }


    private void runCli() throws NotBoundException, IOException, InterruptedException, ClassNotFoundException {
        terminal_interface.runCli();
    }

    public void disconect() throws IOException, ClassNotFoundException, InterruptedException, NotBoundException {
        System.exit(0);
    }

    /**
     * Verifies a player's name with the server and establishes a connection (if valid).
     *
     * @param player_name The name of the player to be checked.
     * @return An integer flag indicating the connection status:
     *         - 1: New player - name is valid and a new token is created.
     *         - 0: Existing player with invalid name.
     *         - 2: Existing player with valid name - connection is established and heartbeats begin.
     * @throws IOException  If an I/O error occurs during communication with the server.
     * @throws NotBoundException If the name cannot be bound to a remote object.
     * @throws InterruptedException If the heartbeating thread is interrupted.
     */
    public int checkName(String player_name) throws IOException, NotBoundException, InterruptedException {
        int flag;
            String isnew = server.checkName(player_name,this);
        if(isnew.equals("true")) {
            flag = 1;
            this.token = server.createToken(this);}
        else if(isnew.equals("false")){
            flag=0;
            terminal_interface.printError(stringcostant.name_is_not_valid);
            }
        else{
            this.token = isnew;
            int port = server.getPort(token);
            Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
            this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
            flag=2;
            startCheckingMessages();
        }

        startSendingHeartbeats();
        terminal_interface.setToken(token);
        return flag;
    }

    /**
     * Checks if there are any free games available on the server.
     *
     * @return True if there are free games available, false otherwise.
     * @throws IOException  If an I/O error occurs during communication with the server.
     */
    public boolean areThereFreeGames () throws IOException {
        return server.getFreeGames() != null && !server.getFreeGames().isEmpty();
    }

    public List<SocketRmiControllerObject> getFreeGames() throws IOException {return server.getFreeGamesSocket();}

    /**
     * Attempts to create a new game on the server.
     *
     * @param game_name The name of the game to be created.
     * @param numplayers The desired number of players for the game.
     * @param player_name The name of the player creating the game.
     * @throws IOException  If an I/O error occurs during communication with the server.
     * @throws NotBoundException If the name cannot be bound to a remote object.
     * @throws InterruptedException If the message checking thread is interrupted.
     */    public void createGame(String game_name, int numplayers, String player_name) throws IOException, NotBoundException, InterruptedException {
        int port;
        port = server.createGame(game_name, numplayers, token, player_name,this);
        Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
        this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
        startCheckingMessages();
    }

    /**
     * Requests game information from the server based on the game state.
     *
     * @param endgame A flag indicating whether the game has ended.
     * @throws IOException  If an I/O error occurs during communication with the server.
     */
    public void manageGame(boolean endgame) throws IOException {
        if(!endgame) rmi_controller.getPoints(token);
        else rmi_controller.getFinalStandings(token);
    }

    /**
     * Sends a request to the server to insert a card into the game.
     *
     * @param choice The chosen card index (presumably adjusted by -1).
     * @param x The X-coordinate for the card placement (optional).
     * @param y The Y-coordinate for the card placement (optional).
     * @param flipped Whether the card should be placed face down (flipped).
     * @throws IOException  If an I/O error occurs during communication with the server.
     * @throws InterruptedException If the sleep thread is interrupted.
     */
    public void selectAndInsertCard(int choice, int x, int y, boolean flipped) throws IOException, InterruptedException {
        SendFunction function = new SendInsertCard(token, choice-1, x,y,flipped);
        rmi_controller.addQueue(function);
        Thread.sleep(750);
    }

    /**
     * Sends a request to the server to draw a card in the game.
     *
     * @param function A pre-constructed SendFunction object for drawing a card.
     * @throws IOException  If an I/O error occurs during communication with the server.
     * @throws InterruptedException If the sleep thread is interrupted.
     */
    public void drawCard(SendFunction function) throws IOException, InterruptedException {
        rmi_controller.addQueue(function);
        Thread.sleep(750);
    }

    /**
     * Attempts to locate the RMI controller for a specific game
     *
     * @param id The identifier of the game to find.
     * @param player_name The player's name.
     * @return True if the RMI controller is found, false otherwise.
     * @throws IOException  If an I/O error occurs during communication with the server.
     * @throws InterruptedException If the process is interrupted.
     */
    public boolean findRmiController(int id, String player_name) throws IOException, InterruptedException {
        return server.findRmiController(id, token, player_name,this);
    }

    /**
     * Establishes an RMI connection to the game server based on the player's token.
     *
     * @throws IOException  If an I/O error occurs during communication with the server.
     * @throws NotBoundException If the name cannot be bound to a remote object (RMI lookup issue).
     */
    public void connectGameServer() throws IOException, NotBoundException {
        int port = server.getPort(token);
        Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
        this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
        startCheckingMessages();
    }

    /**
     * Checks if the player with the current token has placed a goal card.
     *
     * @return True if no goal card is placed, false otherwise.
     * @throws IOException  If an I/O error occurs during communication with the RMI server.
     */
    public boolean isGoalCardPlaced() throws IOException {
        return rmi_controller.getTtoP().get(token).getGoalCard() == null;
    }

    public String getGoalPlaced() throws IOException {
        return rmi_controller.getTtoP().get(token).getGoalCard().toString();
    }

    public GraficInterterface getTerminal_interface() throws IOException{
        return terminal_interface;
    }

    @Override
    public String getFirstGoal() throws IOException {
        return rmi_controller.getTtoP().get(this.token).getInitial_goal_cards().get(0).toString();
    }
    public Goal getFirstGoalCard() throws IOException {
        return rmi_controller.getTtoP().get(this.token).getInitial_goal_cards().get(0);
    }

    public Goal getSecondGoalCard() throws IOException {
        return rmi_controller.getTtoP().get(this.token).getInitial_goal_cards().get(1);
    }

    @Override
    public String getSecondGoal() throws IOException {
        return rmi_controller.getTtoP().get(this.token).getInitial_goal_cards().get(1).toString();
    }

    /**
     * Sends a request to the server to choose a goal for the player.
     *
     * @param i The index of the chosen goal.
     * @throws IOException  If an I/O error occurs during communication with the RMI server.
     * @throws InterruptedException If the process is interrupted.
     */
    @Override
    public void chooseGoal(int i) throws IOException, InterruptedException {
        rmi_controller.chooseGoal(token,i);
    }

    /**
     * Sends a request to the server to reveal the starting card for the player.
     *
     * @throws IOException  If an I/O error occurs during communication with the RMI server.
     */
    @Override
    public void showStartingCard() throws IOException {
        rmi_controller.showStartingCard(token);
    }

    /**
     * Requests the starting card information from the server.
     *
     * @return A PlayCard object containing information about the starting card (if successful), or null otherwise.
     * @throws IOException  If an I/O error occurs during communication with the RMI server.
     */
    public PlayCard showStartingCardGUI() throws IOException{
       return rmi_controller.showStartingCardGUI(token);
    }

    /**
     * Sends a request to the server indicating the player's choice regarding the starting card.
     *
     * @param b A boolean value representing the player's decision.
     * @throws IOException  If an I/O error occurs during communication with the RMI server.
     * @throws InterruptedException If the process is interrupted.
     */
    @Override
    public void chooseStartingCard(boolean b) throws IOException, InterruptedException {
        rmi_controller.chooseStartingCard(token,b);
    }

    /**
     * Checks if the player with the current token is the first player to place a card.
     *
     * @return True if the player is the first to place a card, false otherwise.
     * @throws IOException  If an I/O error occurs during communication with the RMI server.
     */
    @Override
    public boolean isFirstPlaced() throws IOException {
        return rmi_controller.getTtoP().get(token).isFirstPlaced();
    }

    @Override
    public String getToken()  {
        return this.token;
    }

    /**
     * Checks if there are any cards remaining in the gold deck on the server.
     *
     * @return True if there are cards in the gold deck, false otherwise.
     * @throws IOException  If an I/O error occurs during communication with the RMI server.
     */
    @Override
    public boolean isGoldDeckPresent() throws IOException {
        return rmi_controller.getController().getGame().getGold_deck().getNumber()>0;
    }

    /**
     * Checks if there are any cards remaining in the resource deck on the server.
     *
     * @return True if there are cards in the resource deck, false otherwise.
     * @throws IOException  If an I/O error occurs during communication with the RMI server.
     */
    @Override
    public boolean isResourceDeckPresent() throws IOException {
        return rmi_controller.getController().getGame().getResources_deck().getNumber()>0;
    }

    /**
     * Continuously checks for incoming messages from the game server and executes their actions.
     *
     */
    public void startCheckingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    ResponseMessage s = miniModel.popOut();
                    if(s!=null) s.action();
                } catch (IOException e) {throw new RuntimeException(e);}
                try {Thread.sleep(200);} catch (InterruptedException e) {}
            }
        }).start();
    }

    /**
     * Sends periodic heartbeats to the server to maintain connection and monitors for disconnection.
     */
    public void startSendingHeartbeats() {
        new Thread(() -> {
            int cracked = 0;
            while (true) {
                try {
                    Thread.sleep(150);
                    server.receiveHeartbeat(token);
                } catch (IOException | InterruptedException e) {
                        System.err.println("\n              [SERVER ERROR] \n           SERVER DISCONNECTED");
                        while(true) {
                            try {terminal_interface.buffering();} catch (InterruptedException ignored) {}
                    }
                }
            }
        }).start();
    }

    public void setGameFieldMiniModel() throws IOException {miniModel.setGameField(rmi_controller.getGameFields(token));}

    /**
     * setter
     */

    public void setGameField(List<GameField> games){miniModel.setGameField(games);}
    public void setCards(List<PlayCard> cards){miniModel.setCards(cards);}
    public void pushBack(ResponseMessage message){miniModel.pushBack(message);}
    public void setState(String state){ miniModel.setState(state);}

    @Override
    public void setCenterCards(CenterCards cards, PlayCard res , PlayCard gold) throws IOException {
        miniModel.setCardsInCenter(cards,res,gold);
    }

    public void setNumToPlayer(HashMap<Integer, String> map){miniModel.setNumToPlayer(map);}

    /**
     * Sends a chat message to the server based on the number of players and the chosen recipient.
     *
     * @param message The text content of the chat message.
     * @param decision An integer representing the chosen recipient.
     * @throws IOException  If an I/O error occurs during communication with the RMI server.
     */
    public void ChatChoice(String message, int decision) throws IOException {
        if(miniModel.getNum_players()!=2) {
            if (decision == miniModel.getNum_players() + 1) {
                rmi_controller.chattingGlobal(new ChatMessage(message, miniModel.getMy_player()));
            } else {
                rmi_controller.chattingMoment(miniModel.getMy_index(), decision, new ChatMessage(message, miniModel.getMy_player()));
            }
        }
        else{
            rmi_controller.chattingGlobal(new ChatMessage(message, miniModel.getMy_player()));
        }
    }

    @Override
    public void showUpdate(GameField gamefield) {}

    @Override
    public void reportError(String details) {System.err.print("\n[ERROR] " + details + "\n> ");}

    @Override
    public void reportMessage(String details) {System.out.print("\n[ERROR] " + details + "\n> ");}

    public MiniModel getMiniModel() {return miniModel;}

    public VirtualGameServer getGameServer(){ return rmi_controller;}

    public void showCardsInCenter() throws IOException {rmi_controller.showCardsInCenter(token);}

    public void printString(String s) {System.out.println(s);}

    public void addChat(int idx, ChatMessage message) throws IOException{
        miniModel.addChat(idx, message);
    }

    public void insertId(int id) throws IOException{
        miniModel.setMy_index(id);
    }

    public void insertNumPlayers(int numPlayersMatch) throws IOException{
        miniModel.setNum_players(numPlayersMatch);
    }

    public void insertPlayer(Player player) throws IOException{
        miniModel.setMy_player(player);
    }

    /**
     * Displays a textual representation of a card, including the back and front sides with relevant information.
     * @param card The PlayCard object containing card data (front and back sides).
     */
    @Override
    public void showCard(PlayCard card) {
        Side back = card.getBackSide();
        Side front = card.getFrontSide();

        System.out.println("BACK SIDE\n----------------------------");
        System.out.println( " | " + back.getAngleLeftUp().toString().substring(0,2)  +   " |               "+ " | " + back.getAngleRightUp().toString().substring(0,2) + " |\n " );

        System.out.println( " |       | " + back.getCentral_resource().toString().substring(0,2) + back.getCentral_resource2().toString().substring(0,2) + back.getCentral_resource3().toString().substring(0,2) + " |         |\n " );
        System.out.println( " | " + back.getAngleLeftDown().toString().substring(0,2) +  " |               " + " | " + back.getAngleRightDown().toString().substring(0,2) + " |\n " );
        System.out.println("----------------------------\n\n");

        System.out.println("FRONT SIDE\n----------------------------");

        if(card instanceof ResourceCard) {
            System.out.println(" | " + card.getPoint() + " | ");
            if (card instanceof GoldCard) {
                System.out.println(" | " + ((GoldCard) card).getPointBonus().toString().substring(0, 2) + " | " + "             | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
            } else {
                System.out.println(" | " + front.getAngleLeftUp().toString().substring(0, 2) + " | " + "              | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
            }
        }
        else {
            System.out.println(" | " + front.getAngleLeftUp().toString().substring(0, 2) + " | " + "              | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
        }
        System.out.println( " |       | " + front.getCentral_resource().toString().substring(0,2) + front.getCentral_resource2().toString().substring(0,2) + front.getCentral_resource3().toString().substring(0,2) + " |        |\n " );
        if ( card instanceof GoldCard ){
            System.out.println( " | " + front.getAngleLeftDown().toString().substring(0,2) + " | " +
                    "  " + card.getCostraint().toString()  + " | " + front.getAngleRightDown().toString().substring(0,2) + " |\n ");
        }else{
            System.out.println( " | " + front.getAngleLeftDown().toString().substring(0,2) + " |              " + " | " + front.getAngleRightDown().toString().substring(0,2) + " |\n " );
        }
        System.out.println("----------------------------\n\n");

    }

    /**
     * Analyzes a game field and displays a textual representation with row/column headers, indicating filled cells and their contents.
     * @param field The GameField object representing the game board.
     */
    public void showField(GameField field) {
        boolean[] nonEmptyRows = new boolean[Constants.MATRIXDIM];
        boolean[] nonEmptyCols = new boolean[Constants.MATRIXDIM];


        for (int i = 0; i < Constants.MATRIXDIM; i++) {
            for (int j = 0; j < Constants.MATRIXDIM; j++) {
                if (field.getCell(i, j, Constants.MATRIXDIM).isFilled()) {
                    nonEmptyRows[i] = true;
                    nonEmptyCols[j] = true;


                    if (i > 0) nonEmptyRows[i - 1] = true;
                    if (i < Constants.MATRIXDIM - 1) nonEmptyRows[i + 1] = true;
                    if (j > 0) nonEmptyCols[j - 1] = true;
                    if (j < Constants.MATRIXDIM - 1) nonEmptyCols[j + 1] = true;
                }
            }
        }


        System.out.print("   ");
        for (int k = 0; k < Constants.MATRIXDIM; k++) {
            if (nonEmptyCols[k]) {
                System.out.print(k + " ");
            }
        }
        System.out.print("\n");


        for (int i = 0; i < Constants.MATRIXDIM; i++) {
            if (nonEmptyRows[i]) {
                System.out.print(i + " ");
                for (int j = 0; j < Constants.MATRIXDIM; j++) {
                    if (nonEmptyCols[j]) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).isFilled()) {
                            System.out.print(field.getCell(i, j, Constants.MATRIXDIM).getShort_value() + " ");
                        } else {
                            System.out.print("   ");
                        }
                    }
                }
                System.out.print("\n");
            }
        }
    }

    //MAIN
    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException, ClassNotFoundException {
        Registry registry = LocateRegistry.getRegistry(Constants.IPV4, 1);
        VirtualServerF server = (VirtualServerF) registry.lookup("VirtualServer");
        new RmiClientF(server).run();
    }

}
