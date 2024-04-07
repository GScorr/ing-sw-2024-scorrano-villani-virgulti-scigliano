package it.polimi.ingsw.MODEL.Player;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Game.GameObserver;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.State.*;

import java.util.List;

/*
@Francesco Virgulti 16/03
@Mirko 19/03
@Francesco 19/03
* TODO:
    - Spiega cosa hai fatto con gli stati del Player e perchè sono utili{
        I metodi che non influenzano lo stato devono essere chiamata dallo PState actual_state
        Gli stati di Player influiscono sulla View => ogni volta che verrà cambiato lo stato del Player verrà notificata questa cosa alla view che cambierà
        Per questo è importante chiamare i metodi di Player direttamente da Game => non cambia solo lo stato del player attuale ma anche degli altri
        }
*   -*/



/* (concetto spiegato  nella classe GAME
* PLAYER_OBSERVER:
* PLAYER_SUBJECT
* */
public class Player implements PlayerObserver,PlayerSubject {

    public PState
            not_initialized = new NotInitialized(this),
            begin = new Begin(this),
            choose_goal = new ChooseGoal(this),
            choose_side_first_card = new ChooseSideFirstCard(this),
            wait_turn = new WaitTurn(this),
            place_card = new PlaceCard(this),
            draw_card = new DrawCard(this),
            actual_state;


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
    private PlayCard starting_card;

    private List<Goal> initial_goal_cards;
    private Goal goal_card;
    private int player_points = 0;

    //Questi mazzi servono per pescare
    private CenterCards cards_in_center;
    private Deck gold_deck, resources_deck;

    GameObserver gameObserver;

    public Player(boolean isFirst, ColorsEnum color, GameField game_field){
        this.isFirst = isFirst;
        this.color = color;
        this.player_state = PlayerState.NOT_INITIALIZED;
        this.game_field = game_field;
        this.actual_state = not_initialized;
    }
    /*
    getter:
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

    public int getPlayerPoints() {
        return player_points;
    }

    /*
            setter:
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

    public void setGoal_card(Goal goal_card) {
        this.goal_card = goal_card;
    }

    /*
    * STATE MACHINE
    * Costructor iniziatialized all the player to NOT_INITIALIZED
    * Il model del game riesce a cambiare lo stato del player grazie all'PlayerObserver
    */
    public void update(){
        InitialNextStatePlayer();
    }

    // NOT INITIALIZED => il player entra nella partita e deve attendere che tutti i player entrino e il gioco effettivamente inizi
    // BEGIN => la classe GAME distribuisce le carte ai Player (e le carte sul tavolo). Sono attivi tutti i metodi di setting delle carte iniziali
    // CHOOSE_GOAL => scelta dell'obbiettivo
    // CHOOSE_SIDE_FIRST_CARD => piazza la prima carta
    // Iniziano i turni di gioco per tutti i player, attenzione a quale player è il primo

    //questi cambiamenti vengono chiamati su tutti i player, perchè contemporaneamente devono eseguire i comandi
    // Es: tutti  i player devono scegliere "contemporaneamente" (sullo stesso stato di Game)  il loro obbiettivo prima di proseguire il gioco
    // tutti i  plater devono piazzare la loro starting_car prima di proseguire il gioco con il prossimo stato
    private void InitialNextStatePlayer(){
        switch(actual_state.getNameState()){
            case "NOT_INITIALIZED":
                setPlayer_state(begin);
            case "BEGIN":
                setPlayer_state(choose_goal);
            case "CHOOSE_GOAL":
                setPlayer_state( choose_side_first_card);
            case "CHOOSE_SIDE_FIRST_CARD":
                if(this.isFirst){
                    setPlayer_state( place_card);
                }
                else {
                    setPlayer_state(wait_turn);
                }

        }
    }

    //questi cambiamenti vengono fatti quando:
    // Il player ha giocato la carta e deve passare da uno stato di
    // La funzione deve essere chiamata su un singolo player perchè se ci son 3 giocatori, questa dovrà essere chiamata solo su 2, quello che passa da attesa a gioco e quello che passa da gioco a attesa
    public void nextStatePlayer(){
        switch(actual_state.getNameState()){
            case "WAIT_TURN" :
                    setPlayer_state( place_card);
            case "PLACE_CARD":
                setPlayer_state( draw_card );
            case "DRAW_CARD":
                setPlayer_state( wait_turn);
        }
    }


    /*
    *if (player_state != PLACE_CARD
    * index -> posizione nella lista delle carte in mano
    * */
    public void placeCard(int index,boolean flipped, int x, int y){
            PlayCard playing_card =  cards_in_hand.get(index);
            playing_card.flipCard(flipped);
            if(game_field.insertCard(playing_card, x, y)){
                removeHandCard(playing_card, index);
            }
    }
    private void removeHandCard(PlayCard card, int index){
        this.index_removed_card=index;
        cards_in_hand.set(index, tc);
    }

    public void addPoints(int point){
        this.player_points=this.player_points+point;
    }


    //questo metodo serve a peachFrom...  per inserire la carta pescata
    private void insertCard(PlayCard card){
        this.cards_in_hand.set(this.index_removed_card, card);
    }

    //Una volta che il giocatore ha giocato una carta ne deve pescare una
    //Il front end sceglierà quale metodo chiamare in base a dove uno clicca
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





    public void selectGoal(int i){
            this.goal_card = initial_goal_cards.get(i);

            //notifica al Game che il player ha chiamato questo metodo
            gameObserver.updateChooseGoal();
    }


    //this metod select the first side of the starting_card and put it on the field
    public void selectStartingCard(boolean flipped){

            this.starting_card.flipCard(flipped);
            game_field.insertCard(this.starting_card, 0, 0);
            //notifica al Game che il player ha chiamato questo metodo
            gameObserver.updateChooseStartingCard();
    }



    //questo metodi servo
    @Override
    public void registerObserver(GameObserver gameObserver) {
        this.gameObserver = gameObserver;
    }

    @Override
    public void removeObserver(GameObserver gameObserver) {

    }

    @Override
    public void notifyObservers() {

    }
}
