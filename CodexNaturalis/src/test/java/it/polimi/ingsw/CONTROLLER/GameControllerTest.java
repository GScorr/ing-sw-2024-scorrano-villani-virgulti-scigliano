package it.polimi.ingsw.CONTROLLER;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Game.State.GameState;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MODEL.Player.State.PState;
import it.polimi.ingsw.MODEL.Player.State.PlaceCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    String nomePlayer = "nome_giocatore";
    GameController controller = new GameController(4);

    //Game gioco = new Game(4);

@Test
void createErrorPlayer(){
    try{
        GameController controller_errore = new GameController(5);
    }catch(ControllerException e){
        System.out.println(e.getId() + " " + e.getMessage());
    }
}
    @Test
    void createPlayer() {

        Player giocatore1, giocatore2, giocatore3, giocatore4;
        giocatore1 = controller.createPlayer("nome_giocatore", true);
        //System.out.println("stato del giocatore: "+ giocatore.getPlayerState()); //il player parte con lo stato non inizializzato
        giocatore2 = controller.createPlayer("nome_giocatore2", false); //cosa succede se istanzio due giocatori con isFirst=1
        giocatore3 = controller.createPlayer("nome_giocatore3", false);
        giocatore4 = controller.createPlayer("nome_giocatore4", false);


    }

    @Test
    void checkNumPlayer() {

        createPlayer();
        Game gioco = controller.getGame(); //ho il game istanziato da gamecontroller
        GameState stato_del_gioco1, stato_del_gioco2;

        List<String> statoPre = new ArrayList<>();
        List<String> statoPost = new ArrayList<>();

        stato_del_gioco1 = gioco.getActual_state();
        for(int i = 0; i<controller.getGame().getNum_player(); i++){
            statoPre.add(gioco.getGet_player_index().get(i).actual_state.getNameState());
            System.err.println(gioco.getGet_player_index().get(i).actual_state.getNameState());
        }



        boolean check = controller.checkNumPlayer();
        //System.out.println(check);
        stato_del_gioco2 =  gioco.getActual_state();
        //il cambio di stato del game funziona
        //System.err.println(stato_del_gioco1.getNameState());
        //System.err.println(stato_del_gioco2.getNameState());
        for(int i = 0; i<controller.getGame().getNum_player(); i++){
            statoPost.add(gioco.getGet_player_index().get(i).actual_state.getNameState());
            System.err.println(gioco.getGet_player_index().get(i).actual_state.getNameState());
        }


        assertNotEquals(stato_del_gioco1, stato_del_gioco2, "not success");
        assertTrue(check);
        assertNotEquals(statoPre, statoPost, "errore");

    }

    @Test
    void playerChooseGoal() {

        createPlayer();
        Game gioco = controller.getGame(); //ho il game istanziato da gamecontroller
        boolean check = controller.checkNumPlayer(); //forse posso evitare la variabile check ma usare solo la chiamata

        //sto facendo le chiamate alla funzione per tutti i player
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerChooseGoal(gioco.getGet_player_index().get(i), 1); //i indica il goal tra 1 e 2
            System.err.println(gioco.getGet_player_index().get(i).getGoalCard().getResource()+" "+gioco.getGet_player_index().get(i).getGoalCard().getGoalType()); //check che esista una carta

        }

        //forse controllo anche che i vecchi choosed goal siano diversi non posso fare il check sui goal pre chiamata a funziona
        //perche sono tutti a null
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            assertTrue(controller.getChoosed_goal().get(gioco.getGet_player_index().get(i)));
        }


    }


    @Test
    void playerSelectStartingCard() {

        createPlayer();
        Game gioco = controller.getGame(); //ho il game istanziato da gamecontroller
        boolean check = controller.checkNumPlayer(); //forse posso evitare la variabile check ma usare solo la chiamata
        /*
        setto lo stato del palyer
         */
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            gioco.getGet_player_index().get(i).setPlayer_state(gioco.getGet_player_index().get(i).choose_side_first_card);
        }
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            System.err.println(gioco.getGet_player_index().get(i).actual_state.getNameState());
        }
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerSelectStartingCard(gioco.getGet_player_index().get(i), false);
        }
        /*
        check dello stato dopo la chiamata e che venga impostata una first card
         */
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            System.err.println(gioco.getGet_player_index().get(i).getStartingCard().getSide().getAngleRightDown());
            System.err.println(gioco.getGet_player_index().get(i).actual_state.getNameState());
        }
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            assertNotEquals(gioco.getGet_player_index().get(i).getStartingCard(), null);
        }

    }

    @Test
    void statePlaceCard() {

        createPlayer();

        if(controller.checkNumPlayer()){
            System.out.println("gioco inizializzato");
        }else{System.out.println("gioco non inizializzato");}


        Game gioco = controller.getGame(); //ho il game istanziato da gamecontroller


        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerChooseGoal(gioco.getGet_player_index().get(i), 1); //i indica il goal tra 1 e 2

        }
        /*
        devo arrivare a poter piazzare le carte
         */

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerSelectStartingCard(gioco.getGet_player_index().get(i), false);
        }
        /*

         */
        Player p1 = gioco.getGet_player_index().get(0);
        Player p2 = gioco.getGet_player_index().get(1);
        Player p3 = gioco.getGet_player_index().get(2);
        Player p4 = gioco.getGet_player_index().get(3);
        System.out.println(p1.actual_state.getNameState());
        System.out.println(p2.actual_state.getNameState());
        System.out.println(p3.actual_state.getNameState());
        System.out.println(p4.actual_state.getNameState());

        assertEquals(p1.getGameField().getCell(22,22,45).getCard(),p1.getGameField().getCell(23,23,45).getCard());

        System.out.println(p1.getCardsInHand());

        /*
        test sull'errore
         */
        //controller.statePlaceCard(p1, 4, false, 23, 23);

        controller.statePlaceCard(gioco.getGet_player_index().get(0), 1, 23, 23);
        //assertEquals(p1.getGameField().getCell(22,22,45).getCard(),p1.getGameField().getCell(23,23,45).getCard());
        System.out.println("stato di p1 "+p1.actual_state.getNameState());
        System.out.println("stato di p2 "+p2.actual_state.getNameState());

        System.out.println(p1.getCardsInHand());

        assertEquals(p1.getCardsInHand().size(), 3); //perche al posto della carta vecchia c'è la tc

        controller.playerPeachCardFromGoldDeck(p1); //devo pescare dopo aver giocato una carta altrimenti non cambia lo stato dei giocatori successivi che rimangono in wait
        System.out.println("p1: " + p1.actual_state.getNameState());
        System.out.println("p2: " + p2.actual_state.getNameState());

    }

    @Test
    void EndGame(){

        /*
        test fine game con p1.PlayerPoints!=20
         */

        Game gioco = controller.getGame();
        createPlayer();

        Player p1 = gioco.getGet_player_index().get(0);
        Player p2 = gioco.getGet_player_index().get(1);
        Player p3 = gioco.getGet_player_index().get(2);
        Player p4 = gioco.getGet_player_index().get(3);

        System.out.println("1 "+gioco.getActual_state().getNameState());

        if(controller.checkNumPlayer()){
            //System.out.println("gioco inizializzato");
        }else{
            //System.out.println("gioco non inizializzato");
        }

        System.out.println("2 "+gioco.getActual_state().getNameState());

        for(int i=0; i<controller.getGame().getNum_player(); i++){

            controller.playerChooseGoal(gioco.getGet_player_index().get(i), 1); //i indica il goal tra 1 e 2
            try{
                controller.selectSideCard(gioco.getGet_player_index().get(i),2,true);

            }catch (ControllerException e){
                System.out.println(e.getId() + e.getMessage());
            }
        }

        System.out.println("3 "+gioco.getActual_state().getNameState());

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerSelectStartingCard(gioco.getGet_player_index().get(i), false);
            try{
                controller.selectSideCard(gioco.getGet_player_index().get(i),2,true);

            }catch (ControllerException e){
                System.out.println(e.getId() + e.getMessage());
            }
        }

        System.out.println("4 "+gioco.getActual_state().getNameState());


        System.out.println(p1.actual_state.getNameState());
        System.out.println(p2.actual_state.getNameState());
        System.out.println(p3.actual_state.getNameState());
        System.out.println(p4.actual_state.getNameState());

        p1.setPlayer_points(15);
        p2.setPlayer_points(20);
        p3.setPlayer_points(15);
        p4.setPlayer_points(15);

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            try{
                controller.selectSideCard(gioco.getGet_player_index().get(i),2,true);

            }catch (ControllerException e){
                System.out.println(e.getId() + e.getMessage());
            }
        }

        controller.statePlaceCard(p1, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p1);

        controller.statePlaceCard(p2, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p2);

        controller.statePlaceCard(p3, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p3);

        controller.statePlaceCard(p4, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p4);

        System.out.println("5" + " " + gioco.getActual_state().getNameState());


        System.out.println( controller.tmp_final_state + " " + controller.is_final_state);
        System.out.println("stampo il counter: " + controller.get_final_counter());


        controller.statePlaceCard(p1, 1, 24,24);
        controller.playerPeachCardFromResourcesDeck(p1);
        System.out.println( controller.tmp_final_state + " " + controller.is_final_state);

        controller.statePlaceCard(p2, 1, 24, 24);
        controller.playerPeachCardFromResourcesDeck(p2);

        controller.statePlaceCard(p3, 1, 24, 24);
        controller.playerPeachCardFromResourcesDeck(p3);

        controller.statePlaceCard(p4, 1, 24, 24);
        controller.playerPeachCardFromResourcesDeck(p4);


        System.out.println("stampo il counter: " + controller.get_final_counter());


        System.out.println("6 "+gioco.getActual_state().getNameState());

    }


    @Test
    void EndGame2(){

        Game gioco = controller.getGame();
        createPlayer();

        Player p1 = gioco.getGet_player_index().get(0);
        Player p2 = gioco.getGet_player_index().get(1);
        Player p3 = gioco.getGet_player_index().get(2);
        Player p4 = gioco.getGet_player_index().get(3);

        System.out.println("1 "+gioco.getActual_state().getNameState());

        if(controller.checkNumPlayer()){
            //System.out.println("gioco inizializzato");
        }else{
            //System.out.println("gioco non inizializzato");
        }

        System.out.println("2 "+gioco.getActual_state().getNameState());

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerChooseGoal(gioco.getGet_player_index().get(i), 1); //i indica il goal tra 1 e 2

        }

        System.out.println("3 "+gioco.getActual_state().getNameState());

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerSelectStartingCard(gioco.getGet_player_index().get(i), false);
        }

        System.out.println("4 "+gioco.getActual_state().getNameState());


        System.out.println(p1.actual_state.getNameState());
        System.out.println(p2.actual_state.getNameState());
        System.out.println(p3.actual_state.getNameState());
        System.out.println(p4.actual_state.getNameState());

        p1.setPlayer_points(20);
        p2.setPlayer_points(15);
        p3.setPlayer_points(15);
        p4.setPlayer_points(15);


        System.out.println( controller.tmp_final_state + " " + controller.is_final_state);

        controller.statePlaceCard(p1, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p1);

        System.out.println( controller.tmp_final_state + " " + controller.is_final_state);

        controller.statePlaceCard(p2, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p2);

        controller.statePlaceCard(p3, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p3);

        controller.statePlaceCard(p4, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p4);

        System.out.println("5" + " " + gioco.getActual_state().getNameState());

        try {
            controller.statePlaceCard(p1, 0, 23, 24);
        }catch (ControllerException e){
            System.out.println(e.getId() + " " + e.getMessage());
        }

    }

    @Test
    void checkDeckPlayer(){

        Game gioco = controller.getGame();
        createPlayer();

        Player p1 = gioco.getGet_player_index().get(0);
        Player p2 = gioco.getGet_player_index().get(1);
        Player p3 = gioco.getGet_player_index().get(2);
        Player p4 = gioco.getGet_player_index().get(3);

        System.out.println("1"+gioco.getActual_state().getNameState());

        if(controller.checkNumPlayer()){
            //System.out.println("gioco inizializzato");
        }else{
            //System.out.println("gioco non inizializzato");
        }

        System.out.println("2"+gioco.getActual_state().getNameState());

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerChooseGoal(gioco.getGet_player_index().get(i), 1); //i indica il goal tra 1 e 2

        }

        System.out.println("3"+gioco.getActual_state().getNameState());

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerSelectStartingCard(gioco.getGet_player_index().get(i), false);
        }

        System.out.println("4"+gioco.getActual_state().getNameState());


        System.out.println(p1.actual_state.getNameState());
        System.out.println(p2.actual_state.getNameState());
        System.out.println(p3.actual_state.getNameState());
        System.out.println(p4.actual_state.getNameState());

        p1.setPlayer_points(20);
        p2.setPlayer_points(15);
        p3.setPlayer_points(15);
        p4.setPlayer_points(15);



        for(int i=0; i<controller.getGame().getNum_player(); i++){
            System.out.println(gioco.getGet_player_index().get(i).getPlayerPoints());
        }

        int size__goldDeck_before = p1.getGold_deck().cards.size();
        int size__resourcesDeck_before = p2.getResources_deck().cards.size();

        controller.statePlaceCard(p1, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p1);


        controller.statePlaceCard(p2, 1, 23, 23);
        controller.playerPeachCardFromGoldDeck(p2);

        assertEquals(p1.getGold_deck().cards.size(), size__goldDeck_before -1 );
        assertEquals(p2.getResources_deck().cards.size(), size__resourcesDeck_before -1 );



    }

    @Test
    void deckTerminated(){

        Game gioco = controller.getGame();
        createPlayer();

        Player p1 = gioco.getGet_player_index().get(0);
        Player p2 = gioco.getGet_player_index().get(1);
        Player p3 = gioco.getGet_player_index().get(2);
        Player p4 = gioco.getGet_player_index().get(3);

        System.out.println("1"+ " " + gioco.getActual_state().getNameState());

        if(controller.checkNumPlayer()){
            //System.out.println("gioco inizializzato");
        }else{
            //System.out.println("gioco non inizializzato");
        }

        System.out.println("2"+ " " +gioco.getActual_state().getNameState());

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerChooseGoal(gioco.getGet_player_index().get(i), 1); //i indica il goal tra 1 e 2

        }

        System.out.println("3"+ " " +gioco.getActual_state().getNameState());

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerSelectStartingCard(gioco.getGet_player_index().get(i), false);
        }

        System.out.println("4"+ " " +gioco.getActual_state().getNameState());

        controller.statePlaceCard(p1, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p1);

        controller.statePlaceCard(p2, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p2);

        controller.statePlaceCard(p3, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p3);

        controller.statePlaceCard(p4, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p4);

        int initial_goldDeck_size = controller.getGame().getGold_deck().cards.size() - 1;
        //empty GoldDeck
        for(int i = 0; i < initial_goldDeck_size; i++ ) {
            controller.getGame().getGold_deck().drawCard();
        }



        controller.statePlaceCard(p1, 1, 24, 24);
        try{controller.playerPeachCardFromGoldDeck(p1);}
        catch (ControllerException e){
            System.out.println(e.getId() + " " + e.getMessage());
        }

        controller.statePlaceCard(p2, 1, 24, 24);
        try{controller.playerPeachCardFromGoldDeck(p2);}
        catch (ControllerException e){
            System.out.println("515 " + e.getId() + " " + e.getMessage());
        }
        try{controller.playerPeachCardFromResourcesDeck(p2);}
        catch (ControllerException e){
            System.out.println("519 " + e.getId() + " " + e.getMessage());
        }

        //empty Resources Deck
        int initial_resourcesDeck_size = controller.getGame().getResources_deck().cards.size() - 1;
        for(int i = 0; i < initial_resourcesDeck_size ; i++ ) {
            controller.getGame().getResources_deck().drawCard();
        }


        controller.statePlaceCard(p3, 1, 24, 24);
        try{controller.playerPeachCardFromResourcesDeck(p3);}
        catch (ControllerException e){
            System.out.println("532 "+ e.getId() + " " + e.getMessage());
        }

        System.out.println(controller.both_deck_finished + " " + controller.tmp_final_state + " " + controller.is_final_state);


        controller.statePlaceCard(p4, 1, 24, 24);

        try{controller.playerPeachCardFromResourcesDeck(p4);}
        catch (ControllerException e){
            System.out.println("542 " + e.getId() + " " + e.getMessage());
        }



        controller.statePlaceCard(p1,2,21,23);
        System.out.println(controller.both_deck_finished + " " + controller.tmp_final_state + " " + controller.is_final_state);
        controller.statePlaceCard(p2,2,21,23);
        controller.statePlaceCard(p3,2,21,23);
        controller.statePlaceCard(p4,2,21,23);
        System.out.println(controller.end__game);




        System.out.println("5"+ " " +gioco.getActual_state().getNameState());


    }
    @Test
    void deckTerminated2(){

        Game gioco = controller.getGame();
        createPlayer();

        Player p1 = gioco.getGet_player_index().get(0);
        Player p2 = gioco.getGet_player_index().get(1);
        Player p3 = gioco.getGet_player_index().get(2);
        Player p4 = gioco.getGet_player_index().get(3);

        System.out.println("1"+ " " + gioco.getActual_state().getNameState());

        if(controller.checkNumPlayer()){
            //System.out.println("gioco inizializzato");
        }else{
            //System.out.println("gioco non inizializzato");
        }

        System.out.println("2"+ " " +gioco.getActual_state().getNameState());

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerChooseGoal(gioco.getGet_player_index().get(i), 1); //i indica il goal tra 1 e 2

        }

        System.out.println("3"+ " " +gioco.getActual_state().getNameState());

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerSelectStartingCard(gioco.getGet_player_index().get(i), false);
        }

        System.out.println("4"+ " " +gioco.getActual_state().getNameState());

        controller.statePlaceCard(p1, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p1);

        controller.statePlaceCard(p2, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p2);

        controller.statePlaceCard(p3, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p3);

        controller.statePlaceCard(p4, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p4);

        //empty Resources Deck
        int initial_resourcesDeck_size = controller.getGame().getResources_deck().cards.size() - 1;
        for(int i = 0; i < initial_resourcesDeck_size ; i++ ) {
            controller.getGame().getResources_deck().drawCard();
        }


        controller.statePlaceCard(p1, 1, 24, 24);
        try{controller.playerPeachCardFromResourcesDeck(p1);}
        catch (ControllerException e){
            System.out.println(e.getId() + " " + e.getMessage());
        }

        controller.statePlaceCard(p2, 1, 24, 24);
        try{controller.playerPeachCardFromResourcesDeck(p2);}
        catch (ControllerException e){
            System.out.println("515 " + e.getId() + " " + e.getMessage());
        }
        try{controller.playerPeachCardFromGoldDeck(p2);}
        catch (ControllerException e){
            System.out.println("519 " + e.getId() + " " + e.getMessage());
        }

        //empty GoldDeck
        int initial_goldDeck_size = controller.getGame().getGold_deck().cards.size() - 1;
        for(int i = 0; i < initial_goldDeck_size; i++ ) {
            controller.getGame().getGold_deck().drawCard();
        }


        controller.statePlaceCard(p3, 1, 24, 24);
        try{controller.playerPeachCardFromGoldDeck(p3);}
        catch (ControllerException e){
            System.out.println("532 "+ e.getId() + " " + e.getMessage());
        }

        System.out.println(controller.both_deck_finished + " " + controller.tmp_final_state + " " + controller.is_final_state);


        controller.statePlaceCard(p4, 1, 24, 24);

        try{controller.playerPeachCardFromResourcesDeck(p4);}
        catch (ControllerException e){
            System.out.println("542 " + e.getId() + " " + e.getMessage());
        }



        controller.statePlaceCard(p1,2,21,23);
        System.out.println(controller.both_deck_finished + " " + controller.tmp_final_state + " " + controller.is_final_state);
        controller.statePlaceCard(p2,2,21,23);
        controller.statePlaceCard(p3,2,21,23);
        controller.statePlaceCard(p4,2,21,23);
        System.out.println(controller.end__game);




        System.out.println("5"+ " " +gioco.getActual_state().getNameState());


    }
    @Test

    void deckTerminated3(){

        Game gioco = controller.getGame();
        createPlayer();

        Player p1 = gioco.getGet_player_index().get(0);
        Player p2 = gioco.getGet_player_index().get(1);
        Player p3 = gioco.getGet_player_index().get(2);
        Player p4 = gioco.getGet_player_index().get(3);

        System.out.println("1"+ " " + gioco.getActual_state().getNameState());

        if(controller.checkNumPlayer()){
            //System.out.println("gioco inizializzato");
        }else{
            //System.out.println("gioco non inizializzato");
        }

        System.out.println("2"+ " " +gioco.getActual_state().getNameState());

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerChooseGoal(gioco.getGet_player_index().get(i), 1); //i indica il goal tra 1 e 2

        }

        System.out.println("3"+ " " +gioco.getActual_state().getNameState());

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerSelectStartingCard(gioco.getGet_player_index().get(i), false);
        }

        System.out.println("4"+ " " +gioco.getActual_state().getNameState());

        controller.statePlaceCard(p1, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p1);

        controller.statePlaceCard(p2, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p2);

        controller.statePlaceCard(p3, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p3);

        controller.statePlaceCard(p4, 1, 23, 23);
        controller.playerPeachCardFromResourcesDeck(p4);

        //empty Resources Deck
        int initial_resourcesDeck_size = controller.getGame().getResources_deck().cards.size() - 1;
        for(int i = 0; i < initial_resourcesDeck_size ; i++ ) {
            controller.getGame().getResources_deck().drawCard();
        }


        controller.statePlaceCard(p1, 1, 24, 24);
        try{controller.playerPeachFromCardsInCenter(p1,2);}
        catch (ControllerException e){
            System.out.println(e.getId() + " " + e.getMessage());
        }

        controller.statePlaceCard(p2, 1, 24, 24);
        try{controller.playerPeachCardFromResourcesDeck(p2);}
        catch (ControllerException e){
            System.out.println("515 " + e.getId() + " " + e.getMessage());
        }
        try{controller.playerPeachCardFromGoldDeck(p2);}
        catch (ControllerException e){
            System.out.println("519 " + e.getId() + " " + e.getMessage());
        }

        //empty GoldDeck
        int initial_goldDeck_size = controller.getGame().getGold_deck().cards.size() - 1;
        for(int i = 0; i < initial_goldDeck_size; i++ ) {
            controller.getGame().getGold_deck().drawCard();
        }


        controller.statePlaceCard(p3, 1, 24, 24);
        try{controller.playerPeachFromCardsInCenter(p3,0);}
        catch (ControllerException e){
            System.out.println("532 "+ e.getId() + " " + e.getMessage());
        }


        System.out.println(controller.both_deck_finished + " " + controller.tmp_final_state + " " + controller.is_final_state);


        controller.statePlaceCard(p4, 1, 24, 24);

        try{controller.playerPeachCardFromResourcesDeck(p4);}
        catch (ControllerException e){
            System.out.println("542 " + e.getId() + " " + e.getMessage());
        }



        controller.statePlaceCard(p1,2,21,23);
        System.out.println(controller.both_deck_finished + " " + controller.tmp_final_state + " " + controller.is_final_state);
        controller.statePlaceCard(p2,2,21,23);
        controller.statePlaceCard(p3,2,21,23);
        controller.statePlaceCard(p4,2,21,23);
        System.out.println(controller.end__game);




        System.out.println("5"+ " " +gioco.getActual_state().getNameState());


    }


    @Test
    void playerPeachCardFromGoldDeck() {
        createPlayer();
        Game gioco = controller.getGame(); //ho il game istanziato da gamecontroller
       // boolean check = controller.checkNumPlayer(); //forse posso evitare la variabile check ma usare solo la chiamata
        List<PState> stato = new ArrayList<>();
        PlayCard carta_da_pescare;

        // NOT INITIALIZED => BEGIN => CHOOSE_GOAL
        controller.checkNumPlayer(); //inizializzo il gioco

        Player p1 = gioco.getGet_player_index().get(0);
        Player p2 = gioco.getGet_player_index().get(1);
        Player p3 = gioco.getGet_player_index().get(2);
        Player p4 = gioco.getGet_player_index().get(3);


        /*
        forse dobbiamo aggiungere che quando restituisce gli errori di posizionamento o di pescaggio si chiede di rieffetuare la scelta
         */

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerChooseGoal(gioco.getGet_player_index().get(i), 1); //se metto un index maggiore di 2 da correttamente errore
        }

        // CHOOSE_GOAL => STARTING_CARD

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerSelectStartingCard(gioco.getGet_player_index().get(i), false);
        }
/*
test sull'errore
 */
       // controller.playerPeachCardFromGoldDeck(p1);


        System.out.println("supero il select starting card");

        // STARTING_CARD => FIRST_PLAYER => PLACE     (OTHER => WAIT)
        carta_da_pescare = gioco.getResources_deck().seeFirstCard();
        System.out.println();
        System.out.println("stampo la mano di p1 prima il peach");
        carta_da_pescare = gioco.getGold_deck().seeFirstCard();
        System.out.println(p1.getCardsInHand());
        controller.statePlaceCard(gioco.getGet_player_index().get(0), 1, 23, 23);
        controller.playerPeachCardFromGoldDeck(gioco.getGet_player_index().get(0));
        System.out.println("stampo la mano di p1 dopo il peach");
        System.out.println(p1.getCardsInHand());
        System.out.println();
        assertEquals(carta_da_pescare, p1.getCardsInHand().get(1));


        carta_da_pescare = gioco.getGold_deck().seeFirstCard();
        controller.statePlaceCard(gioco.getGet_player_index().get(1), 1, 23, 23);
        controller.playerPeachCardFromGoldDeck(gioco.getGet_player_index().get(1));
        assertEquals(carta_da_pescare, p2.getCardsInHand().get(1));

        carta_da_pescare = gioco.getGold_deck().seeFirstCard();
        controller.statePlaceCard(gioco.getGet_player_index().get(2), 1, 23, 23);
        controller.playerPeachCardFromGoldDeck(gioco.getGet_player_index().get(2));
        assertEquals(carta_da_pescare, p3.getCardsInHand().get(1));

        carta_da_pescare = gioco.getGold_deck().seeFirstCard();
        controller.statePlaceCard(gioco.getGet_player_index().get(3), 1, 23, 23);
        controller.playerPeachCardFromGoldDeck(gioco.getGet_player_index().get(3));
        assertEquals(carta_da_pescare, p4.getCardsInHand().get(1));


        for(int i=0; i<controller.getGame().getNum_player(); i++){
            System.err.println(gioco.getGet_player_index().get(i).actual_state.getNameState());
        }


    }

    @Test
    void playerPeachCardFromResourcesDeck() {
        createPlayer();
        Game gioco = controller.getGame(); //ho il game istanziato da gamecontroller
        boolean check = controller.checkNumPlayer(); //forse posso evitare la variabile check ma usare solo la chiamata
        List<PState> stato = new ArrayList<>();
        PlayCard carta_da_pescare;
/*
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            gioco.getGet_player_index().get(i).setPlayer_state(gioco.getGet_player_index().get(i).draw_card);
            System.err.println(gioco.getGet_player_index().get(i).actual_state.getNameState());
        }
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerPeachCardFromResourcesDeck(gioco.getGet_player_index().get(i));
        }

 */
        controller.checkNumPlayer();
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerChooseGoal(gioco.getGet_player_index().get(i), 1);
        }

        // CHOOSE_GOAL => STARTING_CARD

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerSelectStartingCard(gioco.getGet_player_index().get(i), false);
        }

        /*
        controllo sull'errore
         */

      //  controller.playerPeachCardFromResourcesDeck(gioco.getGet_player_index().get(0));

        //System.out.println(gioco.getGet_player_index().get(0).getCardsInHand());
        //System.out.println(gioco.getResources_deck().seeFirstCard());
        // STARTING_CARD => FIRST_PLAYER => PLACE     (OTHER => WAIT)
        controller.statePlaceCard(gioco.getGet_player_index().get(0), 1, 23, 23);
        carta_da_pescare = gioco.getResources_deck().seeFirstCard();
        //System.out.println(carta_da_pescare);
        controller.playerPeachCardFromResourcesDeck(gioco.getGet_player_index().get(0));
        //System.out.println(gioco.getGet_player_index().get(0).getCardsInHand());
        assertEquals(carta_da_pescare, gioco.getGet_player_index().get(0).getCardsInHand().get(1), "errore player 1");

        controller.statePlaceCard(gioco.getGet_player_index().get(1), 1, 23, 23);
        carta_da_pescare = gioco.getResources_deck().seeFirstCard();
        controller.playerPeachCardFromResourcesDeck(gioco.getGet_player_index().get(1));
        assertEquals(carta_da_pescare, gioco.getGet_player_index().get(1).getCardsInHand().get(1), "errore player 2");


        controller.statePlaceCard(gioco.getGet_player_index().get(2), 1, 23, 23);
        carta_da_pescare = gioco.getResources_deck().seeFirstCard();
        controller.playerPeachCardFromResourcesDeck(gioco.getGet_player_index().get(2));
        assertEquals(carta_da_pescare, gioco.getGet_player_index().get(2).getCardsInHand().get(1), "errore player 3");

        controller.statePlaceCard(gioco.getGet_player_index().get(3), 1, 23, 23);
        carta_da_pescare = gioco.getResources_deck().seeFirstCard();
        controller.playerPeachCardFromResourcesDeck(gioco.getGet_player_index().get(3));
        assertEquals(carta_da_pescare, gioco.getGet_player_index().get(3).getCardsInHand().get(1), "errore player 4");

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            System.err.println(gioco.getGet_player_index().get(i).actual_state.getNameState());
        }

    }

    @Test
    void playerPeachFromCardsInCenter() {

        createPlayer();
        Game gioco = controller.getGame(); //ho il game istanziato da gamecontroller
        boolean check = controller.checkNumPlayer(); //forse posso evitare la variabile check ma usare solo la chiamata
        List<PState> stato = new ArrayList<>();
/*
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            gioco.getGet_player_index().get(i).setPlayer_state(gioco.getGet_player_index().get(i).draw_card);
            System.err.println(gioco.getGet_player_index().get(i).actual_state.getNameState());
        }
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerPeachFromCardsInCenter(gioco.getGet_player_index().get(i), 1);
        }

 */
        controller.checkNumPlayer();
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerChooseGoal(gioco.getGet_player_index().get(i), 1);
        }


        // CHOOSE_GOAL => STARTING_CARD

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerSelectStartingCard(gioco.getGet_player_index().get(i), false);
        }

       // System.out.println("1 "+gioco.getGet_player_index().get(0).actual_state.getNameState());

        PlayCard prima_carta;

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            System.err.println(gioco.getGet_player_index().get(i).actual_state.getNameState());
        }

        //System.out.println(gioco.getGet_player_index().get(0).getCardsInHand());

       // controller.playerPeachFromCardsInCenter(gioco.getGet_player_index().get(0), 1);

        // STARTING_CARD => FIRST_PLAYER => PLACE     (OTHER => WAIT)
        prima_carta = gioco.getCars_in_center().getGold_list().get(1);
        controller.statePlaceCard(gioco.getGet_player_index().get(0), 1, 23, 23);
       // System.out.println("2 "+gioco.getGet_player_index().get(0).actual_state.getNameState());
        controller.playerPeachFromCardsInCenter(gioco.getGet_player_index().get(0), 1);

        assertEquals(prima_carta, gioco.getGet_player_index().get(0).getCardsInHand().get(1));


        prima_carta = gioco.getCars_in_center().getGold_list().get(1);
        assertNotEquals(prima_carta,gioco.getGet_player_index().get(0).getCardsInHand().get(1));

        prima_carta = gioco.getCars_in_center().getGold_list().get(1);
        controller.statePlaceCard(gioco.getGet_player_index().get(1), 1, 23, 23);
        controller.playerPeachFromCardsInCenter(gioco.getGet_player_index().get(1), 1);
        assertEquals(prima_carta, gioco.getGet_player_index().get(1).getCardsInHand().get(1));

        prima_carta = gioco.getCars_in_center().getGold_list().get(1);
        controller.statePlaceCard(gioco.getGet_player_index().get(2), 1, 23,23 );
        controller.playerPeachFromCardsInCenter(gioco.getGet_player_index().get(2), 1);
        assertEquals(prima_carta, gioco.getGet_player_index().get(2).getCardsInHand().get(1));

        prima_carta = gioco.getCars_in_center().getGold_list().get(1);
        controller.statePlaceCard(gioco.getGet_player_index().get(3), 1, 23, 23);
        controller.playerPeachFromCardsInCenter(gioco.getGet_player_index().get(3), 1);
        assertEquals(prima_carta, gioco.getGet_player_index().get(3).getCardsInHand().get(1));

        for(int i=0; i<controller.getGame().getNum_player(); i++){
            System.err.println(gioco.getGet_player_index().get(i).actual_state.getNameState());
        }

    }

    @Test
    void registerObserver() {
        /*
        test passato di conseguenza ai cambi di stato che avvengono
         */
    }

    @Test
    void removeObserver() {
        /*
        test passato di conseguenza ai cambi di stato che avvengono
         */
    }

    @Test
    void notifyObservers() {
        /*
        test passato di conseguenza ai cambi di stato che avvengono
         */
    }

    /*
    test gioco
     */
    @Test
    void gioco(){

        Scanner scanner = new Scanner(System.in);

        createPlayer();
        Game gioco = controller.getGame(); //ho il game istanziato da gamecontroller
        controller.checkNumPlayer();
        for(int i=0; i<gioco.getNum_player(); i++){
            System.err.println(gioco.getGet_player_index().get(i).getName());
        }

    }
}