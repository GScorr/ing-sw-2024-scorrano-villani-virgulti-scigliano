package it.polimi.ingsw.CONTROLLER;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Game.GameSubject;
import it.polimi.ingsw.MODEL.Game.LimitNumPlayerException;
import it.polimi.ingsw.MODEL.Game.State.GameInvalidStateException;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.InvalidBoundException;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MODEL.Player.PlayerObserver;
import it.polimi.ingsw.MODEL.Player.State.InvalidStateException;

import java.io.Serializable;
import java.util.*;

/*
  todo:
    vedere output di javadoc per @code
    togliere gli import non necessari
    togliere i metodi non utilizzati
 */


/**
 * This class manages player state transitions, game state, and notifies registered players about these changes.
 * <p>
 * The class facilitates communication during specific game phases:
 * </p>
 * <ol>
 *   <li>NOT_INITIALIZED to BEGIN: Notifies players when all have joined, triggering card distribution.</li>
 *   <li>BEGIN to CHOOSE_GOAL: Notifies players after card distribution, prompting them to choose goal cards.</li>
 *   <li>CHOOSE_GOAL to CHOOSE_STARTING_CARD: Notifies players after all choose goals, prompting them to choose starting card sides.</li>
 *   <li>CHOOSE_STARTING_CARD to GAME STATE: Notifies players after all choose starting cards, transitioning to the actual game state. During this transition, one player is designated as the first player (is_first = true).</li>
 * </ol>
 * <p>
 * By implementing the Observer pattern, this class ensures players are informed about relevant game progress and can react accordingly.
 * </p>
 */

public class GameController implements GameSubject, Serializable {
    /**
     * indicates the game il full
     */
    private boolean full;
    /**
     * list of observer of the player state changes
     */
    private List<PlayerObserver> player_observers = new ArrayList<>();
    /**
     * list of all player in one game
     */
    private List<Player> player_list = new ArrayList<>();
    /**
     * list of all names of the player in the game
     */
    private List<String> names = new ArrayList<>();
    /**
     * tracks wich player have choosed the goal
     */
    private HashMap<Player,Boolean> choosed_goal = new HashMap<Player, Boolean>();
    /**
     * maps players to their GameFiledController
     */
    private HashMap<Player,GameFieldController> field_controller = new HashMap<>();
    /**
     *
     */
    private int goal_count = 0;
    /**
     * maps which players have chosen their starting card.
     */
    private HashMap<Player,Boolean> choosed_starting_card = new HashMap<Player, Boolean>();
    /**
     *
     */
    private int starting_card_count = 0;

    /***
     * get the current player
     */
    private int actual_player = 0;

    /**
     * attributes used in end game
     */
    public boolean is_final_state = false;
    public boolean tmp_final_state = false;
    public boolean end__game = false;
    private boolean resources_deck_finished = false;
    private boolean gold_deck_finished = false;
    //public solo per test
    public boolean both_deck_finished = false;

    private int final_counter = 0;

    private Game game;

    /**
     * Transient comparator for sorting players by points (descending order).
     *
     * Uses the `Player::getPlayerPoints` method to access the player's points.
     */
    transient Comparator<Player> idComparator_point = Comparator.comparingInt(Player::getPlayerPoints);

    /**
     * Transient comparator for sorting players by number of achieved goals (descending order).
     *
     * Uses the `Player::getNum_goal_achieve` method to access the number of achieved goals.
     */
    transient Comparator<Player> idComparator_goals_achieve = Comparator.comparingInt(Player::getNum_goal_achieve);


    /**
     * create a new game
     *
     * @param max_num_player specifies the maximum number of players that the game can have
     * @throws ControllerException If the provided `max_Num_Players` is less than 2 or greater than 4
     *
     */
    public GameController(int max_num_player) {
        synchronized(this) {
            if (max_num_player < 2 || max_num_player > 4) {
                throw new ControllerException(0, "Num Player not Valid in creation Game, Insert Again ...");
            } else {
                this.game = new Game(max_num_player);
            }
        }
    }

    /**
     *
     * Creates a new game controller instance with the specified player name and maximum number of players
     *
     * @param name The name of the first player joining the game.
     * @param max_num_player The maximum number of players allowed in the game. Valid values are between 2 and 4
     * @throws ControllerException If the provided `max_Num_Players` is less than 2 or greater than 4
     *
     */
    public GameController(String name, int max_num_player) {
        synchronized(this) {
            if (max_num_player < 2 || max_num_player > 4) {
                throw new ControllerException(0, "Num Player not Valid in creation Game, Insert Again ...");
            } else {
                this.game = new Game(name, max_num_player);

            }
        }
    }

    /**
     * Returns a map containing the associations between players and their corresponding game field controllers.
     *
     * The returned map is a copy of the internal data structure. Modifications to the returned map will not affect the internal state of the game controller.
     *
     * @return A map where the key is a `Player` object and the value is a corresponding `GameFieldController` object.
     */
    public HashMap<Player, GameFieldController> getField_controller() {
        return field_controller;
    }

    /**
     *
     * @return the game associated with this controller
     */
    public Game getGame() {
        return game;
    }


    /**
     *
     * @param game set the game associated with this controller
     */
    public void setGame(Game game) {
        this.game = game;
    }


    public int get_final_counter() {
        return final_counter;
    }

    /**
     *
     * @return the list of the players in this game
     */
    public List<Player> getPlayer_list() {
        return player_list;
    }

    /**
     * Checks if the provided player name is unique within the game.
     *
     * @param name The player name to check for uniqueness.
     * @throws ControllerException If the name is not unique or is empty.
     * <ul>
     *  <li>Error code 1: "Insert at least 1 character" (Empty name)</li>
     *  <li>Error code 2: "Name is not unique, Insert another one" (Non-unique name)</li>
     * </ul>
     */
    //da eliminare
    private void isUniqueName(String name) {
        if(name.length() == 0){
            throw new ControllerException(1,"Insert at least 1 character");
        }
        // Cicla attraverso la lista di nomi
        for (String existingName : names) {
            // Confronta il nome dato con ogni nome nella lista, mantenendo la distinzione tra maiuscole e minuscole
            if (existingName.equals(name)) {
                // Se trova una corrispondenza, restituisce false perché il nome non è univoco
                throw new ControllerException(2,"Name is not unique, Insert another one");

            }
        }
        // Se non trova corrispondenze, restituisce true perché il nome è univoco
        return;
    }

    /**
     * Checks if the current player is the only remaining connected player in the game.
     *
     * <p>
     * This method iterates through all player slots (up to the maximum number of players allowed)
     * and counts the number of disconnected players. If the difference between the maximum number
     * of players and the number of disconnected players is 1 (one remaining connected player) or 0
     * (no connected players), then the current player is considered alone.
     * </p>
     *
     * @return True if the current player is the only remaining connected player, false otherwise.
     */
    public boolean isAlone(){
        boolean num = false;
        int num_disconnected = 0;
        for ( int i = 0 ; i < game.getMax_num_player(); i++ )
        {
            if (game.getGet_player_index().get(i).isDisconnected()) num_disconnected++;
        }
        if(  (game.getMax_num_player() - num_disconnected == 1) || (game.getMax_num_player() - num_disconnected == 0) ){
            num = true;
        }
        return num;
    }

    /**
     * Creates a new player and adds them to the game, if possible.
     *
     * <p>
     * This method attempts to add a new player to the game with the given name. If the player is the
     * first to join the game, the flag `isFirst` should be set to true.
     * This method implements some check to ensure that the player can be added
     * like checking the maximum number of players,
     * the validity of the game state, and the uniqueness of the player name.
     * </p>
     *
     * @param nome The name of the player.
     * @param isFirst True if this is the first player joining the game, false otherwise.
     * @return The newly created player.
     * @throws ControllerException If:
     * <ul>
     *   <li>The maximum number of players has already been reached (Error code 3).</li>
     *   <li>The game state is not valid for adding players (Error code 4).</li>
     *   <li>The player name is not unique (if implemented, not shown here).</li>
     * </ul>
     *
     */
    public Player createPlayer(String nome, boolean isFirst){
        //da eliminare
        /*if(game.getNumPlayer() > 0) {
            isUniqueName(nome);
        }*/
        Player player = new Player(ColorsEnum.GREEN, nome, isFirst);
        if(player_list.size()==0){
        player = new Player(ColorsEnum.GREEN, nome, isFirst);}
        if(player_list.size()==1){
             player = new Player(ColorsEnum.RED, nome, isFirst);}
        if(player_list.size()==2){
             player = new Player(ColorsEnum.YELLOW, nome, isFirst);}
        if(player_list.size()==3){
             player = new Player(ColorsEnum.BLU, nome, isFirst);}
        if(game.getNum_player() == game.getMax_num_player()){
            throw new ControllerException(3,"Maximum number of players reached");
        }
        GameFieldController field = new GameFieldController(player);
        this.names.add(nome);
        this.field_controller.put(player, field);
        if(this.game.actual_state.insertPlayer(player)){
            this.choosed_goal.put(player, false);
            this.choosed_starting_card.put(player, false);
            registerObserver(player);
            player_list.add(player);
        }else{
            throw new ControllerException(4,"Not possible call this method, Game State is:" + game.actual_state.getNameState());
        }
        return player;
    }

    public boolean getFull(){
        if(game.getMax_num_player() == game.getNum_player() ) return true;
        return false;
    }

    /**
     * Checks if the maximum number of players has been reached and transitions the game state if necessary.
     * <p>
     * retrieves the current number of players and the maximum allowed number from the {@code Game} object.
     * If both numbers are equal (indicating the maximum number of players has been reached) and the current game state
     * is "NOT_INITIALIZED", the following actions occur:
     * <ol>
     *   <li>Sets the {@code full} flag to true (indicating the game is full).</li>
     *   <li>Transitions the game state to the next state using {@code game.gameNextState()}.</li>
     *   <li>Notifies all observers about the player state change from "NOT_INITIALIZED" to "BEGIN".</li>
     *   <li>Calls {@code game.actual_state.initializedGame()} (assuming this method initializes the game for play).</li>
     *   <li>Notifies all observers about the player state change from "BEGIN" to "CHOOSE_GOAL".</li>
     *   <li>Transitions the game state to the "CHOOSE_GOAL" state using {@code game.gameNextState()}.</li>
     * </ol>
     * The method returns {@code true} if the maximum number of players has been reached and the game state has been transitioned,
     * otherwise it returns {@code false}.
     * </p>
     *
     * @return {@code true} if the maximum number of players has been reached and the game state has been transitioned, {@code false} otherwise.
     */
    public boolean  checkNumPlayer(){
        Integer num_player = game.getNum_player();
        Integer max_num_player = game.getMax_num_player();
        if(num_player == max_num_player && game.actual_state.getNameState().equals("NOT_INITIALIZED")){
            full = true;
            game.gameNextState();
            // 1° notifyObservers: playerState from NOT_INITIALIZED to BEGIN
            notifyObservers();
            game.actual_state.initializedGame();
            // 2° notifyObservers: playerState from BEGIN to CHOOSE_GOAL
            notifyObservers();
            game.gameNextState();
            return true;
        }
        else{
            return false;
        }
    }

    //da eliminare
    /*public boolean insertCard(Player player, PlayCard card, int x, int y, int index, boolean flipped) throws ControllerException{
        if (field_controller.get(player).checkPlacing(card, x, y)){
            player.placeCard(index, flipped, x, y);
            nextStatePlayer();
            return true;
        }
        return false;
    }*/

    /**
     * Allows a player to select a goal card.
     * <p>
     * This method is synchronized to ensure thread-safe access to shared resources.
     * </p>
     *
     * @param p The player object who wants to select a goal card.
     * @param i The index of the desired goal card (0 or 1).
     * @throws ControllerException If:
     * <ul>
     *   <li>The provided index is out of bounds (Error code 30).</li>
     *   <li>The player has already chosen a goal card (Error code 6).</li>
     *   <li>The player state is not valid for selecting a goal (Error code 5).</li>
     * </ul>
     *
     * @implNote This method updates the internal {@code goal_count} and calls {@code notifyObservers} if all players have chosen their goals.
     */
    public synchronized void playerChooseGoal(Player p, int i) {
        if(i< 0 || i > 1){
            throw new ControllerException(30, "Index Goal OUTBOUND, 0 <= index <= 1");
        }
        if (this.choosed_goal.get(p) == false) {
            if (p.actual_state.selectGoal(i)) {
                goal_count++;
                this.choosed_goal.put(p, true);
                if (goal_count == game.getMax_num_player()) {
                    game.gameNextState();
                    notifyObservers();
                }
            } else {
                throw new ControllerException(5, "Not possible call this method, Player State is:" + p.actual_state.getNameState());
            }
        }else{ throw new ControllerException(6, "Goal Card already select, wait for the continuing of the game.");}
    }

    /**
     * Returns the map that tracks which players have chosen their goal cards.
     *
     * The map key is a `Player` object, and the value is a boolean indicating whether that player has chosen a goal card (true) or not (false).
     *
     * @return A map containing player objects as keys and booleans representing if the player have choosed the goal card.
     */
    public HashMap<Player, Boolean> getChoosed_goal() {
        return choosed_goal;
    }

    /**
     * Allows a player to select their starting card (flipped or not).
     * <p>
     * Synchronized method to ensure thread-safe access to shared resources.
     * </p>
     *
     * @param p The player object who wants to select their starting card.
     * @param flipped True if the player wants the card flipped, false otherwise.
     * @throws ControllerException If:
     * <ul>
     *   <li>The player has already chosen their starting card (Error code 8).</li>
     *   <li>The player state is not valid for selecting a starting card (Error code 7).</li>
     * </ul>
     *
     * @implNote This method updates the internal {@code starting_card_count} and calls {@code notifyObservers} if all players have chosen their cards.
     */
    public void playerSelectStartingCard(Player p, boolean flipped){
        if (this.choosed_starting_card.get(p) == false){
            if(p.actual_state.selectStartingCard(flipped)) {
                starting_card_count++;
                this.choosed_starting_card.put(p, true);
                if (starting_card_count == game.getMax_num_player()) {
                    notifyObservers();
                    game.gameNextState();
                }
            }else{
                throw new ControllerException(7, "Not possible call this method, Player State is:" + p.actual_state.getNameState());
            }
        }else{
            throw new ControllerException(8, "Starting Card already select, wait for the continuing of the game.");
        }
    }

    /**
     * Advances the game to the next player's turn.
     * Handles logic for ending the round, game, and transitioning player states.
     */
    public void nextStatePlayer(){
        Player currentPlayer = player_list.get(actual_player), nextPlayer;
        if(currentPlayer.actual_state.getNameState().equals("PLACE_CARD")){
            currentPlayer.nextStatePlayer();
            if(both_deck_finished && end__game){
                game.gameNextState(); //cambio stato al game
                finalPointEndGame(); //conta i punti di ogni giocatore
            }else {
                // If deck are terminated the DRAW_CARD state is skipped
                if (both_deck_finished) {
                    nextStatePlayer();
                }
            }
        }else if (currentPlayer.actual_state.getNameState().equals("DRAW_CARD")) {
            if (end__game ) {
                game.gameNextState(); //cambio stato al game
                finalPointEndGame(); //conta i punti di ogni giocatore
            } else {
                if (actual_player == player_list.size() - 1) {
                    nextPlayer = player_list.get(0);
                } else {
                    nextPlayer = player_list.get(actual_player + 1);
                }
                currentPlayer.nextStatePlayer();
                nextPlayer.nextStatePlayer();
                if (actual_player + 1 == game.getMax_num_player()) {
                    actual_player = 0;
                } else {
                    actual_player++;
                }
            }
            }
    }


    /**
     * Allows a player to select wich side of the card to use
     * <p>
     * The selected card is used with the choosed side in the game. The method throws exceptions in case of invalid player state or index.
     * </p>
     *
     * @param player The player selecting the card.
     * @param index The index of the side card (0-2).
     * @param flip True if the card should be flipped, false otherwise.
     * @throws ControllerException If:
     *   <ul>
     *     <li>The player's state disallows side card selection (Error code 21).</li>
     *     <li>The provided index is out of bounds (Error code 32).</li>
     *   </ul>
     */
    public void selectSideCard(Player player, int index, boolean flip){
       if( player.actual_state.selectSideCard(index,flip)){}
       else{
           throw new ControllerException(21,"Not possible chosing SIDE card in this STATE: " + player.actual_state.getNameState());
       }
       if(index < 0 || index > 2){
           throw new ControllerException(32,"Index out of bound: 0 <= index <= 2");
       }
    }

    /**
     * Allows a player to play a card at a specific location on the game board.
     * <p>
     * It throws exceptions in case of invalid player state, index, or unexpected game conditions.
     * </p>
     *
     * @param player The player playing the card.
     * @param index The index of the card in the player's hand (0-2).
     * @param x The x-coordinate of the placement location.
     * @param y The y-coordinate of the placement location.
     * @throws ControllerException If:
     *   <ul>
     *     <li>The player's state disallows card placement (Error code 10).</li>
     *     <li>The provided index is out of bounds (Error code 9).</li>
     *     <li>The game is in an unexpected state.</li>
     *   </ul>
     */
    public void statePlaceCard(Player player, int index, int x, int y) throws ControllerException{ //cambiare nome al metodo

        if(player.actual_state.getNameState().compareTo("PLACE_CARD") != 0){
            throw new ControllerException(10, "Not possible call this method, Player State is:" + player.actual_state.getNameState());
        }

        if(index>2 || index < 0){
            throw new ControllerException(9,"Index error, placeCard accept 0 <= index <= 2");
        }
        if(is_final_state){
            placeCard(player, index, x, y);
            final_counter++;
            if(final_counter == game.getMax_num_player()){
                //l'ultimo giocatore deve pescare una carta
                end__game = true;
            }
            // --
            nextStatePlayer();
        }
        else{
            if(tmp_final_state){
                placeCard(player, index, x, y);
                if(player.getIsFirst()){
                    is_final_state = true;
                    final_counter=1;
                }
                // --
                nextStatePlayer();
            }
            else{
                placeCard(player, index, x, y);
                if(player.getPlayerPoints() >= 20) {
                    if(player.getIsFirst()){
                        is_final_state=true;
                        final_counter=1;
                    }
                    else{
                        tmp_final_state = true;
                    }
                }
                // --
                nextStatePlayer();
            }
        }
    }

    /**
     * <p>
     * This method checks the validity of the card placement and the player's state before performing the action.
     * </p>
     *
     * @param player The player placing the card.
     * @param index The index of the card in the player's hand (0-2).
     * @param x The x-coordinate of the placement location.
     * @param y The y-coordinate of the placement location.
     * @throws ControllerException If:
     *   <ul>
     *     <li>The card placement is invalid.</li>
     *     <li>The player's state disallows card placement (Error code 10).</li>
     *   </ul>
     */
    private void placeCard(Player player, int index, int x, int y) throws ControllerException{
        boolean flipped = player.getCardsInHand().get(index).flipped;

            GameFieldController field_controller_player = field_controller.get(player);
            PlayCard card_played = player.getCardsInHand().get(index);
            if(field_controller_player.checkPlacing(card_played,x,y)) {
                if(player.actual_state.placeCard(index, flipped, x, y)){

                }else{
                    throw new ControllerException(10, "Not possible call this method, Player State is:" + player.actual_state.getNameState());
                }
            }

    }

    /**
     * Calculates final points for each player at the end of the game, considering goals and achievements.
     * <p>
     * This method sorts players by points (in descending order) and then by the number of achieved goals.
     * </p>
     * <p>
     * It prints player names
     * </p>
     */
    private void finalPointEndGame(){

        for(Player p: player_list){ //forall player inside player_list
            p.setEndGame(); //all the players are in the final state
            Goal goal = p.getGoalCard();
            p.addPoints(goal.numPoints(p.getGameField()));
            if(goal.numPoints(p.getGameField()) > 0){
                p.setNum_goal_achieve(p.getNum_goal_achieve()+1);
            }
            p.addPoints(game.getGoal1().numPoints(p.getGameField()));
            if(game.getGoal1().numPoints(p.getGameField()) > 0){
                p.setNum_goal_achieve(p.getNum_goal_achieve()+1);
            }
            p.addPoints(game.getGoal2().numPoints(p.getGameField()));
            if(game.getGoal2().numPoints(p.getGameField()) > 0){
                p.setNum_goal_achieve(p.getNum_goal_achieve()+1);
            }
        }
        Collections.sort(player_list, idComparator_point.reversed().thenComparing(idComparator_goals_achieve));
    }

    /**
     * Allows a player in the "DRAW_CARD" state to draw a card from the gold deck if available.
     * <p>
     * This method updates deck depletion flags and potentially transitions to the final state based on game conditions.
     * </p>
     * <p>
     * It throws exceptions for invalid player state, empty deck, or unexpected behavior during card draw.
     * </p>
     *
     * @param player The player attempting to draw from the gold deck.
     * @throws ControllerException If:
     *   <ul>
     *     <li>The player's state disallows drawing from the gold deck (Error code 14).</li>
     *     <li>The gold deck is empty (Error code 15).</li>
     *     <li>Unexpected behavior occurs during card draw from the player's state.</li>
     *   </ul>
     */
    public void playerPeachCardFromGoldDeck(Player player){
        if(player.actual_state.getNameState().compareTo("DRAW_CARD") != 0 ){
            throw new ControllerException(14, "Not possible call this method, Player State is:" + player.actual_state.getNameState());
        }
        if(game.getGold_deck().cards.size() == 0 ){
            throw new ControllerException(15, "Deck is empty, draw from a different one." );
        }
        if(player.actual_state.peachCardFromGoldDeck()) {
            if(game.getGold_deck().cards.size() == 0){
                gold_deck_finished = true;
                if(resources_deck_finished){
                    both_deck_finished = true;
                    if(player.getIsFirst()){
                        is_final_state=true;
                        final_counter=1;
                    }
                    else{
                        tmp_final_state = true;
                    }
                }
            }
            nextStatePlayer();
        }else{
            throw new ControllerException(14, "Not possible call this method, Player State is:" + player.actual_state.getNameState());
        }
    }

    /**
     * Allows a player in the "DRAW_CARD" state to draw a card from the resources deck if available.
     * @param player The player attempting to draw from the resources deck.
     * same behavior of playerPeachCardFromGoldDeck
     */
    public void playerPeachCardFromResourcesDeck(Player player){
        if(player.actual_state.getNameState().compareTo("DRAW_CARD") != 0 ){
            throw new ControllerException(14, "Not possible call this method, Player State is:" + player.actual_state.getNameState());
        }
            if(game.getResources_deck().cards.size() == 0 ){
                throw new ControllerException(15, "Deck is empty, draw from a different one." );
            }
            if(player.actual_state.peachFromResourcesDeck()) {
                if(game.getResources_deck().cards.size() == 0){
                    resources_deck_finished = true;
                    if(gold_deck_finished){
                        both_deck_finished = true;
                        if(player.getIsFirst()){
                            is_final_state=true;
                            final_counter=1;
                        }
                        else{
                            tmp_final_state = true;
                        }
                    }
                }
                nextStatePlayer();
            }else{
                throw new ControllerException(14, "Not possible call this method, Player State is:" + player.actual_state.getNameState());
            }
    }

    /**
     * Allows a player in the "DRAW_CARD" state to take a card from the center area (gold or resource pile) if available.
     * <p>
     * This method updates deck depletion flags and potentially transitions to the final state based on game conditions.
     * </p>
     * <p>
     * It throws exceptions for invalid player state, index out of bounds, missing cards, or unexpected behavior during card draw.
     * </p>
     *
     * @param player The player attempting to take a card from the center area.
     * @param i The index of the desired card (0: gold pile 1st card, 1: gold pile 2nd card, 2: resource pile 1st card, 3: resource pile 2nd card).
     * @throws ControllerException If:
     *   <ul>
     *     <li>The player's state disallows taking a card from the center (Error code 14).</li>
     *     <li>The index is out of bounds (Error code 16).</li>
     *     <li>The selected card is missing from the center (Error codes 17-20).</li>
     *     <li>Unexpected behavior occurs during card draw from the player's state (Error code 15).</li>
     *   </ul>
     */
    public void playerPeachFromCardsInCenter(Player player, int i){
        if(player.actual_state.getNameState().compareTo("DRAW_CARD") != 0 ){
            throw new ControllerException(14, "Not possible call this method, Player State is:" + player.actual_state.getNameState());
        }

        if(i< 0 || i > 3){
            throw new ControllerException(16,"Bound exception: l'int passato può essere solo 0<=i<4");
        }

        if(i==0 && game.getCars_in_center().getGold_list().get(0) == null){
            throw new ControllerException(17,"Card is not present. Case where the deck is terminated");
        }else if(i==1 && game.getCars_in_center().getGold_list().get(1) == null){
            throw new ControllerException(18,"Card is not present. Case where the deck is terminated");
        }else if(i==2 && game.getCars_in_center().getResource_list().get(0) == null){
            throw new ControllerException(19,"Card is not present. Case where the deck is terminated");
        } else if (i == 3 && game.getCars_in_center().getResource_list().get(1) == null) {
            throw new ControllerException(20,"Card is not present. Case where the deck is terminated");
        }
        if(player.actual_state.peachFromCardsInCenter(i)){
            if(i == 0 || i == 1){
                if(game.getGold_deck().cards.size() == 0){
                    gold_deck_finished = true;
                    if(resources_deck_finished){
                        both_deck_finished = true;
                        if(player.getIsFirst()){
                            is_final_state=true;
                            final_counter=1;
                        }
                        else{
                            tmp_final_state = true;
                        }
                    }
                }
            }
            if( i == 2 || i == 3){
                if(game.getResources_deck().cards.size() == 0){
                    resources_deck_finished = true;
                    if(gold_deck_finished){
                        both_deck_finished = true;
                        if(player.getIsFirst()){
                            is_final_state=true;
                            final_counter=1;
                        }
                        else{
                            tmp_final_state = true;
                        }
                    }
                }
            }
            nextStatePlayer();
        }else{
            throw new ControllerException(15, "Not possible call this method, Player State is:" + player.actual_state.getNameState());
        }
    }

    /**
     *
     * @param player_observer
     */
    @Override
    public void registerObserver(PlayerObserver player_observer) {
        this.player_observers.add(player_observer);
    }

    /**
     *
     * @param player_observer
     */
    @Override
    public void removeObserver(PlayerObserver player_observer) {
        this.player_observers.remove(player_observer);
    }

    @Override
    public void notifyObservers() {
        for (PlayerObserver player_observer : player_observers) {
            player_observer.update();
        }
    }

    /**
     *
     * @param i
     * @param message The chat message to be inserted.
     */
    public void insertMessageinChat(int i, ChatMessage message) {
        game.insertMessageinChat(i,message);
    }
}
