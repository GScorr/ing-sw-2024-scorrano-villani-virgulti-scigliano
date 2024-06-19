package it.polimi.ingsw.MODEL.Game;
import it.polimi.ingsw.Chat;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.DeckPackage.DeckGoalCard;
import it.polimi.ingsw.MODEL.Game.State.*;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    todo
        eliminare i metodi commmentati
 */

/**
 * This class manages the overall game state and flow.
 */
public class Game implements Serializable {

    /**
     * creation of the deck
     */
    DeckCreation creation;

    /**
     * istantiation of player used during th game
     */
    private int max_num_player;
    private int num_player;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Integer index_game;
    private String name;

    /**
     * this map stores associations between a player's unique index and the corresponding player object
     */
    private Map<Integer,Player> get_player_index = new HashMap<>();

    /**
     * common goal for the game
     */
    private Goal goal1;
    private Goal goal2;

    /**
     * deck and the center card for the game
     */
    private CenterCards cards_in_center;
    private Deck gold_deck,resources_deck, starting_cards_deck;
    private DeckGoalCard goal_deck;
    private List<Chat> chats = new ArrayList<>();

    /**
     *
     */
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

    //da cancellare -- chiedere a fra
    public Game( /* DeckGoalCard goal_deck */ int max_num_player) {
        this.creation = new DeckCreation();

        /*  DECK NON MISCHIATO
        this.gold_deck = new Deck(creation.getGoldDeck());
        this.resources_deck = new Deck(creation.getResourcesDeck());
         */

        //shuffled deck
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

    /**
     * contstructor of the class
     * @param name
     * @param max_num_player
     */
    public Game(String name, int max_num_player) {
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

    /**
     * Inserts a player into the game.
     *
     * @param player the player to insert
     * @throws IllegalArgumentException if the number of players already reaches the maximum (4)
     */
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

    /**
     *
     * This method performs several setup tasks to prepare the game for play, including:
     *  * Distributing starting cards to players
     *  * Initializing the center cards
     *  * Distributing three play cards to players
     *  * Selecting game goals
     *  * Distributing two goals to each player
     *  * Setting player decks with resources, gold, and center cards
     */
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

    /**
     * Distributes starting cards to players.
     *
     * This method deals one starting card to each player from the starting_cards_deck.
     */
    public void distributeStartingCard(){
        for(int i=0; i<num_player;i++){
            get_player_index.get(i).setStartingCard(starting_cards_deck.drawCard());
        }
    }

    /**
     * Initializes the center cards for the game.
     *
     * This method creates a new CenterCards object with four cards:
     *  * Two cards drawn from the gold deck
     *  * Two cards drawn from the resource deck
     */
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

    /**
     * Distributes three additional cards to each player.
     *
     * This method deals three cards to each player:
     *  * One card drawn from the gold deck
     *  * Two cards drawn from the resource deck
     */
    public void distributeThreeCards(){
        for (int i = 0; i<num_player;i++){
            List<PlayCard> tmp = new ArrayList<PlayCard>();
            tmp.add(gold_deck.drawCard());
            tmp.add(resources_deck.drawCard());
            tmp.add(resources_deck.drawCard());
            get_player_index.get(i).setInitialCardsInHand(tmp);
        }
    }

    /**
     * set 2 goal for the game
     */
    public void selectGoals(){
        goal1 = goal_deck.drawCard();
        goal2 = goal_deck.drawCard();
        player1.setGlobalGoal(goal1,goal2);
        if( player3 != null )   player3.setGlobalGoal(goal1,goal2);
        player2.setGlobalGoal(goal1,goal2);
        if( player4 != null )  player4.setGlobalGoal(goal1,goal2);
    }

    /**
     * Distributes two random goals to each player.
     *
     * This method deals two cards from the goal deck to each player. The cards are added to a temporary list
     * and then assigned to the player's initial goal cards using `setInitialGoalCards`.
     */
    public void distributeTwoGoalsToPlayer(){
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

    /**
     * Transitions the game to the next state.
     *
     * This method progresses the game state based on the current state (`actual_state`).
     * Here's the flow of transitions:
     *  * NOT_INITIALIZED -> BEGIN
     *  * BEGIN -> CHOOSING_GOAL
     *  * CHOOSING_GOAL -> CHOOSING_STARTING_CARD
     *  * CHOOSING_STARTING_CARD -> TURN
     *  * TURN -> END_GAME
     */
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

    /**
     * Inserts a chat message into a specific chat conversation.
     *
     * @param i the index of the chat conversation in the `chats` list (0-based indexing)
     * @param message the chat message to insert
     * @throws IndexOutOfBoundsException if the provided chatIndex is outside the bounds of the `chats` list
     */
    public void insertMessageinChat(int i, ChatMessage message){
        chats.get(i).addMessage(message);
    }

}