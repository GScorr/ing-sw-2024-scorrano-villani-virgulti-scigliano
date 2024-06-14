package it.polimi.ingsw.MODEL.Card;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.Costraint;

import java.io.Serializable;

/**
 * Abstract base class representing playable cards in the game
 */
public abstract class  PlayCard implements Serializable {

    /**
     * Front side of the card
     */
    private final Side front_side ;
    /**
     * Back side of the card
     */
    private final  Side back_side;

    /**
     * path to the side image
     */
    public  String front_side_path;
    public  String back_side_path;

    /**
     * true when the card is flipped on the backside
     */
    public boolean flipped;
    /**
     *  Central resource type associated with the card's back side
     */
    public final CentralEnum colore;

    /**
     * Creates a PlayCard with front and back sides.
     *
     * @param front_side The front side of the card.
     * @param back_side The back side of the card.
     * @param flipped set true if the card is on the backside
     */
    public PlayCard(Side front_side, Side back_side, boolean flipped) {
        this.front_side = front_side;
        this.back_side = back_side;
        flipCard(flipped);
        this.colore = back_side.getCentral_resource();
    }

    public void setFront_side_path(String front_side_path) {
        this.front_side_path = front_side_path;
    }

    public void setBack_side_path(String back_side_path) {
        this.back_side_path = back_side_path;
    }

    /**
     * Flips the card.
     *
     * @param flipped The new flipped state.
     */
    public void flipCard(boolean flipped){
        this.flipped = flipped;
    }

    public Side getFrontSide() {
        return front_side;
    }

    public Side getBackSide() {
        return back_side;
    }
    public  Side getSide(){
        if( !flipped){
            return getFrontSide();
        }
        else{
            return getBackSide();
        }
    }

    public abstract String getType();

    public abstract int getPoint();

    public abstract Costraint getCostraint();

    public CentralEnum getColore() {
        return colore;
    }

}
