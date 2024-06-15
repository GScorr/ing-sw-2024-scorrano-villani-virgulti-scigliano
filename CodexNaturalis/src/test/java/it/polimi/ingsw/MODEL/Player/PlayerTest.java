//package it.polimi.ingsw.MODEL.Player;
//
//import it.polimi.ingsw.MODEL.Card.PlayCard;
//import it.polimi.ingsw.MODEL.Card.ResourceCard;
//import it.polimi.ingsw.MODEL.Card.Side;
//import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
//import it.polimi.ingsw.MODEL.DeckPackage.Deck;
//import it.polimi.ingsw.MODEL.DeckPackage.DeckGoalCard;
//import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
//import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
//import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
//import it.polimi.ingsw.MODEL.Game.DeckCreation;
//import it.polimi.ingsw.MODEL.Goal.Goal;
//import it.polimi.ingsw.MODEL.Player.State.InvalidStateException;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PlayerTest {
//    private CenterCards cards_in_center;
//    private Deck gold_deck,resources_deck, starting_cards_deck;
//    private DeckGoalCard goal_deck;
//    private final Side tc_back_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
//    private final Side tc_front_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
//    private final PlayCard tc = new ResourceCard(tc_front_side, tc_back_side,false, 0);
//
//    @Test
//    void getPlayerState() {
//    }
//
//    @Test
//    void getCardsInHand() {
//    }
//
//    @Test
//    void getStartingCard() {
//    }
//
//    @Test
//    void getGoalCard() {
//    }
//
//    @Test
//    void getPlayerPoints() {
//    }
//
//    @Test
//    void setPlayer_state() {
//    }
//
//    @Test
//    void setInitialCardsInHand() {
//    }
//
//    @Test
//    void setInitialGoalCards() {
//    }
//
//    @Test
//    void setStartingCard() {
//    }
//
//    @Test
//    void setGoal_card() {
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void nextStatePlayer() {
//    }
//
//    @Test
//    void placeCard() {
//    }
//
//    @Test
//    void addPoints() {
//    }
//
//    @Test
//    void peachCardFromGoldDeck() {
//    }
//
//    @Test
//    void peachFromResourcesDeck() {
//    }
//
//    @Test
//    void peachFromCardsInCenter() {
//    }
//
//    @Test
//    void selectGoal() {
//    }
//
//    @Test
//    void selectStartingCard() {
//    }
//
//    private void distributeStartingCard(Player p){
//            p.actual_state.setStartingCard(starting_cards_deck.drawCard());
//    }
//
//    //4 cards at the center has to be initialized
//    private void initializedCenterCard(){
//        List<PlayCard> gold_list = new ArrayList<PlayCard>();
//        List<PlayCard> resource_list= new ArrayList<PlayCard>();;
//        gold_list.add(gold_deck.drawCard());
//        gold_list.add(gold_deck.drawCard());
//
//        resource_list.add(resources_deck.drawCard());
//        resource_list.add(resources_deck.drawCard());
//        CenterCards tmp = new CenterCards(gold_list,resource_list,gold_deck,resources_deck);
//        this.cards_in_center = tmp;
//    }
//
//    private void distributeThreeCards(Player p){
//
//            List<PlayCard> tmp = new ArrayList<PlayCard>();
//            tmp.add(gold_deck.drawCard());
//            tmp.add(resources_deck.drawCard());
//            tmp.add(resources_deck.drawCard());
//            p.actual_state.setInitialCardsInHand(tmp);
//
//    }
//
//
//    private void distributeTwoGoalsToPlayer(Player p){
//            List<Goal> tmp = new ArrayList<Goal>();
//            tmp.add(goal_deck.drawCard());
//            tmp.add(goal_deck.drawCard());
//            p.actual_state.setInitialGoalCards(tmp);
//
//    }
//    private void initializedCard(Player p){
//        distributeStartingCard(p);
//        initializedCenterCard();
//        distributeThreeCards(p);
//        distributeTwoGoalsToPlayer(p);
//    }
//
//
//    @Test
//    public void main(){
//        DeckCreation creation = new DeckCreation();
//        this.gold_deck = new Deck(creation.getMixGoldDeck());
//        this.resources_deck = new Deck(creation.getMixResourcesDeck());
//        this.starting_cards_deck = new Deck(creation.getMixStartingDeck());
//        this.goal_deck = new DeckGoalCard(creation.getMixGoalDeck());
//
//        Player p1 = new Player(ColorsEnum.BLU, "nome_1",true);
//        Player p2 = new Player(ColorsEnum.BLU, "nome_2",false);
//
//        p1.setDeck(resources_deck,gold_deck,cards_in_center);
//        p2.setDeck(resources_deck,gold_deck,cards_in_center);
//
//        try{assertEquals("NOT_INITIALIZED",p1.actual_state.getNameState());}
//        catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("1° TEST: i Player sono inizialmente sullo stato NOT INITIALIZED, non dovrebbe essere possibile chiamare nessun metodo:");
//
//        try{
//            distributeStartingCard(p1);
//            distributeThreeCards(p1);
//            distributeTwoGoalsToPlayer(p1);
//            p1.actual_state.peachFromCardsInCenter(0);
//            p1.actual_state.peachCardFromGoldDeck();
//            p1.actual_state.peachFromResourcesDeck();
//            p1.actual_state.peachFromCardsInCenter(0);
//            p1.actual_state.selectStartingCard(true);
//            p1.actual_state.selectGoal(0);
//            p1.actual_state.placeCard(0,true,15,15);
//
//         }catch(InvalidStateException e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("2° TEST: cambio lo stato al Player-> vedo se effettivamente lo stato è cambiato:");
//        p1.InitialNextStatePlayer();
//        p2.InitialNextStatePlayer();
//        try{assertEquals("BEGIN",p1.actual_state.getNameState());
//           }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("3° TEST: inserisco le carte al Player:");
//        try{
//            initializedCard(p1);
//            initializedCard(p2);
//        }catch(InvalidStateException e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("3° parte 2: controllo che le carte siano effettivamente inserite:");
//        try{assertEquals(3,p1.getCardsInHand().size());
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//        try{assertEquals(2,p1.getInitial_goal_cards().size());
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//        System.out.println("la carta centrale è: " + p1.getStartingCard().colore.toString());
//
//
//        System.out.println("4° Test: Il Player è nello Stato BEGIN, da questo stato non è possibile chiamare i prossimi metodi");
//        try{
//            p1.actual_state.peachFromCardsInCenter(0);
//            p1.actual_state.peachCardFromGoldDeck();
//            p1.actual_state.peachFromResourcesDeck();
//            p1.actual_state.peachFromCardsInCenter(0);
//            p1.actual_state.selectStartingCard(true);
//            p1.actual_state.selectGoal(0);
//            p1.actual_state.placeCard(0,true,15,15);
//
//        }catch(InvalidStateException e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("5° Test: Cambio stato al player, -> vedo se effettivamente lo stato è cambiato: ");
//        p1.InitialNextStatePlayer();
//        p2.InitialNextStatePlayer();
//        try{assertEquals("CHOOSE_GOAL",p1.actual_state.getNameState());
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("7° Test: Il Player deve selezionare il suo obbiettivo, vedo se lo seleziona bene ");
//        int choose = 0;
//        p1.actual_state.selectGoal(choose);
//        p2.actual_state.selectGoal(choose);
//        try{assertEquals(p1.getInitial_goal_cards().get(choose), p1.getGoalCard());
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//
//
//        System.out.println("8 parte 2° Test: Il Player deve selezionare il suo obbiettivo, vedo se lo seleziona bene ");
//        choose = 1;
//        p1.actual_state.selectGoal(choose);
//        try{assertEquals(p1.getInitial_goal_cards().get(choose), p1.getGoalCard());
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//
///*
//        System.out.println("8 parte 3° Test: se il Player inserisce un Index non valido deve essere lanciata un'eccezione (bound right):  ");
//        choose = 2;
//        try{
//        p1.actual_state.selectGoal(choose);
//        }catch(IndexOutOfBoundsException e){
//            System.out.println(e.getMessage());
//        }
//
//        */
//
//        /*
//        System.out.println("8 parte 4° Test: se il Player inserisce un Index non valido deve essere lanciata un'eccezione (bound left):  ");
//        choose = -1;
//        try{
//            p1.actual_state.selectGoal(choose);
//        }catch(InvalidBoundException e){
//            System.out.println(e.getMessage());
//        }
//
//         */
//
//        System.out.println("9° test : cambio lo stato del Player:  ");
//        p1.InitialNextStatePlayer();
//        p2.InitialNextStatePlayer();
//        try{assertEquals("CHOOSE_SIDE_FIRST_CARD",p1.actual_state.getNameState());
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//
//
//
//        System.out.println("10° test : il Player mette la prima carta Starting:  ");
//        p1.actual_state.selectStartingCard(true);
//        p1.InitialNextStatePlayer();
//        p2.actual_state.selectStartingCard(true);
//        p2.InitialNextStatePlayer();
//
//        System.out.println("11° test parte 1: Quando il player P1 mette questa carta il suo stato deve essere = Place Card  ");
//        try{assertEquals(p1.actual_state.getNameState(), "PLACE_CARD");
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("11° test parte 2: Quando il player P2 mette questa carta il suo stato deve essere = Wait  ");
//        try{assertEquals(p1.actual_state.getNameState(), "PLACE_CARD");
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("12°: PlaceCard  ");
//        try{assertEquals(3,p1.getCardsInHand().size());
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//        p1.actual_state.placeCard(0,true,1,1);
//        System.out.println("controllo che la carta si sia tolta dal deck in mano");
//        try{assertEquals(p1.tc,p1.getCardsInHand().get(0));
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
///*
//        System.out.println("12 parte 1°: PlaceCard Bound Exception (left)  ");
//        try{p1.actual_state.placeCard(-1,true,1,1);}
//        catch (InvalidBoundException e){
//            System.out.println(e.getMessage());
//        }
//
//
// */
//        /*
//
//        System.out.println("12 parte 2°: PlaceCard Bound Exception (right)  ");
//        try{p1.actual_state.placeCard(3,true,1,1);}
//        catch (InvalidBoundException e){
//            System.out.println(e.getMessage());
//        }
//
//         */
//
//        System.out.println("13 : PLACE_CARD -> DRAW_CARD ");
//        p1.nextStatePlayer();
//        try{assertEquals("DRAW_CARD",p1.actual_state.getNameState());
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//
//
//        /*
//        System.out.println("14 DrawCard: Gold-deck");
//        PlayCard gold_deck_upper_card = gold_deck.seeFirstCard();
//        p1.actual_state.peachCardFromGoldDeck();
//        try{assertEquals(gold_deck_upper_card,p1.getCardsInHand().get(0));
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//        */
//        System.out.println("14 DrawCard: Resources-deck");
//        PlayCard resources_deck_upper_card = resources_deck.seeFirstCard();
//        p1.actual_state.peachFromResourcesDeck();
//        try{assertEquals(resources_deck_upper_card,p1.getCardsInHand().get(0));
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//
//
//        System.out.println("15: una volta che il player pesca la carta gli stati del player si devono aggiornare correttamente:");
//        p1.nextStatePlayer();
//        p2.nextStatePlayer();
//        try{assertEquals("WAIT_TURN",p1.actual_state.getNameState());
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//
//        try{assertEquals("PLACE_CARD",p2.actual_state.getNameState());
//        }catch(AssertionError e){
//            System.out.println(e.getMessage());
//        }
//
//
//
//    }
//}