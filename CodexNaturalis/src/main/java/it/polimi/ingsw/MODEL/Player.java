package it.polimi.ingsw.MODEL;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.Card.StartingCard;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Goal.Goal;

import java.util.List;

/*
@Francesco Virgulti 16/03
@Mirko 19/03
@Francesco 19/03
* TODO:
*   -*/
public class Player {
    private final boolean isFirst;
    private int index_removed_card;
    /*
    tc -> transparent card, used when a card is removed from cards_in_hands to set the value
     */
    private final Side tc_front_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    private final Side tc_back_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    private final PlayCard tc = new ResourceCard(tc_front_side, tc_back_side,false, 0);
    private final ColorsEnum color;
    private List<PlayCard> cards_in_hand;
    private PlayerState player_state;
    private GameField game_field;
    private StartingCard starting_car;

    private Goal[] initial_goal_cards;
    private Goal goal_card;
    private int player_points = 0;

    public Player(boolean isFirst, ColorsEnum color, List<PlayCard> cards_in_hand, GameField game_field,Goal[] initial_goal_cards, StartingCard starting_car) {
        this.isFirst = isFirst;
        this.color = color;
        this.cards_in_hand = cards_in_hand;
        this.player_state = PlayerState.NOT_INITIALIZED;
        this.game_field = game_field;
        this.initial_goal_cards = initial_goal_cards;
        this.starting_car = starting_car;
    }
    /*
    getter:
     */
    public boolean getisFirst() {
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

    public StartingCard getStarting_car() {
        return starting_car;
    }

    public Goal getGoal_card() {
        return goal_card;
    }

    /*
        setter:
         */
    public void setPlayer_state(Player p, PlayerState state){
        p.player_state=state;
    }

    public void setCards_in_hand(PlayCard card, int index_removed_card) {
        this.cards_in_hand.set(index_removed_card, card);
    }

    public void setGoal_card(Goal goal_card) {
        this.goal_card = goal_card;
    }

    /*
    * STATE MACHINE
    * Costructor iniziatialized all the player to NOT_INITIALIZED
    */
    public void nextStatePlayer(){
        switch(player_state){
            case NOT_INITIALIZED:
                setPlayer_state(this, PlayerState.CHOOSE_SIDE_FIRST_CARD);
            case CHOOSE_SIDE_FIRST_CARD:
                setPlayer_state(this, PlayerState.CHOOSE_GOAL);
            case CHOOSE_GOAL:
                setPlayer_state(this, PlayerState.BEGIN);
            case BEGIN :
            if(this.isFirst){ // only when the state of player is begin
                setPlayer_state(this, PlayerState.PLACE_CARD);
            }else {
                setPlayer_state(this, PlayerState.WAIT_TURN);
            }
            case PLACE_CARD:
                setPlayer_state(this, PlayerState.DRAW_CARD);
            case DRAW_CARD:
                setPlayer_state(this, PlayerState.WAIT_TURN);
        }
    }

    /*
    *if (player_state != PLACE_CARD
    * index -> posizione nella lista delle carte in mano
    * */
    public void placeCard(int index,boolean flipped, int x, int y){
        if(player_state == PlayerState.PLACE_CARD){
            PlayCard playing_card =  cards_in_hand.get(index);
            playing_card.flipCard(flipped);
            if(game_field.insertCard(playing_card, x, y)){
                removeHandCard(playing_card, index);
            }
        }
        else{
            System.out.println("ERROR: IT'S NOT YOUR TURN");
        }
    }
    private void removeHandCard(PlayCard card, int index){
        this.index_removed_card=index;
        cards_in_hand.set(index, tc);
    }

    public void addPoints(int point){
        this.player_points=this.player_points+point;
    }


    public void insertCard(PlayCard card){
        setCards_in_hand(card, this.index_removed_card);
    }


    public void selectGoal(int i){
        this.goal_card = initial_goal_cards[i];
    }

    //this metod select the first side of the starting_card and put it on the field
    public void selectFirstCard(boolean flipped){
        if(player_state==PlayerState.CHOOSE_SIDE_FIRST_CARD) {
            this.starting_car.flipCard(flipped);
            game_field.insertCard(this.starting_car, 0, 0);
        }else{
            System.out.println("ERROR: IT'S NOT YOUR TURN");
        }
}













}
