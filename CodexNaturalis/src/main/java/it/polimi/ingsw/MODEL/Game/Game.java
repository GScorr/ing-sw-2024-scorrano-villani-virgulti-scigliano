package it.polimi.ingsw.MODEL.Game;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.DeckPackage.DeckGoalCard;
import it.polimi.ingsw.MODEL.Game.State.Begin;
import it.polimi.ingsw.MODEL.Game.State.GameState;
import it.polimi.ingsw.MODEL.Game.State.NotInitialized;
import it.polimi.ingsw.MODEL.Game.State.Turn;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MODEL.Player.PlayerObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
@Davide
TODO:



@Fra

TODO:
     -bisogna definire un END_GAME state



*/


public class Game  {
    DeckCreation creation;
    private int max_num_player;
    private int num_player;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;


    private Map<Integer,Player> get_player_index = new HashMap<>();
    private Goal goal1;
    private Goal goal2;

    private CenterCards cards_in_center;
    private Deck gold_deck,resources_deck, starting_cards_deck;
    private DeckGoalCard goal_deck;



    private GameState not_initialized = new NotInitialized(this),
              begin = new Begin(this),
              turn = new Turn(this);
    public  GameState actual_state;




    public Goal getGoal1() {
        return goal1;
    }

    public Goal getGoal2() {
        return goal2;
    }

    public int getNumPlayer() {
        return num_player;
    }
    public void setNumPlayer(int num_player) {
        this.num_player = num_player;
    }
    //Getter of points, given the index of the player

    public int getPoints(int player_index) {
        if( player_index < 0 || player_index > num_player )  System.out.printf("\n ERROR: INDEX EXCEED DOMAIN");
        return get_player_index.get(player_index).getPlayerPoints();
    }

    public GameField getField(int player_index){
        if( player_index < 0 || player_index > num_player )  System.out.printf("\n ERROR: INDEX EXCEED DOMAIN");
        return get_player_index.get(player_index).getGameField();
    }

    public int getMax_num_player() {
        return max_num_player;
    }

    public int getNum_player() {
        return num_player;
    }

    public CenterCards getCars_in_center() {
        return cards_in_center;
    }

    public Deck getGold_deck() {
        return gold_deck;
    }

    public Deck getResources_deck() {
        return resources_deck;
    }

    //crezione dei deck
    // scelta max_num_player
    public Game( /* DeckGoalCard goal_deck */ int max_num_player) {
        if(max_num_player <= 0 || max_num_player > 4){throw new LimitNumPlayerException("max_num_player invalido");}
        this.creation = new DeckCreation();
        this.gold_deck = new Deck(creation.getMixGoldDeck());
        this.resources_deck = new Deck(creation.getMixResourcesDeck());
        this.starting_cards_deck = new Deck(creation.getMixStartingDeck());
        this.goal_deck = new DeckGoalCard(creation.getMixGoalDeck());
        this.max_num_player = max_num_player;
        this.actual_state = not_initialized;


    }


    public void insertPlayer(Player player){
        if( this.num_player == max_num_player ){
            throw new LimitNumPlayerException("Limite giocatori già raggiunto, non è possibile entrare in questa partita");
        }else{
            if(num_player == 0){
                this.player1 = player;
                get_player_index.put(0,player1);
            }else
            if(num_player == 1){
                this.player2 = player;
                get_player_index.put(1,player2);
            }else
            if(num_player == 2 ){
                this.player3 = player;
                get_player_index.put(2,player3);
            }else
            if(num_player == 3){
                this.player4 = player;
                get_player_index.put(3,player4);
            }

            this.num_player++;

        }
    }

    //prima di chiamare questo metodo devo vedere se i Players sono >= 2 &&  < 4 (non possono mai essere per metodo InsertPlayer)
    public void initializedGame(){

                distributeStartingCard();
                initializedCenterCard();
                distributeThreeCards();
                selectGoals();
                distributeTwoGoalsToPlayer();

    }

    // At the beginnig , starting_cards has to be distributed to the player
    public void distributeStartingCard(){
        for(int i=0; i<num_player;i++){
            get_player_index.get(i).setStartingCard(starting_cards_deck.drawCard());
        }
    }

    //4 cards at the center has to be initialized
    public void initializedCenterCard(){
        List<PlayCard> gold_list = new ArrayList<PlayCard>();
        List<PlayCard> resource_list= new ArrayList<PlayCard>();;
        gold_list.add(gold_deck.drawCard());
        gold_list.add(gold_deck.drawCard());

        resource_list.add(resources_deck.drawCard());
        resource_list.add(resources_deck.drawCard());
        CenterCards tmp = new CenterCards(gold_list,resource_list,gold_deck,resources_deck);
        this.cards_in_center = tmp;
    }

    public void distributeThreeCards(){
        for (int i = 0; i<num_player;i++){
            List<PlayCard> tmp = new ArrayList<PlayCard>();
            tmp.add(gold_deck.drawCard());
            tmp.add(resources_deck.drawCard());
            tmp.add(resources_deck.drawCard());
            get_player_index.get(i).setInitialCardsInHand(tmp);
        }
    }

    public void selectGoals(){
        goal1 = goal_deck.drawCard();
        goal2 = goal_deck.drawCard();
    }

    private void distributeTwoGoalsToPlayer(){
        for(int i=0;i<num_player;i++){
            List<Goal> tmp = new ArrayList<Goal>();
            tmp.add(goal_deck.drawCard());
            tmp.add(goal_deck.drawCard());
            get_player_index.get(i).setInitialGoalCards(tmp);
        }
    }

    private void setGame_state(GameState state){
        this.actual_state = state;
    }

    public Map<Integer, Player> getGet_player_index() {
        return get_player_index;
    }

    public void gameNextState(){
        switch(actual_state.getNameState()){
            case "NOT_INITIALIZED":
                setGame_state(begin);
            case "BEGIN":
                setGame_state(turn);
            //bisogna definire un END_GAME state
            case "TURN":
                setGame_state( actual_state);


        }

    }




























}