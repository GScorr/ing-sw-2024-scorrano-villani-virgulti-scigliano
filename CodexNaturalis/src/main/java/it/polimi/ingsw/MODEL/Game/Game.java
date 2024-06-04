package it.polimi.ingsw.MODEL.Game;
import it.polimi.ingsw.Chat;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.DeckPackage.DeckGoalCard;
import it.polimi.ingsw.MODEL.Game.State.*;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;


import java.io.Serializable;
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


public class Game implements Serializable {
    DeckCreation creation;
    private int max_num_player;
    private int num_player;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Integer index_game;
    private String name;


    private Map<Integer,Player> get_player_index = new HashMap<>();
    private Goal goal1;
    private Goal goal2;

    private CenterCards cards_in_center;
    private Deck gold_deck,resources_deck, starting_cards_deck;
    private DeckGoalCard goal_deck;
    private List<Chat> chats = new ArrayList<>();


    private GameState not_initialized = new NotInitialized(this),
              begin = new Begin(this),
              choosing_goal = new CHOOSING_GOAL(this),
              choosing_starting_card = new CHOOSING_STARTING_CARD(this),
              turn = new Turn(this),
              end_game = new EndGame(this);
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
        this.creation = new DeckCreation();

        /*  DECK NON MISCHIATO
        this.gold_deck = new Deck(creation.getGoldDeck());
        this.resources_deck = new Deck(creation.getResourcesDeck());
         */


        //Deck Mischiato
        this.gold_deck = new Deck(creation.getMixGoldDeck());
        this.resources_deck = new Deck(creation.getMixResourcesDeck());


        this.starting_cards_deck = new Deck(creation.getMixStartingDeck());
        this.goal_deck = new DeckGoalCard(creation.getMixGoalDeck());
        this.max_num_player = max_num_player;
        this.actual_state = not_initialized;
        this.index_game = IndexManagerF.getNextIndex();
        for(int i=0; i<7; i++){
            chats.add(new Chat());
        }
    }

    public Game( /* DeckGoalCard goal_deck */ String name, int max_num_player) {
        this.creation = new DeckCreation();
        this.gold_deck = new Deck(creation.getMixGoldDeck());
        this.resources_deck = new Deck(creation.getMixResourcesDeck());
        this.starting_cards_deck = new Deck(creation.getMixStartingDeck());
        this.goal_deck = new DeckGoalCard(creation.getMixGoalDeck());
        this.max_num_player = max_num_player;
        this.actual_state = not_initialized;
        this.index_game = IndexManagerF.getNextIndex();
        this.name = name;
        for(int i=0; i<7; i++){
            chats.add(new Chat());
        }
    }


    public void insertPlayer(Player player){
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

    //prima di chiamare questo metodo devo vedere se i Players sono >= 2 &&  < 4 (non possono mai essere per metodo InsertPlayer)
    public void initializedGame(){

                distributeStartingCard();
                initializedCenterCard();
                distributeThreeCards();
                selectGoals();
                distributeTwoGoalsToPlayer();

                for(int i=0; i<num_player;i++){
                    get_player_index.get(i).setDeck(resources_deck,gold_deck,cards_in_center);
                }

    }

    // At the beginnig , starting_cards has to be distributed to the player
    private void distributeStartingCard(){
        for(int i=0; i<num_player;i++){
            get_player_index.get(i).setStartingCard(starting_cards_deck.drawCard());
        }
    }

    //4 cards at the center has to be initialized
    private void initializedCenterCard(){
        List<PlayCard> gold_list = new ArrayList<PlayCard>();
        List<PlayCard> resource_list= new ArrayList<PlayCard>();;
        gold_list.add(gold_deck.drawCard());
        gold_list.add(gold_deck.drawCard());

        resource_list.add(resources_deck.drawCard());
        resource_list.add(resources_deck.drawCard());
        CenterCards tmp = new CenterCards(gold_list,resource_list,gold_deck,resources_deck);
        this.cards_in_center = tmp;
    }

    private void distributeThreeCards(){
        for (int i = 0; i<num_player;i++){
            List<PlayCard> tmp = new ArrayList<PlayCard>();
            tmp.add(gold_deck.drawCard());
            tmp.add(resources_deck.drawCard());
            tmp.add(resources_deck.drawCard());
            get_player_index.get(i).setInitialCardsInHand(tmp);
        }
    }

    private void selectGoals(){
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

    public GameState getActual_state() {
        return actual_state;
    }

    public Map<Integer, Player> getGet_player_index() {
        return get_player_index;
    }

    public void gameNextState(){
        switch(actual_state.getNameState()){
            case "NOT_INITIALIZED":
                setGame_state(begin);
                break;
            case "BEGIN":
                setGame_state(choosing_goal);
                break;
            case "CHOOSING_GOAL":
                setGame_state(choosing_starting_card);
                break;
            case "CHOOSING_STARTING_CARD":
                setGame_state(turn);
                break;
            case "TURN":
                setGame_state( end_game);
                break;

        }

    }

    public String getName() {
        return name;
    }

    public Integer getIndex_game() {
        return index_game;
    }

    public void insertMessageinChat(int i, ChatMessage message){
        chats.get(i).addMessage(message);
    }
}