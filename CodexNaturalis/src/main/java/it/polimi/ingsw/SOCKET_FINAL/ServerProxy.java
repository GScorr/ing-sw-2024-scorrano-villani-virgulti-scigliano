package it.polimi.ingsw.SOCKET_FINAL;



import it.polimi.ingsw.RMI_FINAL.RmiController;
import it.polimi.ingsw.SOCKET_FINAL.Message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;



public class ServerProxy implements VirtualServer {

    public ObjectOutputStream output;
    public ObjectInputStream input;

    public ServerProxy(ObjectOutputStream output,ObjectInputStream input) throws IOException {
        this.output = output;
        this.input = input;
    }


    public void inserisciGiocatore(String nome, String token) throws IOException {
        CreatePlayerMessage DP_message = new CreatePlayerMessage(nome,token);
        output.writeObject(DP_message);
        output.flush();
    }
    public void lunchMessage(String message, String token) throws IOException {
        Message DP_message = new LunchMessageMessage(message,token);
        output.writeObject(DP_message);
        output.flush();


    }

    public boolean checkName(String name) throws IOException, ClassNotFoundException {
        Message DP_message = new CheckNameMessage(name);
        output.writeObject(DP_message);
        output.flush();

        MyMessageFinal response = (MyMessageFinal) input.readObject();

        if(response.getContent().compareTo("TRUE") == 0){
            return true;
        }else{
            return false;
        }

    }

    public String getToken(String name) throws IOException, ClassNotFoundException {
        Message DP_message = new GetTokenMessage(name);
        output.writeObject(DP_message);
        output.flush();

        MyMessageFinal response = (MyMessageFinal) input.readObject();

        return response.getContent();
    }

    public ArrayList getFreeGame(String token) throws IOException, ClassNotFoundException {
        Message DP_message = new GetFreeGames(token);
        output.writeObject(DP_message);
        output.flush();

        // Wait for response from server
        //Object response = input.readObject();

        /*
         * return response.getList()
         * */
        return null;
    }

    public  String createGame(String game_name, int num_players, String token, String nome  ) throws IOException, ClassNotFoundException, SocketException {
        Message DP_message = new CreateGame(game_name,num_players,token,nome);
        output.writeObject(DP_message);
        output.flush();
        // Wait for response from server
        //Object response = input.readObject();

        /*
        if(response.getBool()) {
            return "creazione Player andata a buon fine!\n";
        }else{
            throw new SocketException(0, "Game non creato");
        }

         */

        return "creazione Player andata a buon fine!\n";
    }

    public boolean findRmiController(Integer id, String p_token, String player_name) throws IOException, ClassNotFoundException {

        Message DP_message = new FindRMIControllerMessage(id, p_token, player_name);
        output.writeObject(DP_message);
        output.flush();
     /*
        MyMessageFinal response = (MyMessageFinal) input.readObject();

        if(response.getContent().compareTo("TRUE") == 0){
            return true;
        }else{
            return false;
        }

      */
        return true;

    }

    public void receiveHeartbeat(String token) throws IOException {
        Message DP_message = new receiveHeartbeatMessage(token);
        output.writeObject(DP_message);
        output.flush();
    }

    public void getRmiController(String token) throws IOException {
        Message DP_message = new getRmiControllerMessage(token);
        output.writeObject(DP_message);
        output.flush();

    }

    public void chooseGoal(String token, int intero) throws IOException {
        Message DP_message = new chooseGoalMessage(token, intero);
        output.writeObject(DP_message);
        output.flush();


    }

    public void showStartingCard(String token) throws IOException {
        System.out.println("entro");
        Message DP_message = new showStartingCardMessage(token);
        output.writeObject(DP_message);
        output.flush();
    }

    public void chooseStartingCard(String token, boolean check) throws IOException {
        Message DP_message = new chooseStartingCardMessage(token, check);
        output.writeObject(DP_message);
        output.flush();
    }

    public void showGameField(String token) throws IOException, ClassNotFoundException {
        Message DP_message = new showGameFieldMessage(token);
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
