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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
*       GEMESUBJECT : la classe Game comunica agli ascoltatori (i Player) di cambiare stato quando:
*                       (per le prossime righe si considera lo stato del Player)
*                   - si passa dallo stato NOT INITIALIZED (bisogna attendere che tutti i giocatori entrino nel game) allo stato BEGIN (si distribuiscono le carte)
*                   - si passa dallo stato BEGIN allo stato CHOOSE GOAL (  le carte sono state distribuite (sia i deck sul tavolo, sia le carte dei giocatori) e TUTTI i player devono scegliere il loro goal
*                   - si passa dallo stato CHOOSE GOAL allo stato CHOOSE_STARTING_CARD (tutti i player devono scegliere il side della loro carta)
*                   - si passa dallo stato CHOOSE_STARTING_CARD allo stato di GIOCO effettivo (al momento della scrittura di questo commento non ho ancora scelto il nome)
*                           quando per ogni observer (player) si chiama il passaggio di stato, solo uno di questi player avrà la variabile is_first = true => quel player sarà il primo giocatore
*/

public class GameController implements GameSubject {
    private List<PlayerObserver> player_observers = new ArrayList<>();
    private List<Player> player_list = new ArrayList<>();
    private HashMap<Player,Boolean> choosed_goal = new HashMap<Player, Boolean>();
    private HashMap<Player,GameFieldController> field_controller = new HashMap<>();
    private int goal_count = 0;
    private HashMap<Player,Boolean> choosed_starting_card = new HashMap<Player, Boolean>();
    private int starting_card_count = 0;
    private int actual_player = 0;  //tiene traccia del giocatore che sta giocando
     /*
     attributi per fine game
      */
    private boolean is_final_state = false;
    private boolean tmp_final_state = false;

    private int final_counter = 0;

    private Game game;


    public GameController(int max_num_player){
        try{
            this.game = new Game(max_num_player);}
        catch (LimitNumPlayerException e){
            System.out.println(e.getMessage());
        }

    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player createPlayer(String nome, boolean isFirst){
        Player player = new Player(ColorsEnum.GREEN, nome, isFirst);
        try {
            GameFieldController field = new GameFieldController(player);
            this.field_controller.put(player,field);
            this.game.actual_state.insertPlayer(player);
            this.choosed_goal.put(player,false);
            this.choosed_starting_card.put(player,false);
            registerObserver(player);
            player_list.add(player);
        }catch(GameInvalidStateException e){
            System.out.println(e.getMessage());
        }
        return player;
    }


    public boolean checkNumPlayer(){
        Integer num_player = game.getNum_player();
        Integer max_num_player = game.getMax_num_player();
        if(num_player == max_num_player && game.actual_state.getNameState().equals("NOT_INITIALIZED")){
            game.gameNextState();
            // 1° notifyObservers: playerState from NOT_INITIALIZED to BEGIN
            notifyObservers();
            /*
            for(int i = 0; i<game.getNum_player(); i++){
                System.err.println(game.getGet_player_index().get(i).actual_state.getNameState());
            }

             */
            game.actual_state.initializedGame();
            // 2° notifyObservers: playerState from BEGIN to CHOOSE_GOAL
            notifyObservers();
            return true;
        }
        else{
            return false;
        }
    }

    public void playerChooseGoal(Player p, int i){
        try{
            if (this.choosed_goal.get(p) == false){
                try{
                    p.actual_state.selectGoal(i);
                    goal_count++;
                    this.choosed_goal.put(p,true);
                    if(goal_count == game.getMax_num_player()){
                        notifyObservers();
                    }
                }
                catch (InvalidBoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }catch(InvalidStateException e){
            System.out.println(e.getMessage());
        }
    }

    public HashMap<Player, Boolean> getChoosed_goal() {
        return choosed_goal;
    }

    public void playerSelectStartingCard(Player p, boolean flipped){
        try{
            if (this.choosed_starting_card.get(p) == false){
                p.actual_state.selectStartingCard(flipped);
                starting_card_count++;
                this.choosed_starting_card.put(p,true);
                if(starting_card_count == game.getMax_num_player()){
                    notifyObservers();
                }
            }
        }catch(InvalidStateException e){
            System.out.println(e.getMessage());
        }
    }

// ------ da qui in avanti inizia il gioco con i turni


    private void nextStatePlayer(){
        Player currentPlayer = player_list.get(actual_player), nextPlayer;
        if(currentPlayer.actual_state.getNameState().equals("PLACE_CARD")){
            currentPlayer.nextStatePlayer();
        }else if (currentPlayer.actual_state.getNameState().equals("DRAW_CARD")){
            if( actual_player == player_list.size() - 1 ){
                nextPlayer = player_list.get(0) ;}
            else{
                nextPlayer = player_list.get(actual_player + 1);
            }
            currentPlayer.nextStatePlayer();
            nextPlayer.nextStatePlayer();
            if(actual_player + 1 == game.getMax_num_player() ){
                actual_player = 0;
            }
            else{
                actual_player ++;
            }
        }
    }


    public void statePlaceCard(Player player, int index, boolean flipped, int x, int y){ //cambiare nome al metodo
        if(is_final_state){
            placeCard(player, index, flipped, x, y);
            final_counter++;
            if(final_counter == game.getMax_num_player()){
                finalPointEndGame(); //conta i punti di ogni giocatore
            }
        }
        else{
            if(tmp_final_state){
                placeCard(player, index, flipped, x, y);
                if(player.getIsFirst()){
                    is_final_state = true;
                    final_counter=1;
                }
            }
            else{
                placeCard(player, index, flipped, x, y);
                if(player.getPlayerPoints() >= 20) {
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
    }

    private void placeCard(Player player, int index,boolean flipped, int x, int y){
        try{
            if(index>2 || index < 0){
                System.out.println("carta non esistente, index sbagliato"); //da convertire in errore
                return;
            }
            GameFieldController field_controller_player = field_controller.get(player);
            PlayCard card_played = player.getCardsInHand().get(index);

            if(field_controller_player.checkPlacing(card_played,x,y)) {
                player.actual_state.placeCard(index, flipped, x, y);
                nextStatePlayer();
            }else{
                System.out.println("non è possibile aggiungere la carta in questa posizione");
            }
        }
        catch(InvalidStateException e){
            System.out.println(e.getMessage());
        }
    }

    private void finalPointEndGame(){
        for(Player p: player_list){ //forall player inside player_list
            Goal goal = p.getGoalCard();
            p.addPoints(goal.numPoints(p.getGameField())); //aggiungo i punti del goal singolo
            p.addPoints(game.getGoal1().numPoints(p.getGameField()));
            p.addPoints(game.getGoal2().numPoints(p.getGameField()));
        }

    }


    public void playerPeachCardFromGoldDeck(Player player){
            try {
                player.actual_state.peachCardFromGoldDeck();
                nextStatePlayer();
            }catch(InvalidStateException e){
                System.out.println(e.getMessage());
            }
        }

    public void playerPeachCardFromResourcesDeck(Player player){
        try {
            player.actual_state.peachFromResourcesDeck();
            nextStatePlayer();
        }catch(InvalidStateException e){
            System.out.println(e.getMessage());
        }
    }

    public void playerPeachFromCardsInCenter(Player player, int i){
        try {
            player.actual_state.peachFromCardsInCenter(i);
            nextStatePlayer();
        }catch(InvalidStateException e){
            System.out.println(e.getMessage());
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
