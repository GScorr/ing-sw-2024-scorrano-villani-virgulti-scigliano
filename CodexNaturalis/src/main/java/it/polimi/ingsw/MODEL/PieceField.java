package it.polimi.ingsw.MODEL;
import java.util.Map;

import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.Player.Player;

/**
 * Represents the game board for placing worker pieces.
 *
 * This class manages the placement of worker pieces for four players.
 * It keeps track of:
 *  - Player objects and their assigned worker colors
 *  - Mapping between a color and its current position on the board (0-based index)
 *  - Mapping between a player and their assigned worker color
 *  - An array representing the initial worker placement order (clockwise)
 *
 */
public class PieceField {

    Player player1,player2,player3,player4;
    Map<Integer,ColorsEnum[]> get_colors_at_position;
    Map<ColorsEnum,Integer> get_position_of_color;
    Map<Player,ColorsEnum> get_player_color;

    ColorsEnum[] colors = new ColorsEnum[4];

    public PieceField(Player  player1, ColorsEnum p1, Player player2, ColorsEnum p2, Player player3, ColorsEnum p3,Player player4,ColorsEnum p4){
        this.player1=player1;
        this.player2=player2;
        this.player3=player3;
        this.player4=player4;

        get_player_color.put(player1,p1);
        get_player_color.put(player2,p2);
        get_player_color.put(player3,p3);
        get_player_color.put(player4,p4);

        get_position_of_color.put(ColorsEnum.RED,0);
        get_position_of_color.put(ColorsEnum.GREEN,0);
        get_position_of_color.put(ColorsEnum.YELLOW,0);
        get_position_of_color.put(ColorsEnum.BLU,0);

        for (int i=0; i<=31;i++){
            if(i==0){
                colors[0] = ColorsEnum.RED;
                colors[1] = ColorsEnum.GREEN;
                colors[2] = ColorsEnum.YELLOW;
                colors[3] = ColorsEnum.BLU;
                get_colors_at_position.put(i,colors);
            }else {
                ColorsEnum[] tmp = new ColorsEnum[0];
                get_colors_at_position.put(i,tmp);
            }
        }
    }



}
