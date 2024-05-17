package it.polimi.ingsw.RMI_FINAL.FUNCTION;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.RMI_FINAL.GameServer;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ErrorMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.GameFieldMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.SOCKET_FINAL.Message.Message;

import java.rmi.RemoteException;

public class SendInsertCard extends SendFunction{

    public SendInsertCard(GameServer server) {
        super(server);
    }

    @Override
    public ResponseMessage action(String token, int index, int x, int y, boolean flipped) throws RemoteException {
        ResponseMessage message;
        try{
            super.server.insertCard(token, index, x , y, flipped);
             message = new GameFieldMessage(server.token_to_player.get(token).getGameField());
            for (String t : server.token_to_player.keySet()){
                server.token_manager.getTokens().get(t).setGameField(server.getGameFields(t));
            }
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
