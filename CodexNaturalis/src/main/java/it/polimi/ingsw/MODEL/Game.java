package it.polimi.ingsw.MODEL;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.DeckPackage.DeckGoalCard;
import it.polimi.ingsw.MODEL.ENUM.GameState;
import it.polimi.ingsw.MODEL.Goal.Goal;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*
@Davide
TODO:

    - inserire tutti i metodi non get e set


@Fra

TODO:
*/
public class Game {
    private int num_player;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private Map<Integer,Player> get_player_index;
    public GameState game_state;
    private Goal goal1;
    private Goal goal2;

    private CenterCards cards_in_center;
    private Deck gold_deck, resources_deck, starting_cards_deck;
    private DeckGoalCard goal_deck;

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



    public Game(int num_player, Player player1, Player player2, Player player3, Player player4, Deck gold_deck, Deck resources_deck, Deck starting_cards_deck, DeckGoalCard goal_deck) {
        this.num_player = num_player;
        this.player1 = player1;
        get_player_index.put(0,player1);

        this.player2 = player2;
        get_player_index.put(1,player2);

        if(num_player>2) {
            this.player3 = player3;
            get_player_index.put(2,player2);
            if(num_player>3) {
                this.player4 = player4;
                get_player_index.put(2,player2);
            }
        }

        this.gold_deck = gold_deck;
        this.resources_deck = resources_deck;
        this.starting_cards_deck = starting_cards_deck;
        this.goal_deck = goal_deck;

        initializedCenterCard();
        distributeStartingCard();
        distributeThreeCards();
        selectGoals();
        distributeTwoGoalsToPlayer();
    }
}
