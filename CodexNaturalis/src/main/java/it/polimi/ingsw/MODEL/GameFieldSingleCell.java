package it.polimi.ingsw.MODEL;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;

//@Davide
// Class for the single cell present in the matrix of GameField
public class GameFieldSingleCell {
    private boolean filled;
    private PlayCard card;
    private AnglesEnum value;
    private PlayCard card_down;

    public GameFieldSingleCell(boolean filled, PlayCard card, AnglesEnum value, PlayCard card_down) {
        this.filled = filled;
        this.card = card;
        this.value = value;
        this.card_down = card_down;
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

    public PlayCard getCardDown() {
        if ( !isEmpty() ) return card_down;
        System.out.print("ERROR: THERE IS NO CARD HERE");
        return null;
    }
    public void setCardDown(PlayCard card) {
        this.card_down = card;
    }
}
