package it.polimi.ingsw.PARTITA_INTERA;

import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Player.Player;

public class PartitaFra {
    GameController controller = new GameController(2);
    private String nome_1 = "Francesco";
    private String nome_2 = "Mirko";

   Player p1 = controller.createPlayer(nome_1,true);

   Player p2 = controller.createPlayer(nome_2, false);




}
