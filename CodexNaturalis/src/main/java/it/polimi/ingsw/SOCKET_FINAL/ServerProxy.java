package it.polimi.ingsw.SOCKET_FINAL;



import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.SOCKET_FINAL.Message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;


public class ServerProxy implements  Serializable {

    public ObjectOutputStream output;
    public ObjectInputStream input;

    public ServerProxy(ObjectOutputStream output) throws IOException {
        this.output = output;
    }

    // this function send the Message Object to the server
    private void sendMessage(Message DP_Message) throws IOException {
        output.writeObject(DP_Message);
        output.flush();
        output.reset();
    }



    // ---- create the subclasses Object and call sendMessage(...)  ----


    public void checkName(String name) throws IOException, ClassNotFoundException {
        Message DP_message = new CheckNameMessage(name);
        sendMessage(DP_message);
    }


    public void getFreeGame() throws IOException, ClassNotFoundException {
        Message DP_message = new GetFreeGames();
        sendMessage(DP_message);
    }

    public  void createGame(String game_name, int num_players, String nome  ) throws IOException, ClassNotFoundException, SocketException {
        Message DP_message = new CreateGame(game_name,num_players,nome);
        sendMessage(DP_message);
    }

    public void findRmiController(Integer id, String player_name) throws IOException, ClassNotFoundException {
        Message DP_message = new FindRMIControllerMessage(id, player_name);
        sendMessage(DP_message);

    }

    public void getGoalCard() throws IOException, ClassNotFoundException {
        Message DP_message = new getGoalCard();
        sendMessage(DP_message);
    }

    public void getListGoalCard() throws IOException {
        Message DP_message = new getListGoalCard();
        sendMessage(DP_message);
    }

    public void chooseGoal(int index) throws IOException {
        Message DP_message = new chooseGoalMessage( index);
        sendMessage(DP_message);
    }

    public void getStartingCard() throws IOException {
        Message DP_message = new getStartingCard();
        sendMessage(DP_message);
    }

    void startingCardIsPlaced() throws IOException {
        Message DP_message = new firstCardIsPlaced();
        sendMessage(DP_message);
    }

    public void chooseStartingCard(boolean check) throws IOException {
        Message DP_message = new chooseStartingCardMessage(check);
        sendMessage(DP_message);
    }


    public void placeCard(int index, int x, int y, boolean flipped) throws IOException, ClassNotFoundException {
        Message DP_message = new placeCard(index,x,y,flipped);
        sendMessage(DP_message);
    }

    public void isGoldDeckPresent() throws IOException, ClassNotFoundException {
        Message DP_message = new getGoldDeckSize();
        sendMessage(DP_message);

    }

    public void isResourceDeckPresent() throws IOException, ClassNotFoundException {
        Message DP_message = new getResourcesDeckSize();
        sendMessage(DP_message);
    }

    public void getCardsInCenter() throws IOException, ClassNotFoundException {
        Message DP_message = new getCardsInCenter();
        sendMessage(DP_message);

    }
/*
    public void peachFromGoldDeck() throws IOException {
        Message DP_message = new peachFromGoldDeck();
        sendMessage(DP_message);
    }

    public void peachFromResourcesDeck() throws IOException {
        Message DP_message = new peachFromResourcesDeck();
        sendMessage(DP_message);
    }

    public void peachFromCardsInCenter(int index) throws IOException {
        Message DP_message = new peachFromCardsInCenter(index);
        sendMessage(DP_message);
    }
*/

    public void drawCard(SendFunction function) throws IOException {
        Message DP_message = new drawCard(function);
        sendMessage(DP_message);
    }
    public void getPoint() throws IOException, ClassNotFoundException {
        Message DP_message = new getPoint();
        sendMessage(DP_message);
    }


    public void connectGame() throws IOException {
        Message DP_message = new connectGame();
        sendMessage(DP_message);
    }

    public void getToken() throws IOException {
        Message DP_message = new getToken();
        sendMessage(DP_message);
    }

    public void chattingGlobal(ChatMessage chatMessage) throws IOException {
        Message DP_message = new chatGlobal(chatMessage);
        sendMessage(DP_message);
    }

    public void chattingMoment(int myIndex, int decision, ChatMessage chatMessage) throws IOException {
        Message DP_message = new chatMoment(myIndex,decision,chatMessage);
        sendMessage(DP_message);
    }
}
