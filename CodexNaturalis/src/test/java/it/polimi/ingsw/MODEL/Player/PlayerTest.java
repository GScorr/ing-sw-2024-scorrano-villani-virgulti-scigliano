package it.polimi.ingsw.MODEL.Player;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.DeckPackage.Deck;
import it.polimi.ingsw.MODEL.DeckPackage.DeckGoalCard;
import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.Game.DeckCreation;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.State.InvalidStateException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private CenterCards cards_in_center;
    private Deck gold_deck,resources_deck, starting_cards_deck;
    private DeckGoalCard goal_deck;

    @Test
    void getPlayerState() {
    }

    @Test
    void getCardsInHand() {
    }

    @Test
    void getStartingCard() {
    }

    @Test
    void getGoalCard() {
    }

    @Test
    void getPlayerPoints() {
    }

    @Test
    void setPlayer_state() {
    }

    @Test
    void setInitialCardsInHand() {
    }

    @Test
    void setInitialGoalCards() {
    }

    @Test
    void setStartingCard() {
    }

    @Test
    void setGoal_card() {
    }

    @Test
    void update() {
    }

    @Test
    void nextStatePlayer() {
    }

    @Test
    void placeCard() {
    }

    @Test
    void addPoints() {
    }

    @Test
    void peachCardFromGoldDeck() {
    }

    @Test
    void peachFromResourcesDeck() {
    }

    @Test
    void peachFromCardsInCenter() {
    }

    @Test
    void selectGoal() {
    }

    @Test
    void selectStartingCard() {
    }

    private void distributeStartingCard(Player p){
            p.actual_state.setStartingCard(starting_cards_deck.drawCard());
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

    private void distributeThreeCards(Player p){

            List<PlayCard> tmp = new ArrayList<PlayCard>();
            tmp.add(gold_deck.drawCard());
            tmp.add(resources_deck.drawCard());
            tmp.add(resources_deck.drawCard());
            p.actual_state.setInitialCardsInHand(tmp);

    }


    private void distributeTwoGoalsToPlayer(Player p){
            List<Goal> tmp = new ArrayList<Goal>();
            tmp.add(goal_deck.drawCard());
            tmp.add(goal_deck.drawCard());
            p.actual_state.setInitialGoalCards(tmp);

    }

    @Test
    public void main(){
        DeckCreation creation = new DeckCreation();
        this.gold_deck = new Deck(creation.getMixGoldDeck());
        this.resources_deck = new Deck(creation.getMixResourcesDeck());
        this.starting_cards_deck = new Deck(creation.getMixStartingDeck());
        this.goal_deck = new DeckGoalCard(creation.getMixGoalDeck());

        Player p1 = new Player(ColorsEnum.BLU, "nome_1",true);
        Player p2 = new Player(ColorsEnum.BLU, "nome_2",false);

        try{assertEquals("NOT_INITIALIZED",p1.actual_state.getNameState());}
        catch(AssertionError e){
            System.out.println(e.getMessage());
        }

        System.out.println("1° TEST: i Player sono inizialmente sullo stato NOT INITIALIZED, non dovrebbe essere possibile chiamare nessun metodo:");

        try{
            distributeStartingCard(p1);
            distributeThreeCards(p1);
            distributeTwoGoalsToPlayer(p1);
         }catch(InvalidStateException e){
            System.out.println(e.getMessage());
        }

        System.out.println("2° TEST: cambio lo stato al Player-> vedo se effettivamente lo stato è cambiato:");
        p1.InitialNextStatePlayer();
        try{assertEquals("BEGIN",p1.actual_state.getNameState());
            System.out.println("prova");}catch(AssertionError e){
            System.out.println(e.getMessage());
        }

    }
}