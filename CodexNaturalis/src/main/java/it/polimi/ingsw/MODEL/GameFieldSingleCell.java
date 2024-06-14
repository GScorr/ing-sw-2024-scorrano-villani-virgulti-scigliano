package it.polimi.ingsw.MODEL;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.EdgeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
    todo
        togliere commenti per la gui
        e eliminare metodi
 */

/**
 * This class manage the single cell of the game field
 */
public class GameFieldSingleCell implements Serializable {

    /**
     * Variable used to indicate if the single cell of the game field is filled with a card
     */
    private boolean filled;

    private int order_above = 0;
    private int order_below = 0;
    private PlayCard card;
    private AnglesEnum value;
    private PlayCard card_down;
    private List<AnglesEnum> values = new ArrayList<>(); //probably useless, but I'm waiting for the Gui_Initialization
    private List<EdgeEnum> edges = new ArrayList<>(); //probably useless, but I'm waiting for the Gui_Initialization


    public GameFieldSingleCell(boolean filled, PlayCard card, AnglesEnum value, PlayCard card_down) {
        this.filled = filled;
        this.card = card;
        this.value = value;
        this.card_down = card_down;
    }

    /**
     * This method check if the single cell is empty
     *
     * @return return true if is empty and false if not
     */
    public boolean isEmpty() {
        return !filled;
    }

    /**
     * This method check if the single cell is filled with a card
     *
     * @return return true if the card is on the cell and false if not
     */
    public boolean isFilled() { //ritorna 1 se c'è la carta metodo aggiunto -mirko-
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public PlayCard getCard() {
        if(!isFilled() && getValue().equals(AnglesEnum.EMPTY)){
            return card;
        }
        else if ( filled ) return card;
        else {
            return null;
        }
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
        if(!isFilled() && getValue().equals(AnglesEnum.EMPTY)){
            return card_down;
        }
        else{
            if ( !isEmpty() ) return card_down;
            return null;
        }

    }

    public void setCardDown(PlayCard card) {
        this.card_down = card;
    }

    public String getShort_value(){
        switch(value){
            case ANIMAL:
                return "AN";
            case PEN:
                return "PE";
            case PAPER:
                return "PA";
            case PLANT:
                return "PL";
            case FEATHER:
                return "FE";
            case INSECTS:
                return "IN";
            case MUSHROOMS:
                return "MU";
            case NONE:
                return "NO";
            case EMPTY:
                return "EM";
        }
        return " ";
    }

    /* da eliminare
    public List<EdgeEnum> getEdges() {
        return edges;
    }

     */

    public void setValues(AnglesEnum value, int index) {
        values.add(index,value);
    }

    public void setEdges(EdgeEnum value, int index) {
        edges.add(index,value);
    }

    public int getOrder_above() {
        return order_above;
    }

    public int getOrder_below() {
        return order_below;
    }

    public void setOrder_above(int order_above) {
        this.order_above = order_above;
    }

    public void setOrder_below(int order_below) {
        this.order_below = order_below;
    }

}
