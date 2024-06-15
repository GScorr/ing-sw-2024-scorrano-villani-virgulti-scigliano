package it.polimi.ingsw.RMI_FINAL.FUNCTION;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.RMI_FINAL.GameServer;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ErrorMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.GameFieldMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.SOCKET_FINAL.Message.Message;

import java.io.IOException;

/**
 * Remote function to insert a card on the game field.
 *
 * This class implements the `SendFunction` interface and represents the action
 * of a player inserting a card from their hand onto the game field at a specified
 * position (x, y) with a flipped state.
 */
public class SendInsertCard implements SendFunction{

    String token;
    int index;
    int x;
    int y;
    boolean flipped;

    public SendInsertCard( String token,int index,  int x, int y, boolean flipped) {
        this.token = token;
        this.index = index;
        this.x = x;
        this.y = y;
        this.flipped = flipped;
    }

    /**
     * Executes the action of inserting a card on the game field.
     *
     * This method attempts to insert a card from the player's hand onto the game field
     * at the specified coordinates (x, y) and flipped state. It updates the game state
     * accordingly and broadcasts updates to all connected clients.
     *
     * @param server the game server instance
     * @return a response message containing the game field update or an error message
     * @throws RuntimeException if an IOException occurs
     */
    @Override
    public ResponseMessage action(GameServer server)  {
        ResponseMessage message;
        try{
            server.insertCard(token, index, x , y, flipped);
             message = new GameFieldMessage(server.token_to_player.get(token).getGameField());
            for (String t : server.token_to_player.keySet()){
                if( server.token_manager.getTokens().containsKey(t) ){
                    server.token_manager.getTokens().get(t).setGameField(server.getGameFields(t));
                    server.token_manager.getTokens().get(t).setLastTurn(server.getController().isIs_final_state());
                }
                if(server.token_manager.getSocketTokens().containsKey(t)){
                    server.token_manager.getSocketTokens().get(t).setGameField(server.getGameFields(t));

                }
            }
            for (String t : server.token_to_player.keySet()){
                if( server.token_manager.getTokens().containsKey(t) ) server.token_manager.getTokens().get(t).setState( server.token_to_player.get(t).getActual_state().getNameState() );
                if(server.token_manager.getSocketTokens().containsKey(t)) server.token_manager.getSocketTokens().get(t).setState(server.token_to_player.get(t).getActual_state().getNameState() );
            }
        }catch(ControllerException e){
            message = new ErrorMessage(server.token_to_player.get(token).getName() +
                    e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return message;
    }
}
