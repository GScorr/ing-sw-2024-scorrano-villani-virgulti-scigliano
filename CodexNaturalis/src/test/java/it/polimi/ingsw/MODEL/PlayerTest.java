package it.polimi.ingsw.MODEL;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/*
    TODO
        chidere a cosa serve card_down in gamefield single cell
        manca playerstate in player
 */
class PlayerTest {
    //devo inizilizzare tutto per fare i test
    Side front = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.ANIMAL, CentralEnum.ANIMAL, CentralEnum.ANIMAL);
    Side back = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.ANIMAL, CentralEnum.ANIMAL, CentralEnum.ANIMAL);
    PlayCard card = new ResourceCard(front, back, false, 1); //inizializzo tutti i booleani a false per comodità
    PlayCard card_down = new ResourceCard(front, back, false, 1);
    GameFieldSingleCell[][] gf = new GameFieldSingleCell[4][4];
    GameField g = new GameField(gf);
    }
    //Player player = new Player(false, ColorsEnum.RED, g);

  /*  @Test
    void getIsFirst() {
        if(!player.getIsFirst()){
            System.err.println("non è primo");
        }
        else System.err.println("è primo");
        assertFalse(player.getIsFirst()); //controlla che la variabile booleana venga passata correttamente

    }

    @Test
    void getColor() {
        if(player.getColor().equals(ColorsEnum.RED)){
            System.err.println("sono uguali");
        }
        else System.err.println("non sono uguali");
        assertEquals(ColorsEnum.RED, player.getColor());
    }

    @Test //per ora manca nella classe player, meglio inseirlo nella classe potrebbe funzionare meglio a livello di gestione
    void getPlayerState() {
        System.err.println(player.getPlayerState());
        assertEquals(PlayerState.NOT_INITIALIZED, player.getPlayerState());
    }
    /*
    getter
     */
/*
tutti i metodi getter non credo serva testarli però, lascio comunque la struttura
    @Test
    void getGameField() {
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
*/
    /*
    @Test
    void setPlayer_state() {
        player.setPlayer_state(PlayerState.NOT_INITIALIZED);
        //.player_state = PlayerState.NOT_INITIALIZED;
        System.err.println(player.getPlayerState());
        assertEquals(PlayerState.NOT_INITIALIZED, player.getPlayerState());
    }
    /*
    setter
     */
/*
    @Test
    void setInitialCardsInHand() {
       // this.c
    }

    @Test
    void setInitialGoalCards() {
    }

    @Test
    void setCardsInHand() {
    }

    @Test
    void setStartingCard() {
    }

    @Test
    void setGoal_card() {
    }
*//*
    @Test
    void nextStatePlayer() {
        player.nextStatePlayer();
        //System.err.println(player.getPlayerState());
        assertEquals(PlayerState.WAIT_TURN, player.getPlayerState());
    }

    @Test
    void placeCard() {
        this.player.setInitialCardsInHand(Stream.of(card, card, card).collect(Collectors.toList()));
        //this.g.
        this.player.setPlayer_state(PlayerState.PLACE_CARD);
        this.player.placeCard(1, false, 0,0);

    }

    @Test
    void addPoints() {
        player.addPoints(2);
        assertEquals(2, player.getPlayerPoints());
    }

    @Test
    void insertCard() {
        this.player.setInitialCardsInHand(Stream.of(card, card, card).collect(Collectors.toList()));
        //System.err.println(card);
        this.player.insertCard(card);
    }

    @Test
    void selectGoal() {
    }

    @Test
    void selectFirstCard() {
    }
}*/