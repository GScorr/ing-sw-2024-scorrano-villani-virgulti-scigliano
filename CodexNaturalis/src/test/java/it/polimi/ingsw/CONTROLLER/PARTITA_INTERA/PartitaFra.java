package it.polimi.ingsw.CONTROLLER.PARTITA_INTERA;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MODEL.Player.State.PlaceCard;
import org.junit.jupiter.api.Test;

/*
si può togliere
 */


public class PartitaFra {
    GameController controller = new GameController(2);
    private String nome_1 = "Francesco";
    private String nome_2 = "Mirko";

    @Test
    void main(){
        Player p1 = controller.createPlayer(nome_1,true);

        Player p2 = controller.createPlayer(nome_2, false);

        if(controller.checkNumPlayer()){
            System.out.println("Partita iniziata\n");
        }

        System.out.println("p1 Card\n");
        for (PlayCard c : p1.getCardsInHand()){
            System.out.println(c.getSide().getAngleRightDown().name());
            System.out.println(c.getSide().getAngleRightUp().name());
            System.out.println(c.getSide().getAngleLeftDown().name());
            System.out.println(c.getSide().getAngleLeftUp().name());

            System.out.println(c.getSide().getCentral_resource().name());
        }

        System.out.println("p2 Card\n");
        for (PlayCard c : p1.getCardsInHand()){
            System.out.println(c.getSide().getAngleRightDown().name());
            System.out.println(c.getSide().getAngleRightUp().name());
            System.out.println(c.getSide().getAngleLeftDown().name());
            System.out.println(c.getSide().getAngleLeftUp().name());
            System.out.println(c.getSide().getCentral_resource().name());
        }

        controller.playerChooseGoal(p1,1);
        controller.playerChooseGoal(p2,1);

        controller.playerSelectStartingCard(p1,false);
        controller.playerSelectStartingCard(p2,false);

        controller.statePlaceCard(p1,2,22,23);
        controller.playerPeachCardFromGoldDeck(p1);

        controller.statePlaceCard(p2,2,22,23);
        controller.playerPeachCardFromResourcesDeck(p2);

        System.out.println(p1.getResources_deck().cards.size());
        System.out.println(p2.getGold_deck().cards.size());

        p1.setPlayer_points(20);

    }

}
