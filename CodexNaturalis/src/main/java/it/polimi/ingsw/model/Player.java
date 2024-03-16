package it.polimi.ingsw.model;
import it.polimi.ingsw.model.ENUM.ColorsEnum;
import it.polimi.ingsw.model.ENUM.PlayerState;
import it.polimi.ingsw.model.PlayCard;
import it.polimi.ingsw.model.GameField;

import java.util.ArrayList;
import java.util.List;

/*
@Francesco Virgulti
* TODO:
*   - al costruttore passo una lista, questa lista può essere direttamente copiata nella mia variabile oppure devo prima
*    copiarla in una lista ausiliaria ?
*   - impostare il metodo peach()
*   - impostare il metodo peakGoal()
*   - impostare il metodo chooseSideFirstSide()
*   -*/
public class Player {
    private boolean isFirst;
    private final ColorsEnum color;
    private List<PlayCard> cards_in_hand = new ArrayList<>();
    private PlayerState player_state;

    private GameField game_field;

    public Player(boolean isFirst, ColorsEnum color, List<PlayCard> cards_in_hand, GameField game_field) {
        this.isFirst = isFirst;
        this.color = color;
        this.cards_in_hand = cards_in_hand;
        this.player_state = PlayerState.NOT_INITIALIZED;
        this.game_field = game_field;
    }
    /*
    * STATE MACHINE
    * Costructor iniziatialized all the player to NOT_INITIALIZED
    */
    public void nextPlayerState(){
        switch(player_state){
            case NOT_INITIALIZED:
                this.player_state = PlayerState.CHOOSE_SIDE_FIRST_CARD;
            case CHOOSE_SIDE_FIRST_CARD:
                this.player_state = PlayerState.CHOOSE_GOAL;
            case CHOOSE_GOAL:
                this.player_state = PlayerState.BEGIN;
            case BEGIN :
            if(this.isFirst){
                this.player_state = PlayerState.PLACE_CARD;
            }else {
                this.player_state = PlayerState.WAIT_TURN;
            }
            case PLACE_CARD:
                    this.player_state = PlayerState.DRAW_CARD;
            case DRAW_CARD:
                this.player_state = PlayerState.WAIT_TURN;
        }
    }

    /*
    *if (player_state != PLACE_CARD*/
    public void placeCard(int index,boolean flipped, int x, int y){
        if(player_state == PlayerState.PLACE_CARD){
            PlayCard playing_card =  cards_in_hand.get(index);
            playing_card.flipCard(flipped);
            game_field.insertCard(playing_card,x,y);
        }
        else{
            System.out.println("ERROR: IT'S NOT YOUR TURN");
        }
    }






}
