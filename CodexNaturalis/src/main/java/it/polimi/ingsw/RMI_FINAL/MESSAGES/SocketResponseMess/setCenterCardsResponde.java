package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Response message for setting the center cards and development card reward.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing information about the center cards, the development card reward
 * (if applicable), and the gold reward card (if applicable).
 *
 */
public class setCenterCardsResponde extends ResponseMessage {

    public CenterCards cards ;
    public PlayCard res;

    public PlayCard gold;

    public setCenterCardsResponde( CenterCards cards, PlayCard res, PlayCard gold) {
        this.cards = cards;
        this.res = res;
        this.gold = gold;
    }

    /**
     * Updates the client's model with the center cards, reward card, and gold reward card.
     *
     * This method delegates setting the center cards, development card reward (if received),
     * and gold reward card (if received) to the client's miniModel object.
     */
    @Override
    public void action(){

        super.client.miniModel.setCardsInCenter(cards, res,gold);
    }
}
