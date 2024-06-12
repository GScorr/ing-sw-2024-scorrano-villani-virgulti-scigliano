/*package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class ClientProxy  {

    final ObjectInputStream input;

    // when a message is sent to server, Client has to receive a response. Thsi variable represent the response from the serverr, in fact they are passed to clientProxy
    boolean flag_check;
    boolean place_flag_check;
    boolean check;

    boolean checkSizeGoldDeck;
    boolean checkSizeResourcesDeck;
    int point;

    boolean GoalCardisPresent;
    List<Goal> goalsCard;

    PlayCard startingCard;
    boolean startingCardChoosed;

    Goal goal_choosed;

    public ClientProxy(ObjectInputStream input) {
        this.input = input;
    }

    public void  startCheckingMessages() throws IOException,ClassNotFoundException{
        new Thread( () -> {

        }).start();
    }



}

 */
