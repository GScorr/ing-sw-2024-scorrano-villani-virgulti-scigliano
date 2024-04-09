package it.polimi.ingsw.MODEL.Game;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.Goal.Goal;
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
    /*void initializedGame() {
        game.insertPlayer(player1);
        game.insertPlayer(player2);
        game.insertPlayer(player3);
        game.distributeStartingCard();
        System.out.println(game.getGet_player_index().get(0).getStartingCard().getColore());
        System.out.println(game.getGet_player_index().get(1).getStartingCard().getColore());
        System.out.println(game.getGet_player_index().get(2).getStartingCard().getColore());
        //controllo che venga data una carta randomica iniziale differente
        //sto rendendo public temporaneamente i singoli metodi private N.B: questo test non funzionerà in futuro se i metodi rimangono private
        //
        //
        game.initializedCenterCard();
        for(PlayCard c:game.getCars_in_center().getGold_list()){
            System.out.println(c);
        }
        for(PlayCard c:game.getCars_in_center().getResource_list()){
            System.out.println(c);
        }
        //in combo con drawcard testata, la gestione delle carte in mezzo è ottimale
        game.distributeThreeCards();
        for(int i=0;i<3;i++){
            for(PlayCard c:game.getGet_player_index().get(i).getCardsInHand()){
                System.out.println(c + "sono il player " + i);
            }
        }
        game.selectGoals();
        System.out.println(game.getGoal1() + " " + game.getGoal1().getResource());
        System.out.println(game.getGoal2() + " " + game.getGoal2().getResource());
        game.distributeTwoGoalsToPlayer();
        for(int i=0;i<3;i++){
            game.getGet_player_index().get(i).selectGoal(1);
            System.out.println(game.getGet_player_index().get(i).getGoalCard().getGoalType());
            System.out.println(game.getGet_player_index().get(i).getGoalCard().getResource());
            }
    }*/ //tutto commentato perchè i metodi sono stati rimessi a private

    @Test
    void gameNextState() {
    }
}