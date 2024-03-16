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

    public Player(boolean isFirst, ColorsEnum color, List<PlayCard> cards_in_hand, PlayerState player_state) {
        this.isFirst = isFirst;
        this.color = color;
        this.cards_in_hand = cards_in_hand;
        this.player_state = player_state;
    }


}
