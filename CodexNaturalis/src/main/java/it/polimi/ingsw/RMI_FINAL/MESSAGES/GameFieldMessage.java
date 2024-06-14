package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.GameField;

/**
 * Response message for sending the game field information to the client.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a `GameField` object representing the current state of the player's game board.
 *
 */
public class GameFieldMessage extends ResponseMessage{

    GameField field;

    public GameFieldMessage(GameField field) {
        this.field = field;
    }

    public GameField getField() {
        return field;
    }
/*
da eliminare
    public GameField actionPrintGameField(){
        return field;
    }

 */

    public void setField(GameField field) {
        this.field = field;
    }

    /**
     * Updates the client's model with the game field
     */
    @Override
    public void action(){
        showField(field);
    }

    /**
     * Prints the game field to the console
     *
     * @param field the game field to print
     */
    public void showField(GameField field) {
        System.out.println("\nPLAYER : [ " + field.getPlayer().getName() + " ] JUST PLACED A CARD, THIS IS HIS NEW FIELD :\n" );
        boolean[] nonEmptyRows = new boolean[Constants.MATRIXDIM];
        boolean[] nonEmptyCols = new boolean[Constants.MATRIXDIM];


        for (int i = 0; i < Constants.MATRIXDIM; i++) {
            for (int j = 0; j < Constants.MATRIXDIM; j++) {
                if (field.getCell(i, j, Constants.MATRIXDIM).isFilled()) {
                    nonEmptyRows[i] = true;
                    nonEmptyCols[j] = true;


                    if (i > 0) nonEmptyRows[i - 1] = true;
                    if (i < Constants.MATRIXDIM - 1) nonEmptyRows[i + 1] = true;
                    if (j > 0) nonEmptyCols[j - 1] = true;
                    if (j < Constants.MATRIXDIM - 1) nonEmptyCols[j + 1] = true;
                }
            }
        }


        System.out.print("   ");
        for (int k = 0; k < Constants.MATRIXDIM; k++) {
            if (nonEmptyCols[k]) {
                System.out.print(k + " ");
            }
        }
        System.out.print("\n");


        for (int i = 0; i < Constants.MATRIXDIM; i++) {
            if (nonEmptyRows[i]) {
                System.out.print(i + " ");
                for (int j = 0; j < Constants.MATRIXDIM; j++) {
                    if (nonEmptyCols[j]) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).isFilled()) {
                            System.out.print(field.getCell(i, j, Constants.MATRIXDIM).getShort_value() + " ");
                        } else {
                            System.out.print("   ");
                        }
                    }
                }
                System.out.print("\n");
            }
        }
    }

}
