package it.polimi.ingsw.CONTROLLER;

import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Game.GameSubject;
import it.polimi.ingsw.MODEL.Game.LimitNumPlayerException;
import it.polimi.ingsw.MODEL.Game.State.GameInvalidStateException;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MODEL.Player.PlayerObserver;
import it.polimi.ingsw.MODEL.Player.State.InvalidStateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController implements GameSubject {
    private List<PlayerObserver> player_observers = new ArrayList<>();
    private List<Player> player_list = new ArrayList<>();
    private HashMap<Player,Boolean> choosed_goal = new HashMap<Player, Boolean>();
    private int goal_count = 0;
    private HashMap<Player,Boolean> choosed_starting_card = new HashMap<Player, Boolean>();
    private int starting_card_count = 0;
    private int actual_player = 0;  //tiene traccia del giocatore che sta giocando

    private Game game;


    public GameController(int max_num_player){
        try{
            this.game = new Game(max_num_player);}
        catch (LimitNumPlayerException e){
            System.out.println(e.getMessage());
    }

    }

    public Player createPlayer(String nome, boolean isFirst){
        Player player = new Player(ColorsEnum.GREEN, nome, isFirst);
        try {
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
                p.actual_state.selectGoal(i);
                goal_count++;
                this.choosed_goal.put(p,true);
                if(goal_count == game.getMax_num_player()){
                    notifyObservers();
                }
            }
        }catch(InvalidStateException e){
            System.out.println(e.getMessage());
        }
    }

    public void playerSelectStartingcard(Player p, boolean flipped){
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


    public void playerPlaceCard(Player player, int index,boolean flipped, int x, int y){
            try {
                player.actual_state.placeCard(index, flipped, x, y);
                nextStatePlayer();
            }catch(InvalidStateException e){
                System.out.println(e.getMessage());
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
