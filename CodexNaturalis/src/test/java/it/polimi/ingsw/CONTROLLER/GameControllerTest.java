package it.polimi.ingsw.CONTROLLER;

import it.polimi.ingsw.MODEL.Player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    String nomePlayer = "nome_giocatore";
    GameController controller = new GameController(4);

    @Test
    void createPlayer() {
        Player giocatore;
        giocatore = controller.createPlayer("nome_giocatore", true);
        System.out.println("stato del giocatore: "+giocatore.actual_state);
    }

    @Test
    void checkNumPlayer() {
    }

    @Test
    void playerChooseGoal() {
    }

    @Test
    void playerSelectStartingcard() {
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