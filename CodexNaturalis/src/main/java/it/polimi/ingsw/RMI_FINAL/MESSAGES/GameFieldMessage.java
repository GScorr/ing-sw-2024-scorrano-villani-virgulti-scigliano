package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.GameField;

public class GameFieldMessage extends ResponseMessage{
    GameField field;

    public GameFieldMessage(GameField field) {
        this.field = field;
    }

    public GameField getField() {
        return field;
    }

    public GameField actionPrintGameField(){
        return field;
    }

    public void setField(GameField field) {
        this.field = field;
    }
}
