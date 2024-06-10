package it.polimi.ingsw.MODEL.Card;
import it.polimi.ingsw.MODEL.ENUM.Costraint;

/**
 * Represents a Resource Card in the game.
 * Extends {@link PlayCard}.
 */
public class ResourceCard extends PlayCard {

    /**
     * Base point value of the card.
     */
    private final int point;

    /**
     * Creates a new Resource Card.
     *
     * @param front_side Front side of the card.
     * @param back_side Back side of the card.
     * @param flipped set true if the card is on the backside.
     * @param point Base point value of the card.
     */
    public ResourceCard(Side front_side, Side back_side, boolean flipped, int point){
        super(front_side, back_side,  flipped);
        this.point= point;
    }

    public  String getType(){return "Resource";}

    public int getPoint() {
        if(!super.flipped) return point;
        else return 0;
    }

    public Costraint getCostraint(){
        return Costraint.NONE;
    }

}
