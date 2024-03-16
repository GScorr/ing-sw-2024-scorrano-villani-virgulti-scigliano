package it.polimi.ingsw.model;
import it.polimi.ingsw.model.ENUM.ColorsEnum;
import it.polimi.ingsw.model.ENUM.PlayerState;
import it.polimi.ingsw.model.PlayCard;

import java.util.ArrayList;
import java.util.List;

/*
* TODO:
*   - al costruttore passo una lista, questa lista può essere direttamente copiata nella mia variabile oppure devo prima
*    copiarla in una lista ausiliaria ?
*   - impostare il metodo place()
*   - impostare il metodo peach()
*   - impostare il metodo peakGoal()
*   -
*   -*/
public class Player {
    private boolean isFirst;
    private final ColorsEnum color;
    private List<PlayCard> cards_in_hand = new ArrayList<>();
    private PlayerState player_state;

    public Player(boolean isFirst, ColorsEnum color, List<PlayCard> cards_in_hand) {
        this.isFirst = isFirst;
        this.color = color;
        this.cards_in_hand = cards_in_hand;
        this.player_state = PlayerState.NOT_INITIALIZED;
    }

    public Player nextPlayerState(){
        switch(player_state){
            case NOT_INITIALIZED:
                this.player_state = PlayerState.PLACE_CARD;
            case BEGIN :
                if(this.isFirst){
                    this.player_state = PlayerState.PLACE_CARD;
                }else {
                    this.player_state = PlayerState.WAIT_TURN;
                }
            case PLACE_CARD:
                    this.player_state = PlayerState.DRAW_CARD;
            case DRAW_CARD:
                this.player_state = PlayerState.WAIT_TURN;
        }
    }

}
