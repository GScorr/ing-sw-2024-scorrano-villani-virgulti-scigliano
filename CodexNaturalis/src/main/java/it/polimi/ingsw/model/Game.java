package it.polimi.ingsw.model;

//

public class Game {
    private int num_player;
    private int[] points;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    public int getNumPlayer() {
        return num_player;
    }

    public void setNumPlayer(int num_player) {
        this.num_player = num_player;
    }

    //Getter of points, given the index of the player
    public int getPoints(int player_index) {
        if( player_index < 0 || player_index > num_player )  System.out.printf("\n ERROR: INDEX EXCEED DOMAIN");
        return points[player_index];
    }

    //Setter of points, one for 2 player and one for 4 players
    public void setPoints(int points_player1, int points_player2 ) {
        this.points[0] = points_player1;
        this.points[1] = points_player2;
    }
    public void setPoints(int points_player1, int points_player2, int points_player3, int points_player4 ) {
        this.points[0] = points_player1;
        this.points[1] = points_player2;
        this.points[2] = points_player3;
        this.points[3] = points_player4;
    }

}
