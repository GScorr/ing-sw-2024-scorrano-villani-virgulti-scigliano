package it.polimi.ingsw.CONTROLLER;

import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.Game.State.GameState;
import it.polimi.ingsw.MODEL.Player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    String nomePlayer = "nome_giocatore";
    GameController controller = new GameController(4);

    //Game gioco = new Game(4);


    @Test
    void createPlayer() {
        Player giocatore1, giocatore2, giocatore3, giocatore4;
        giocatore1 = controller.createPlayer("nome_giocatore", true);
        //System.out.println("stato del giocatore: "+ giocatore.getPlayerState()); //il player parte con lo stato non inizializzato
        giocatore2 = controller.createPlayer("nome_giocatore2", false); //cosa succede se istanzio due giocatori con isFirst=1
        giocatore3 = controller.createPlayer("nome_giocatore2", false);
        giocatore4 = controller.createPlayer("nome_giocatore2", false);


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
        GameState stato_del_gioco1, stato_del_gioco2;
        boolean check = controller.checkNumPlayer(); //forse posso evitare la variabile check ma usare solo la chiamata

        //sto facendo le chiamate alla funzione per tutti i player
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            controller.playerChooseGoal(gioco.getGet_player_index().get(i), 1); //i indica il goal tra 1 e 2
            System.err.println(gioco.getGet_player_index().get(i).getGoalCard().getResource()+" "+gioco.getGet_player_index().get(i).getGoalCard().getGoalType()); //chech che esista una carta

        }

        //forse controllo anche che i vecchi choosed goal siano diversi non posso fare il check sui goal pre chiamata a funziona
        //perche sono tutti a null
        for(int i=0; i<controller.getGame().getNum_player(); i++){
            assertTrue(controller.getChoosed_goal().get(gioco.getGet_player_index().get(i)));
        }


    }

    @Test
    void playerSelectStartingCard() {
    }

    @Test
    void playerPlaceCard() {
    }

    @Test
    void playerPeachCardFromGoldDeck() {
    }

    @Test
    void playerPeachCardFromResourcesDeck() {
    }

    @Test
    void playerPeachFromCardsInCenter() {
    }

    @Test
    void registerObserver() {
    }

    @Test
    void removeObserver() {
    }

    @Test
    void notifyObservers() {
    }
}