package it.polimi.ingsw.MODEL.Card;
import it.polimi.ingsw.MODEL.ENUM.BonusEnum;
import it.polimi.ingsw.MODEL.ENUM.Costraint;

/**
 * Represents a Gold Card in the game.
 * Extends {@link ResourceCard}.
 */
public class GoldCard extends ResourceCard{

    /**
     * Constraint associated with the Gold Card (inactive when the card is flipped).
     */
    private final Costraint costraint;

    /**
     *  Point bonus type for the Gold Card
     */
    private final BonusEnum point_bonus;

    /**
     * Creates a new Gold Card.
     *
     * @param front_side Front side of the card.
     * @param back_side Back side of the card.
     * @param flipped Whether the card is flipped.
     * @param point Base point value of the card.
     * @param costraint Constraint associated with the card (inactive when flipped).
     * @param point_bonus Point bonus type for the card.
     */
    public GoldCard(Side front_side, Side back_side, boolean flipped, int point, Costraint costraint, BonusEnum point_bonus){
        super( front_side, back_side ,flipped,  point);
        this.costraint = costraint;
        this.point_bonus = point_bonus;
    }

    public  String getType(){return "Gold";}


    public Costraint getCostraint() {

        if(!super.flipped)return costraint;
        else return Costraint.NONE;
    }

    public BonusEnum getPointBonus(){
        return  point_bonus;
    }
}
