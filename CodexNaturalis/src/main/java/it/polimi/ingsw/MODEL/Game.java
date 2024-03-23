package it.polimi.ingsw.MODEL;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.DeckPackage.DeckGoalCard;
import it.polimi.ingsw.MODEL.Goal.Goal;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*
@Davide
TODO:
    - dopo aver fatto classe GameTable istanziarla qui dentro per ogni giocatore
    - inserire tutti i metodi non get e set
    - dopo aver fatto classe Goal instanziarla qui dentro per gli obiettivi pubblici
    - altro da vedere...
    - aggiungere costruttore

-MODIFICHE ESEGUITE
Tolto int[] points perchè i punti sono già presenti dentro Players.getPlayerPoints()
@Fra

TODO:
*/
public class Game {
    private int num_player;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    Goal goal1;
    Goal goal2;

    private CenterCards cards_in_center;
    private Deck gold_deck, resources_deck, starting_cards_deck;
    private DeckGoalCard goal_deck;

    Map<Integer,Player> get_player_index;

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

    public void distributeStartingCard(){
        for(int i=0; i<num_player;i++){
            get_player_index.get(i).setStartingCar(starting_cards_deck.drawCard());
        }
    }

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
    //Setter of points, one for 2 player and one for 4 players
    public void setPoints(int points_player1, int points_player2 ) {

    }
    public void setPoints(int points_player1, int points_player2, int points_player3, int points_player4 ) {

    }




}
