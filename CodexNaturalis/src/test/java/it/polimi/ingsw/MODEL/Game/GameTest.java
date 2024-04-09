package it.polimi.ingsw.MODEL.Game;

import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.Player.Player;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Player player1 = new Player(ColorsEnum.BLU,"a",true);
    Player player2 = new Player(ColorsEnum.RED,"b",false);
    Player player3 = new Player(ColorsEnum.GREEN,"c",false);
    Player player4 = new Player(ColorsEnum.YELLOW,"d",false);
    Game game= new Game(3);
    @Test
    void insertPlayer() {
        game.insertPlayer(player1);
        assertEquals(player1,game.getGet_player_index().get(0));
        game.insertPlayer(player2);
        assertEquals(player2,game.getGet_player_index().get(1));
        game.insertPlayer(player3);
        assertEquals(player3,game.getGet_player_index().get(2));
        assertEquals(3,game.getNum_player());
        try {
            game.insertPlayer(player4);

            fail("Expected LimitNumPlayerException was not thrown");
        } catch (LimitNumPlayerException e) {
            assertEquals("Limite giocatori già raggiunto, non è possibile entrare in questa partita", e.getMessage());
        }

    }

    @Test
    void initializedGame() {
        game.insertPlayer(player1);
        game.insertPlayer(player2);
        game.insertPlayer(player3);
        game.distributeStartingCard();
        assertEquals(game.getGet_player_index().get(0).);
    }

    @Test
    void gameNextState() {
    }
}