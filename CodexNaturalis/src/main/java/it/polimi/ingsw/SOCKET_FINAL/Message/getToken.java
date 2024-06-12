package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess.getTokenResponse;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * This message class likely does not serve a useful purpose in the client-server
 * communication. Consider if the client already possesses the token upon
 * connection or if a different message is needed to handle token generation
 * or retrieval.
 */
public class getToken implements Message, Serializable {

    public Server server;
    public String token;
    ObjectOutputStream output;
    public Common_Server common;
    public VirtualGameServer rmi_controller;

    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setCommonServer(Common_Server common){
        this.common = common;
    }

    public getToken(){}

    public void setToken(String token) {
        this.token = token;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    /**
     * The action() method sends the client's own token back to them
     * (assuming the token field is set).
     *
     * @throws IOException If there is an IO error (should never be thrown).
     */
    @Override
    public void action() throws IOException {
        ResponseMessage message = new getTokenResponse(this.token);
        output.writeObject(message);
        output.flush();
        output.reset();
    }

}
