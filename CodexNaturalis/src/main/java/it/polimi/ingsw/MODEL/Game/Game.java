package it.polimi.ingsw.MODEL.Game;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.DeckPackage.DeckGoalCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MODEL.Player.PlayerObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*
@Davide
TODO:



@Fra

TODO:
    - spiega cosa hai fatto => creazione degli stati del Player (questi saranno modificati dalla classe Game)
    - chiedi riga -170
    - chiedi riga 196
    - tutti i metodi devono essere chiamati dalla classe GAME => {
        quando un player gioca, verrà chiamato Game.letPlayerPlaceCard()
            letPlayerPlaceCard(){
                Player.actual_state.placeCard();
                Game.nextStatePlayer();
            }
            In questo modo ogni volta che un player gioca cambiano gli stati di tutti gli altri player che poi dovranno giocare
            Per capire meglio guarda funzione nextStatePlayer() all'interno di Games

    }
    - Creazione macchina a stati per il GAME

*/
public class Game implements GameSubject{
    DeckCreation creation;
    private int num_player;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private int actual_player = 0;  //tiene traccia del giocatore che sta giocando
    private Map<Integer,Player> get_player_index;
    private Goal goal1;
    private Goal goal2;

    private CenterCards cards_in_center;
    private Deck gold_deck,resources_deck, starting_cards_deck;
    private DeckGoalCard goal_deck;

    private List<PlayerObserver> player_observers = new ArrayList<>();



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

    public CenterCards getCars_in_center() {
        return cards_in_center;
    }

    public Deck getGold_deck() {
        return gold_deck;
    }

    public Deck getResources_deck() {
        return resources_deck;
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

    public void distributeTwoGoalsToPlayer(){
        for(int i=0;i<num_player;i++){
            List<Goal> tmp = new ArrayList<Goal>();
            tmp.add(goal_deck.drawCard());
            tmp.add(goal_deck.drawCard());
            get_player_index.get(i).setInitialGoalCards(tmp);
        }
    }



    public Game( DeckGoalCard goal_deck) {
        this.creation = new DeckCreation();
        this.gold_deck = new Deck(creation.getMixGoldDeck());
        this.resources_deck = new Deck(creation.getResourcesDeck());
        this.starting_cards_deck = new Deck(creation.getStartingDeck());
        this.goal_deck = goal_deck;

    }

    public void InsertPlayer(Player player){
        if(this.num_player > 4 ){
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

            registerObserver(player);
            this.num_player++;

        }
    }

    //prima di chiamare questo metodo devo vedere se i Players sono >= 2 &&  < 4 (non possono mai essere per metodo InsertPlayer)
    public void initializedGame(){
        if(num_player < 2 || num_player > 4){ throw new LimitNumPlayerException("I giocatori non sono a sufficienza, non è possibile creare il gioco");}
        else{
                //prima di chiamare questi metodi devo dire a tutti i Player di cambiare stato
                // chiedi se questo deve essere fatto qua oppure deve avvenire parallelamente con tutti i thread -> per ora qua
                // ----
                notifyObservers();
                // ----
                distributeStartingCard();
                initializedCenterCard();
                distributeThreeCards();
                selectGoals();
                distributeTwoGoalsToPlayer();
            }
    }


    // queste funzioni saranno chiamate dalla macchina a STATI del Game

    public void selectGoalForPlayers(){
        notifyObservers();
    }

    public void insertFirstCardForPlayers(){
        notifyObservers();
    }
    public void startTurnForPlayers(){
        notifyObservers();
    }

    // tutti i giocatori devono aver posizionato la carta e tutti i giocatori devono avere scelto il goal prima di proseguire
    // come implementarlo ???

    // quando il turno del gioco effettivo sarà stato chiamato per cambiare il turno ai giocatori si userà questo metodo
    public void nextStatePlayer(){
        Player currentPlayer = get_player_index.get(actual_player), nextPlayer;
        if(currentPlayer.actual_state.getNameState().equals("PLACE_CARD")){
            currentPlayer.nextStatePlayer();
        }else if (currentPlayer.actual_state.getNameState().equals("DRAW_CARD")){
            if(actual_player + 1 == num_player ){
                nextPlayer = get_player_index.get(0);}
            else{
                nextPlayer = get_player_index.get(actual_player + 1);
            }
            currentPlayer.nextStatePlayer();
            nextPlayer.nextStatePlayer();
            if(actual_player + 1 == num_player ){
                actual_player = 0;
            }
            else{
                actual_player ++;
            }
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