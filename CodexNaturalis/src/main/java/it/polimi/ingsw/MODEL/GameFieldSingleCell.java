package it.polimi.ingsw.MODEL;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;

//@Davide
// Class for the single cell present in the matrix of GameField
public class GameFieldSingleCell {
    private boolean filled;
    private PlayCard card;
    private AnglesEnum value;

    public GameFieldSingleCell(boolean filled, PlayCard card, AnglesEnum value) {
        this.filled = filled;
        this.card = card;
        this.value = value;
    }
    public boolean isEmpty() {
        return !filled;
    }
    public void setFilled(boolean filled) {
        this.filled = filled;
    }
    public PlayCard getCard() {
        if ( !isEmpty() ) return card;
        System.out.print("ERROR: THERE IS NO CARD HERE");
        return null;
    }
    public void setCard(PlayCard card) {
        this.card = card;
    }
    public AnglesEnum getValue() {
        return value;
    }
    public void setValue(AnglesEnum value) {
        this.value = value;
    }

}
