package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class ClientProxy implements VirtualView {
    final ObjectOutputStream output;

    public ClientProxy(ObjectOutputStream output) {
        this.output = output;
    }


    public void reportError(String errore){
        /*
        output.println("errore");
        output.println(errore);
        output.flush();

         */
    }

    @Override
    public void reportMessage(String details) throws IOException {

    }

    @Override
    public void showCard(PlayCard card) throws IOException {

    }

    @Override
    public void pushBack(ResponseMessage message) throws IOException {

    }

    @Override
    public void showField(GameField field) throws IOException {

    }

    @Override
    public void printString(String s) throws IOException {

    }

    @Override
    public void setGameField(List<GameField> games) throws IOException {

    }

    @Override
    public MiniModel getMiniModel() throws IOException {
        return null;
    }

    @Override
    public void setCards(List<PlayCard> cards) throws IOException {

    }

    @Override
    public void setNumToPlayer(HashMap<Integer, String> map) throws IOException {

    }

    @Override
    public void setState(String state) throws IOException {

    }

    public void showMessage(String message){
        /*
        output.println("update_message");
        output.println(message);
        output.flush();

         */
    }
    @Override
    public void showValue(String number) {
        /*
        output.println("update_number");
        output.println(number);
        output.flush();

         */
    }

    @Override
    public void showUpdate(GameField game_field) throws IOException {

    }
}
