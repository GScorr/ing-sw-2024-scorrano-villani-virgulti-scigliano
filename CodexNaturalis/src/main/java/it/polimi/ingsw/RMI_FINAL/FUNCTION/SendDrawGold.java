package it.polimi.ingsw.RMI_FINAL.FUNCTION;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.RMI_FINAL.GameServer;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.*;

import java.io.IOException;

/**
 * Remote function to handle drawing a card from the gold deck.
 *
 * This class implements the `SendFunction` interface and represents the action
 * of a player drawing a card from the gold deck.
 *
 */
public class SendDrawGold implements SendFunction{

    String token;

    public SendDrawGold( String token) {
        this.token = token;
    }

    /**
     * Executes the action of drawing a card from the gold deck.
     *
     * This method attempts to draw a card from the gold deck and updates the game state accordingly.
     * It broadcasts player state updates to all connected clients.
     *
     * @param server the game server instance
     * @return a response message containing either a success message or an error message
     * @throws RuntimeException if an IOException or InterruptedException occurs
     */
    @Override
    public ResponseMessage action(GameServer server){
        ResponseMessage message;
        try{
            server.peachFromGoldDeck(token);
            message = new PrintMessage("Card inserted!");
            if(server.token_manager.getTokens().get(token) != null){
                server.token_manager.getTokens().get(token).setCards(server.token_to_player.get(token).getCardsInHand());
            }else{
                server.token_manager.getSocketTokens().get(token).setCards(server.token_to_player.get(token).getCardsInHand());
            }
             for (String t : server.token_to_player.keySet()){
                 if(server.token_manager.getTokens().get(t) != null){
                     server.token_manager.getTokens().get(t).setState( server.token_to_player.get(t).getActual_state().getNameState() );
                 }else{
                     server.token_manager.getSocketTokens().get(t).setState( server.token_to_player.get(t).getActual_state().getNameState() );
                 }
            }
        }catch(ControllerException e){
            message = new ErrorMessage(server.token_to_player.get(token).getName() +
                    e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return message;
    }
}
