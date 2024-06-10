package it.polimi.ingsw.MODEL.Card;

import it.polimi.ingsw.MODEL.ENUM.Costraint;

/**
 * Represents a Starting Card in the game, treated differently from normal PlayCards.
 *
 * - front_side functions similarly to regular cards.
 * - back_side additionally stores a list of central resources specific to StartingCards.
 *
 * Extends {@link PlayCard}.
 */
public class StartingCard extends PlayCard{

    /**
     * Creates a new Starting Card.
     *
     * @param front_side Front side of the card.
     * @param back_side Back side of the card.
     * @param flipped set true if the card is on the backside.
     */
    public StartingCard(Side front_side, Side back_side, boolean flipped) {
        super(front_side,back_side,flipped);

    }

    public  String getType(){return "Starting Card";}

    public  int getPoint(){return 0;}

    public Costraint getCostraint(){return Costraint.NONE;};

}
