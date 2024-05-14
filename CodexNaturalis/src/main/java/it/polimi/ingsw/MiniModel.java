package it.polimi.ingsw;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.SOCKET_FINAL.ResponseMessage.ResponseMessage;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class MiniModel {
    List<GameField> game_fields ;
    private List<PlayCard> cards_in_hand ;

    private PlayerState player_state;

    private Queue<ResponseMessage> messages = new LinkedList<>();

    public MiniModel(List<GameField> game_fields) {this.game_fields = game_fields;}



    public void pushBack(ResponseMessage mess){
        messages.add(mess);
    }

    public ResponseMessage popOut(){
       return messages.poll();
    }

    public void showGameField(int pos) throws RemoteException {
        showField(game_fields.get(pos));
    }

    public void setGameField(int pos, GameField game){
        game_fields.add(pos, game);
    }

    public void setCards(List<PlayCard> cards){
        cards_in_hand = cards;
    }


    public PlayerState getState(){ return player_state;}

    public void setState(PlayerState ps){ player_state = ps; }




    private void showField(GameField field) throws RemoteException {
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
