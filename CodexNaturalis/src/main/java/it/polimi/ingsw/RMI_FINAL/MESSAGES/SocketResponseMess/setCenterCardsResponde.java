package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.DeckPackage.CenterCards;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

public class setCenterCardsResponde extends ResponseMessage {
    public CenterCards cards ;
    public PlayCard res;

    public PlayCard gold;

    public setCenterCardsResponde( CenterCards cards, PlayCard res, PlayCard gold) {
        this.cards = cards;
        this.res = res;
        this.gold = gold;
    }


    @Override
    public void action(){

        super.client.miniModel.setCardsInCenter(cards, res,gold);
    };
}
