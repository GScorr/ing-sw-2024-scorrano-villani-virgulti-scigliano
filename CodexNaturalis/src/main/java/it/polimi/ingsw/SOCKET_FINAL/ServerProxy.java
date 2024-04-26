package it.polimi.ingsw.SOCKET_FINAL;



import it.polimi.ingsw.SOCKET_FINAL.Message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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

        // Wait for response from server
        Object response = input.readObject();

        /*
        * if response.getBool() return true
        * else return false
        * */
        return true;
    }

    public String getToken(VirtualView client) throws IOException, ClassNotFoundException {
        Message DP_message = new GetTokenMessage(client);
        output.writeObject(DP_message);
        output.flush();

        // Wait for response from server
        Object response = input.readObject();

        /*
         * return response.getString()
         * */
        return "token_di_prova";
    }

    public ArrayList getFreeGame(String token) throws IOException, ClassNotFoundException {
        Message DP_message = new GetFreeGames(token);
        output.writeObject(DP_message);
        output.flush();

        // Wait for response from server
        Object response = input.readObject();

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
        Object response = input.readObject();

        /*
        if(response.getBool()) {
            return "creazione Player andata a buon fine!\n";
        }else{
            throw new SocketException(0, "Game non creato");
        }

         */

        return "creazione Player andata a buon fine!\n";
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
