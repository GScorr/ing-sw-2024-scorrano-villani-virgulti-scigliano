package it.polimi.ingsw.SOCKET_FINAL;



import it.polimi.ingsw.RMI_FINAL.RmiController;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.SOCKET_FINAL.Message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ServerProxy implements VirtualServer {

    public ObjectOutputStream output;
    public ObjectInputStream input;

    public ServerProxy(ObjectOutputStream output, ObjectInputStream input) throws IOException {
        this.output = output;
        this.input = input;
    }


    public void inserisciGiocatore(String nome) throws IOException {
        CreatePlayerMessage DP_message = new CreatePlayerMessage(nome);
        output.writeObject(DP_message);
        output.flush();
    }
    public void lunchMessage(String message) throws IOException {
        Message DP_message = new LunchMessageMessage(message);
        output.writeObject(DP_message);
        output.flush();


    }

    public String checkName(String name) throws IOException, ClassNotFoundException {
        Message DP_message = new CheckNameMessage(name);
        output.writeObject(DP_message);
        output.flush();

        MyMessageFinal response = (MyMessageFinal) input.readObject();
        return response.getContent();

    }


    public List<SocketRmiControllerObject> getFreeGame() throws IOException, ClassNotFoundException {
        Message DP_message = new GetFreeGames();
        output.writeObject(DP_message);
        output.flush();

        List<SocketRmiControllerObject> games  = (List<SocketRmiControllerObject>) input.readObject();
        return games;
    }

    public  String createGame(String game_name, int num_players, String nome  ) throws IOException, ClassNotFoundException, SocketException {
        Message DP_message = new CreateGame(game_name,num_players,nome);
        output.writeObject(DP_message);
        output.flush();

        MyMessageFinal response = (MyMessageFinal) input.readObject();
        return response.getContent();

    }

    public boolean findRmiController(Integer id, String player_name) throws IOException, ClassNotFoundException {

        Message DP_message = new FindRMIControllerMessage(id, player_name);
        output.writeObject(DP_message);
        output.flush();

        MyMessageFinal response = (MyMessageFinal) input.readObject();

        return response.getContent().compareTo("true") == 0;

    }

    public void receiveHeartbeat() throws IOException {
        Message DP_message = new receiveHeartbeatMessage();
        output.writeObject(DP_message);
        output.flush();
    }

    public void getRmiController() throws IOException {
        Message DP_message = new getRmiControllerMessage();
        output.writeObject(DP_message);
        output.flush();

    }

    public void chooseGoal(int intero) throws IOException {
        Message DP_message = new chooseGoalMessage( intero);
        output.writeObject(DP_message);
        output.flush();


    }

    public void showStartingCard() throws IOException {
        System.out.println("entro");
        Message DP_message = new showStartingCardMessage();
        output.writeObject(DP_message);
        output.flush();
    }

    public void chooseStartingCard(boolean check) throws IOException {
        Message DP_message = new chooseStartingCardMessage(check);
        output.writeObject(DP_message);
        output.flush();
    }

    public void showGameField() throws IOException, ClassNotFoundException {
        Message DP_message = new showGameFieldMessage();
        /**
         * non sono sicuro sia giusto, non ritorna nulla ma prende una stringa token
         */
        output.writeObject(DP_message);
        output.flush();

    }

    @Override
    public void getMessage() {

    }

    public void getMessage(String token){

    }

    /**
     * da togliere
     */
    public void getPartita(){

    }
}
