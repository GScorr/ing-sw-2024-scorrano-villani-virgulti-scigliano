package it.polimi.ingsw.MODEL.Player;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.Card.StartingCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.State.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


/**
 * Manages player state, cards, points, and game interaction. Implements PlayerObserver. Serializable.
 */
public class Player implements PlayerObserver, Serializable {
    private String name;

    private int position;
    public PState
            not_initialized = new NotInitialized(this),
            begin = new Begin(this),
            choose_goal = new ChooseGoal(this),
            choose_side_first_card = new ChooseSideFirstCard(this),
            //wait_turn = new WaitTurn(this),
            place_card = new PlaceCard(this),
            draw_card = new DrawCard(this),
            wait_turn = new WaitTurn(this),
            end_game = new EndGame(this),
            actual_state;


    private final boolean isFirst;
    private int index_removed_card;

    public HashMap<Integer,Boolean> side_card_in_hand  = new HashMap<>();



    /**
     * tc -> transparent card, used when a card is removed from cards_in_hands to set the value
     */
    private final Side tc_back_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    private final Side tc_front_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    private PlayCard tc = new ResourceCard(tc_front_side, tc_back_side,false, 0);

    private final ColorsEnum color;
    private List<PlayCard> cards_in_hand;
    private PlayerState player_state;
    private GameField game_field;
    private PlayCard starting_card;
    private List<Goal> initial_goal_cards;
    private Goal goal_card;
    private int player_points = 0;

    private int num_goal_achieve = 0;

    private boolean isDisconnected;
    private Goal global_goal1;
    private Goal global_goal2;

    /**
     * This deck are use to draw
     */
    private CenterCards cards_in_center;
    private Deck gold_deck, resources_deck;
    private boolean firstPlaced = false;

    public boolean isFirstPlaced() {
        return firstPlaced;
    }

    public boolean isDisconnected(){
        return isDisconnected;
    }

    public void disconnect(){
        this.isDisconnected = true;
    }
    public void connect(){
        this.isDisconnected = false;
    }


    public Player( ColorsEnum color, String name, boolean isFirst){
        this.name = name;
        this.color = color;
        this.player_state = PlayerState.NOT_INITIALIZED;
        createField();
        this.actual_state = not_initialized;
        this.isFirst = isFirst;
        this.isDisconnected = false;
    }

    public String getName() {
        return name;
    }

    /**
     * create the player field
     */
    private void createField(){
        Side tc_front_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
        Side tc_back_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
        PlayCard tc = new ResourceCard(tc_front_side, tc_back_side,false, 0);
        GameFieldSingleCell[][] array_single_cell = new GameFieldSingleCell[45][45];
        for (int i = 0; i < 45; i++) {
            for (int j = 0; j < 45; j++) {
                GameFieldSingleCell tmp = new GameFieldSingleCell(false, tc, AnglesEnum.EMPTY, tc);
                array_single_cell[i][j] = tmp;
            }
        }
        this.game_field = new GameField(array_single_cell, this);
    }

    /**
     * Getter method
     */

    public boolean getIsFirst() {
        return isFirst;
    }
    public ColorsEnum getColor() {
        return color;
    }
    public PlayerState getPlayerState() {
        return player_state;
    }
    public GameField getGameField() {
        return game_field;
    }
    public List<PlayCard> getCardsInHand() {
        return cards_in_hand;
    }
    public PlayCard getStartingCard() {
        return starting_card;
    }
    public Goal getGoalCard() {
        return goal_card;
    }
    public List<Goal> getInitial_goal_cards() {
        return initial_goal_cards;
    }
    public int getPlayerPoints() {
        return player_points;
    }
    public Deck getGold_deck() {
        return gold_deck;
    }
    public Deck getResources_deck() {
        return resources_deck;
    }

    /**
     * Setter method
     */

    public void setPlayer_state( PState state){
        this.actual_state=state;
    }
    public void setInitialCardsInHand(List<PlayCard> cards_in_hand){
        this.cards_in_hand = cards_in_hand;
    }
    public void setInitialGoalCards(List<Goal> initial_goal_cards){
        this.initial_goal_cards = initial_goal_cards;
    }
    public void setStartingCard(PlayCard starting_card) {
        this.starting_card = starting_card;
    }
    /* da eliminare
    public void setGoal_card(Goal goal_card) {
        this.goal_card = goal_card;
    }

     */
    public void setDeck(Deck resources_deck, Deck gold_deck,CenterCards cards_in_center){
        game_field.setGlobalGoal(global_goal1,global_goal2);
        this.resources_deck = resources_deck;
        this.gold_deck = gold_deck;
        this.cards_in_center = cards_in_center;
    }

    /**
     * Updates the player's state to the next appropriate state.
     *
     */
    public void update(){
        InitialNextStatePlayer();
    }

    /**
     * Initializes the player's state to `NOT_INITIALIZED`.
     *
     * NOT INITIALIZED => the player enters the game and must wait for all players to join and the game to actually start
     * BEGIN => the GAME class distributes the cards to the Players (and the cards on the table). All methods for setting the initial cards are active
     * CHOOSE_GOAL => choose the objective
     * CHOOSE_SIDE_FIRST_CARD => place the first card
     *
     */
    public void InitialNextStatePlayer(){
        switch(actual_state.getNameState()){
            case "NOT_INITIALIZED":
                setPlayer_state(begin);
                return;
            case "BEGIN":
                setPlayer_state(choose_goal);
                return;
            case "CHOOSE_GOAL":
                setPlayer_state( choose_side_first_card);
                return;
            case "CHOOSE_SIDE_FIRST_CARD":
                if(this.isFirst){
                    setPlayer_state( place_card);
                }
                else {
                    setPlayer_state(wait_turn);
                }
                return;
        }

    }

    /**
     * Advances the player's state to the next appropriate state based on their current state.
     *
     * This method is called after the player has performed an action and needs to transition
     * to the next state in the game sequence. It is only called on the player who has
     * just completed their action, while other players remain in their current state.
     */
    public void nextStatePlayer(){
        switch(actual_state.getNameState()){
            case "WAIT_TURN" :
                    setPlayer_state( place_card);
                    return;
            case "PLACE_CARD":
                setPlayer_state( draw_card );
                return;
            case "DRAW_CARD":
                setPlayer_state( wait_turn);
                return;
            case "END_GAME":
                return;
        }
    }

    public void setEndGame(){
        setPlayer_state(end_game);
    }

    /**
     * Places a card from the player's hand onto the game field.
     *
     * @param index the index of the card in the player's hand to place
     * @param flipped whether to flip the card face up (true) or face down (false)
     * @param x the x-coordinate of the target location on the game field
     * @param y the y-coordinate of the target location on the game field
     * @throws InvalidBoundException if the card index is out of bounds
     * @throws IllegalStateException if the player is not in a state where placing cards is allowed
     */
    public void placeCard(int index,boolean flipped, int x, int y){
        PlayCard playing_card =  cards_in_hand.get(index);
        playing_card.flipCard(flipped);
        game_field.insertCard(playing_card, x, y);
        removeHandCard(playing_card, index);
    }

    /**
     * Removes a card from the player's hand at the specified index.
     *
     * This method is likely called internally by the `placeCard` method
     * after a card is successfully placed on the game field.
     *
     * @param card the card to remove from the player's hand
     * @param index the index of the card to remove
     * @throws InvalidBoundException if the card index is out of bounds
     */
    private void removeHandCard(PlayCard card, int index){
        this.index_removed_card=index;
        tc.setBack_side_path(null);
        tc.setFront_side_path(null);
        cards_in_hand.set(index, tc);
    }

    /**
     * Adds points to the player's score based on the played card.
     *
     * This method is called after a card is successfully placed on the game field.
     * The amount of points awarded is determined by the played card's properties.
     *
     * @param point the value of the points awarded by the card
     */
    public void addPoints(int point){
        this.player_points=this.player_points+point;
    }

    public void setPlayer_points(int player_points) {
        this.player_points = player_points;
    }

    /**
     * Inserts a drawn card into the player's hand at a specific index.
     *
     * This method is called after a card is successfully drawn from the deck.
     * The card is inserted at the position where a card was previously removed
     *
     * @param card the drawn card to insert into the player's hand
     */
    private void insertCard(PlayCard card){
        this.side_card_in_hand.put(index_removed_card,false);
        this.cards_in_hand.set(this.index_removed_card, card);
    }


    public void selectSideCard(int index, boolean flipp){
        cards_in_hand.get(index).flipCard(flipp);
    }

    /**
     * draws method
     */
    public void peachCardFromGoldDeck(){
       insertCard(this.gold_deck.drawCard());
    }

    public void peachFromResourcesDeck(){
        insertCard(this.resources_deck.drawCard());
    }

    public void peachFromCardsInCenter(int i){
        if(i==0){
            insertCard(cards_in_center.drawGoldCard(0));
        }else if(i==1){
            insertCard(cards_in_center.drawGoldCard(1));
        }else if(i==2){
            insertCard(cards_in_center.drawResourceCard(0));
        } else if (i == 3) {
            insertCard(cards_in_center.drawResourceCard(1));
        }
    }

    /**
     * Selects a goal card from the initial choices offered to the player.
     *
     * This method is only used during the initial phase of the game, where the player
     * chooses one of the two goal cards.
     *
     * @param i the index of the chosen goal card within the initial options
     */
    public void selectGoal(int i){
        this.goal_card = initial_goal_cards.get(i);
    }

    /**
     * this metod select the side of the starting_card and put it on the field
     *
     * @param flipped false when the card is face up, true when it's face down
     */
    public void selectStartingCard(boolean flipped){
            this.starting_card.flipCard(flipped);
            game_field.insertCard(this.starting_card, 22, 22);
            game_field.startingCardResourcesAdder((StartingCard) starting_card);
            firstPlaced = true;
    }

    public PState getActual_state() {
        return actual_state;
    }

    public int getNum_goal_achieve() {
        return num_goal_achieve;
    }

    public void setNum_goal_achieve(int num_goal_achieve) {
        this.num_goal_achieve = num_goal_achieve;
    }

    public void setGlobalGoal(Goal goal1, Goal goal2 ){
        global_goal1 = goal1;
        global_goal2 = goal2;}

    public void setPosition(int i) {
        this.position = i;
    }
}
