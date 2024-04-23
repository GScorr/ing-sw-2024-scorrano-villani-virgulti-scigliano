package it.polimi.ingsw.CONTROLLER;

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
*       GEMESUBJECT : la classe Game comunica agli ascoltatori (i Player) di cambiare stato quando:
*                       (per le prossime righe si considera lo stato del Player)
*                   - si passa dallo stato NOT INITIALIZED (bisogna attendere che tutti i giocatori entrino nel game) allo stato BEGIN (si distribuiscono le carte)
*                   - si passa dallo stato BEGIN allo stato CHOOSE GOAL (  le carte sono state distribuite (sia i deck sul tavolo, sia le carte dei giocatori) e TUTTI i player devono scegliere il loro goal
*                   - si passa dallo stato CHOOSE GOAL allo stato CHOOSE_STARTING_CARD (tutti i player devono scegliere il side della loro carta)
*                   - si passa dallo stato CHOOSE_STARTING_CARD allo stato di GIOCO effettivo (al momento della scrittura di questo commento non ho ancora scelto il nome)
*                           quando per ogni observer (player) si chiama il passaggio di stato, solo uno di questi player avrà la variabile is_first = true => quel player sarà il primo giocatore
*/

public class GameController implements GameSubject, Serializable {
    private boolean full;
    private List<PlayerObserver> player_observers = new ArrayList<>();
    private List<Player> player_list = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private HashMap<Player,Boolean> choosed_goal = new HashMap<Player, Boolean>();
    private HashMap<Player,GameFieldController> field_controller = new HashMap<>();
    private int goal_count = 0;
    private HashMap<Player,Boolean> choosed_starting_card = new HashMap<Player, Boolean>();
    private int starting_card_count = 0;
    private int actual_player = 0;  //tiene traccia del giocatore che sta giocando
     /*
     attributi per fine game
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

    transient Comparator<Player> idComparator_point = Comparator.comparingInt(Player::getPlayerPoints);
    transient Comparator<Player> idComparator_goals_achieve = Comparator.comparingInt(Player::getNum_goal_achieve);


    public GameController(int max_num_player) {
        synchronized(this) {
            if (max_num_player < 2 || max_num_player > 4) {
                throw new ControllerException(0, "Num Player not Valid in creation Game");
            } else {
                this.game = new Game(max_num_player);
            }
        }
    }
    public GameController(String name, int max_num_player) {
        synchronized(this) {
            if (max_num_player < 2 || max_num_player > 4) {
                throw new ControllerException(0, "Num Player not Valid in creation Game");
            } else {
                this.game = new Game(name, max_num_player);

            }
        }
    }

    public Game getGame() {
        return game;
    }


    public void setGame(Game game) {
        this.game = game;
    }

    public int get_final_counter() {
        return final_counter;
    }

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

    public Player createPlayer(String nome, boolean isFirst){
        if(game.getNumPlayer() > 0) {
            isUniqueName(nome);
        }
        Player player = new Player(ColorsEnum.GREEN, nome, isFirst);
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

    public boolean checkNumPlayer(){
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

    public void playerChooseGoal(Player p, int i) {
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

    public HashMap<Player, Boolean> getChoosed_goal() {
        return choosed_goal;
    }

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

// ------ da qui in avanti inizia il gioco con i turni


    private void nextStatePlayer(){
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

    /*
    TODO
        gestire la fine del gioco in caso di fine di entrambi i deck
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

    public void statePlaceCard(Player player, int index, int x, int y){ //cambiare nome al metodo

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

    private void placeCard(Player player, int index, int x, int y){
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

    /*
    da testare
     */

    private void finalPointEndGame(){

        for(Player p: player_list){ //forall player inside player_list
            p.setEndGame(); //tutti i player sono in stato finale e non possono fare nulla
            Goal goal = p.getGoalCard();
            p.addPoints(goal.numPoints(p.getGameField())); //aggiungo i punti del goal singolo
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

        Collections.sort(player_list, idComparator_point.reversed().thenComparing(idComparator_goals_achieve)); //restituisce la lista ordinata

        for(Player p: player_list){
            System.out.println(p.getName());
        }


    }

    /*
    da gestire la parità di punteggio
     */
    public void getClassifica(){

    }


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

    @Override
    public void registerObserver(PlayerObserver player_observer) {
        this.player_observers.add(player_observer);
    }

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
}
