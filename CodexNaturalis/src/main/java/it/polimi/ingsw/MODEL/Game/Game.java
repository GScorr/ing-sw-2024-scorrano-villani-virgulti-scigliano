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

/*
*       GEMESUBJECT : la classe Game comunica agli ascoltatori (i Player) di cambiare stato quando:
*                   - si passa dallo stato NOT INITIALIZED (bisogna attendere che tutti i giocatori entrino nel game) allo stato BEGIN (si distribuiscono le carte)
*                   - si passa dallo stato BEGIN allo stato CHOOSE GOAL (  le carte sono state distribuite (sia i deck sul tavolo, sia le carte dei giocatori) e TUTTI i player devono scegliere il loro goal
*                   - si passa dallo stato CHOOSE GOAL allo stato CHOOSE_STARTING_CARD (tutti i player devono scegliere il side della loro carta)
*                   - si passa dallo stato CHOOSE_STARTING_CARD allo stato di GIOCO effettivo (al momento della scrittura di questo commento non ho ancora scelto il nome)
*                           quando per ogni observer (player) si chiama il passaggio di stato, solo uno di questi player avrà la variabile is_first = true => quel player sarà il primo giocatore
*       GAMEOBSERVER : quando i player e il Game si trovano in CHOOSE_GOAL bisognerà che tutti i player scelgano il goal prima di proseguire il GAME
*                       la classe di Game si comporta da Observer: ogni volta che un giocatore chiamerà il metodo di scelta GoalCard questa classe se ne accorgerà
*                       (guardare il metodo playerChooseGoal() x capire meglio
*
*                       stessa cosa per stato CHOOSE_SIDE_FIRST_CARD
* */
public class Game implements GameSubject, GameObserver{
    DeckCreation creation;
    private int num_player;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private int actual_player = 0;  //tiene traccia del giocatore che sta giocando
    private int goal_count = 0;  // tiene traccia di quanti player hanno scelto il goal
    private int starting_card_count = 0; // tiene traccia di quan ti player hanno scelto il side della carta iniziale
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


    public Game( DeckGoalCard goal_deck) {
        this.creation = new DeckCreation();
        this.gold_deck = new Deck(creation.getMixGoldDeck());
        this.resources_deck = new Deck(creation.getResourcesDeck());
        this.starting_cards_deck = new Deck(creation.getStartingDeck());
        this.goal_deck = goal_deck;

    }

    public void insertPlayer(Player player){
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
            player.registerObserver(this);
        }
    }

    //prima di chiamare questo metodo devo vedere se i Players sono >= 2 &&  < 4 (non possono mai essere per metodo InsertPlayer)
    public void initializedGame(){
        if(num_player < 2 || num_player > 4){ throw new LimitNumPlayerException("I giocatori non sono a sufficienza, non è possibile creare il gioco");}
        else{
                //prima di chiamare questi metodi devo dire a tutti i Player di cambiare stato
                // chiedi se questo deve essere fatto qua oppure deve avvenire parallelamente con tutti i thread -> per ora qua

                // tutti i player passano da NOT INITIALIZED a BEGIN
                // ----
                notifyObservers();
                // ----
                distributeStartingCard();
                initializedCenterCard();
                distributeThreeCards();
                selectGoals();
                distributeTwoGoalsToPlayer();

                // tutti i player passano da BEGIN a CHOOSE_GOAL
                notifyObservers();
            }
    }


    //ogni volta che il player sceglierà un goal questo metodo verrà invocato
    //  il metodo farà count++
    //  se count==numPlayer allora tutti i player passeranno allo stato ChooseStartingCard,
    // #Anche il Game passerà allo stato StartingCard
    private void playerChooseGoal(){
        goal_count++;

        //tutti i player hanno scelto il loro GOAL
        //il player passa da CHOOSE_GOAL a CHOOSE_STARTING_CARD
        if(goal_count == num_player){
            notifyObservers();
        }
        else{

        }
    }

    private void playerChooseStartingCard(){
        starting_card_count++;

        //tutti i player hanno scelto il loro GOAL
        //il player passa da CHOOSE_STARTING_CARD al prossimo stato

        if(starting_card_count == num_player){
            notifyObservers();
        }
        else{

        }
    }



// ------ da qui in avanti inizia il gioco con i turni



    // quando il turno del gioco effettivo sarà stato chiamato per cambiare il turno ai giocatori si userà questo metodo
    private void nextStatePlayer(){
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

//non so se player_caller deve essere passato
public boolean playerPlaceCard(Player player_caller, int index,boolean flipped, int x, int y){
        if(player_caller.equals(get_player_index.get(actual_player))){
            player_caller.placeCard(index,flipped,x,y);
            nextStatePlayer();
            return true;
        }else{
            System.out.println("Il player non può chiamare questa funzione");
            return false;
        }
}

public boolean playerPeachCardFromGoldDeck(Player player_caller){
    if(player_caller.equals(get_player_index.get(actual_player))){
        player_caller.peachCardFromGoldDeck();
        nextStatePlayer();
        return true;
    }else{
        System.out.println("Il player non può chiamare questa funzione");
        return false;
    }
}

public boolean playerPeachCardFromResourcesDeck(Player player_caller){
    if(player_caller.equals(get_player_index.get(actual_player))){
        player_caller.peachFromResourcesDeck();
        nextStatePlayer();
        return true;
    }else{
        System.out.println("Il player non può chiamare questa funzione");
        return false;
    }
}

public boolean playerPeachFromCardsInCenter(Player player_caller, int i){
    if(player_caller.equals(get_player_index.get(actual_player))){
        player_caller.peachFromCardsInCenter(i);
        nextStatePlayer();
        return true;
    }else{
        System.out.println("Il player non può chiamare questa funzione");
        return false;
    }
}



    //---- metodi per il design pattern observer


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

    @Override
    public void updateChooseGoal() {
        playerChooseGoal();
    }

    @Override
    public void updateChooseStartingCard() {
        playerChooseStartingCard();
    }
}