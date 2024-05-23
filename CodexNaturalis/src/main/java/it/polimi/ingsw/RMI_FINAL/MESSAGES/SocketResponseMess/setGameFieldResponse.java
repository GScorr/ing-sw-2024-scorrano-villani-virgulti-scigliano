package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Game.Game;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

public class setGameFieldResponse extends ResponseMessage {
    public List<GameField> games = new ArrayList<>();

    public setGameFieldResponse(List<GameField> games) {
        this.games.addAll(games);
    }

    @Override
    public void action()  {
        super.client.miniModel.setGameField(this.games);
    };

}
