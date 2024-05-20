package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;

import java.util.List;

public class setGameFieldResponse extends ResponseMessage{
    public List<GameField> games;

    public setGameFieldResponse(List<GameField> games) {
        this.games = games;
    }

    @Override
    public String getMessage() {
        return message;
    }
    @Override
    public void action(){
        super.miniModel.setGameField(this.games);
    };
}
