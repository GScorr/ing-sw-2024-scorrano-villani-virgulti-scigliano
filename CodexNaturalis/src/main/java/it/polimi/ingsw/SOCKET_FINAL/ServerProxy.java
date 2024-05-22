package it.polimi.ingsw.SOCKET_FINAL;



import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.SOCKET_FINAL.Message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.IOException;
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

    //bisogna che lo cambi e togli questa risposta : MyMessageFinal response = (MyMessageFinal) input.readObject();
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

    public  void createGame(String game_name, int num_players, String nome  ) throws IOException, ClassNotFoundException, SocketException {
        Message DP_message = new CreateGame(game_name,num_players,nome);
        output.writeObject(DP_message);
        output.flush();

    }

    public void findRmiController(Integer id, String player_name) throws IOException, ClassNotFoundException {
        Message DP_message = new FindRMIControllerMessage(id, player_name);
        output.writeObject(DP_message);
        output.flush();

    }

    public String getPlayerState() throws IOException, ClassNotFoundException {
        Message DP_mesasage = new getPlayerState();
        output.writeObject(DP_mesasage);
        output.flush();

        MyMessageFinal response = (MyMessageFinal) input.readObject();
        return response.getContent();
    }

    public void getGoalCard() throws IOException, ClassNotFoundException {
        Message DP_mesasage = new getGoalCard();
        output.writeObject(DP_mesasage);
        output.flush();
    }

    public void getListGoalCard() throws IOException, ClassNotFoundException {
        Message DP_mesasage = new getListGoalCard();
        output.writeObject(DP_mesasage);
        output.flush();

    }

    public void chooseGoal(int index) throws IOException {
        Message DP_message = new chooseGoalMessage( index);
        output.writeObject(DP_message);
        output.flush();

    }

    public void getStartingCard() throws IOException, ClassNotFoundException {
        Message DP_mesasage = new getStartingCard();
        output.writeObject(DP_mesasage);
        output.flush();

    }

    void startingCardIsPlaced() throws IOException, ClassNotFoundException {
        Message DP_message = new firstCardIsPlaced();
        output.writeObject(DP_message);
        output.flush();


    }

    public void chooseStartingCard(boolean check) throws IOException {
        Message DP_message = new chooseStartingCardMessage(check);
        output.writeObject(DP_message);
        output.flush();
    }
/*
    public GameField getGameField() throws IOException, ClassNotFoundException {
        Message DP_mesasage = new getGameField();
        output.writeObject(DP_mesasage);
        output.flush();
        GameField game_field  = (GameField) input.readObject();
        return game_field;
    }
*/
    public List<PlayCard> getCardsInHand() throws IOException, ClassNotFoundException {
        Message DP_mesasage = new getCardsInHand();
        output.writeObject(DP_mesasage);
        output.flush();
        List<PlayCard> cards_in_hand  = (List<PlayCard>) input.readObject();
        return cards_in_hand;
    }

    public void placeCard(int index, int x, int y, boolean flipped) throws IOException, ClassNotFoundException {
        Message DP_mesasage = new placeCard(index,x,y,flipped);
        output.writeObject(DP_mesasage);
        output.flush();
    }

    public void getGoldDeckSize() throws IOException, ClassNotFoundException {
        Message DP_mesasage = new getGoldDeckSize();
        output.writeObject(DP_mesasage);
        output.flush();

    }

    public void getResourcesDeckSize() throws IOException, ClassNotFoundException {
        Message DP_mesasage = new getResourcesDeckSize();
        output.writeObject(DP_mesasage);
        output.flush();

    }

    public void getCardsInCenter() throws IOException, ClassNotFoundException {
        Message DP_mesasage = new getCardsInCenter();
        output.writeObject(DP_mesasage);
        output.flush();

    }

    public void peachFromGoldDeck() throws IOException {
        Message DP_mesasage = new peachFromGoldDeck();
        output.writeObject(DP_mesasage);
        output.flush();
    }

    public void peachFromResourcesDeck() throws IOException {
        Message DP_mesasage = new peachFromResourcesDeck();
        output.writeObject(DP_mesasage);
        output.flush();
    }

    public void peachFromCardsInCenter(int index) throws IOException {
        Message DP_mesasage = new peachFromCardsInCenter(index);
        output.writeObject(DP_mesasage);
        output.flush();
    }

    public void getPoint() throws IOException, ClassNotFoundException {
        Message DP_mesasage = new getPoint();
        output.writeObject(DP_mesasage);
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


    public void showCard(PlayCard card) throws IOException {
        Side back = card.getBackSide();
        Side front = card.getFrontSide();

        System.out.println("BACK SIDE\n----------------------------");
        System.out.println( " | " + back.getAngleLeftUp().toString().substring(0,2)  +   " |               "+ " | " + back.getAngleRightUp().toString().substring(0,2) + " |\n " );
        //System.out.println( " | " + back.getAngleRightUp().toString().charAt(0) + " |\n " );
        System.out.println( " |       | " + back.getCentral_resource().toString().substring(0,2) + back.getCentral_resource2().toString().substring(0,2) + back.getCentral_resource3().toString().substring(0,2) + " |         |\n " );
        System.out.println( " | " + back.getAngleLeftDown().toString().substring(0,2) +  " |               " + " | " + back.getAngleRightDown().toString().substring(0,2) + " |\n " );
        //System.out.println(  );
        System.out.println("----------------------------\n\n");

        System.out.println("FRONT SIDE\n----------------------------");

        if(card instanceof ResourceCard) {
            System.out.println(" | " + card.getPoint() + " | ");
            if (card instanceof GoldCard) {
                System.out.println(" | " + ((GoldCard) card).getPointBonus().toString().substring(0, 2) + " | " + "             | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
            } else {
                System.out.println(" | " + front.getAngleLeftUp().toString().substring(0, 2) + " | " + "              | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
            }
        }
        else {
            System.out.println(" | " + front.getAngleLeftUp().toString().substring(0, 2) + " | " + "              | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
        }
        //System.out.println( " | " + front.getAngleRightUp().toString().charAt(0) + " |\n " );
        System.out.println( " |       | " + front.getCentral_resource().toString().substring(0,2) + front.getCentral_resource2().toString().substring(0,2) + front.getCentral_resource3().toString().substring(0,2) + " |        |\n " );
        //System.out.println( " | " + front.getAngleLeftDown().toString().charAt(0) + " |       " );
        if ( card instanceof GoldCard ){
            System.out.println( " | " + front.getAngleLeftDown().toString().substring(0,2) + " | " +
                    "  " + card.getCostraint().toString()  + " | " + front.getAngleRightDown().toString().substring(0,2) + " |\n ");
        }else{
            System.out.println( " | " + front.getAngleLeftDown().toString().substring(0,2) + " |              " + " | " + front.getAngleRightDown().toString().substring(0,2) + " |\n " );
        }
        //System.out.println( " | " + front.getAngleRightDown().toString().charAt(0) + " |\n " );
        System.out.println("----------------------------\n\n");

    }

    public void showField(GameField field) throws IOException {
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
