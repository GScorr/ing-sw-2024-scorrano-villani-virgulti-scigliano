package it.polimi.ingsw.model;
import it.polimi.ingsw.model.Card.GoldCard;
import it.polimi.ingsw.model.Card.ResourceCard;
import it.polimi.ingsw.model.Card.Side;
import it.polimi.ingsw.model.ENUM.AnglesEnum;
import it.polimi.ingsw.model.ENUM.CentralEnum;
import it.polimi.ingsw.model.ENUM.ColorsEnum;
import it.polimi.ingsw.model.ENUM.PlayerState;
import it.polimi.ingsw.model.Card.PlayCard;

import java.util.ArrayList;
import java.util.List;

/*
@Francesco Virgulti 16/03
@Mirko 19/03
* TODO:
*   - al costruttore passo una lista, questa lista può essere direttamente copiata nella mia variabile oppure devo prima
*    copiarla in una lista ausiliaria ?
*   - impostare il metodo peach() -> gestito da FSA ?
*   - impostare il metodo peakGoal() -> gestito da FSA ?
*   - impostare il metodo chooseSideFirstSide()
    - creare un metodo viewCard()
    -scrivi tutti i getter() e i setter()
    -scrivere commento su placeCard() DONE
    -placeCard() funziona passandogli la carta()
    - la lista deve avere al suo interno anche i flipped della carta()
    -metodo rimuovi DONE
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
    private int player_points = 0;

    public Player(boolean isFirst, ColorsEnum color, List<PlayCard> cards_in_hand, GameField game_field) {
        this.isFirst = isFirst;
        this.color = color;
        this.cards_in_hand = cards_in_hand;
        this.player_state = PlayerState.NOT_INITIALIZED;
        this.game_field = game_field;
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
    public PlayerState getPlayer_state() {
        return player_state;
    }
    public GameField getGame_field() {
        return game_field;
    }
    public List<PlayCard> getCards_in_hand() {
        return cards_in_hand;
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
                rimuoviCartaMano(playing_card, index);
            }
        }
        else{
            System.out.println("ERROR: IT'S NOT YOUR TURN");
        }
    }
    private void rimuoviCartaMano(PlayCard card, int index){
        this.index_removed_card=index;
        cards_in_hand.set(index, tc);
    }

    public void addPoints(int point){
        this.player_points=this.player_points+point;
    }


    public void inserctCard(PlayCard card){
        setCards_in_hand(card, this.index_removed_card);
    }






}
