package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Card.PlayCard;

public class PointResponse extends ResponseMessage{
    public int player_point;

    public PointResponse(int point) {
        this.player_point = point;
    }






}
