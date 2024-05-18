package it.polimi.ingsw.RMI_FINAL.FUNCTION;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.RMI_FINAL.GameServer;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ErrorMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.GameFieldMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.UpdateMessage;

import java.rmi.RemoteException;

public class SendDrawGold implements SendFunction{
    String token;
    public SendDrawGold( String token) {
        this.token = token;
    }

    @Override
    public ResponseMessage action(GameServer server) throws RemoteException {
        ResponseMessage message;
        try{
            server.peachFromGoldDeck(token);
            message = new UpdateMessage("Card inserted!");
            server.token_manager.getTokens().get(token).setCards(server.token_to_player.get(token).getCardsInHand());
            for (String t : server.token_to_player.keySet()){
                server.token_manager.getTokens().get(t).setState( server.token_to_player.get(t).getActual_state().getNameState() );
            }
        }catch(ControllerException e){
            message = new ErrorMessage(server.token_to_player.get(token).getName() +
                    e.getMessage());
        }
        return message;
    }
}
