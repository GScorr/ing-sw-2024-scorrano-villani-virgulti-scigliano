package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;

public class CheckNameMessage implements Message, Serializable {
    public Controller controller;
    public Server server;
    VirtualServerF rmi_server;
    ObjectOutputStream output;


    public String nome;

    boolean check;
    public CheckNameMessage(String nome){
        this.nome = nome;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void setRmiServer(VirtualServerF rmi_server) {
        this.rmi_server = rmi_server;
    }

    public String checkNameMessageAction() throws RemoteException {
        if(rmi_server.checkName(nome).compareTo("true") == 0) {
          //  return rmi_server.createToken(nome);
            return "token";
        }else{
            return "nome_non_disponibilie";
        }

    }

    @Override
    public void action() throws IOException {
        if(controller == null || server == null){
            server.broadcastUpdate("Qualcosa è andato storto nell'invio del messaggio, controller == null || server == null");
            return;
        }


        if(!server.names.contains(nome)){
            server.names.add(nome);
            MyMessageFinal response = new MyMessageFinal("TRUE");
            output.writeObject(response);
            output.flush();
        }else{
            MyMessageFinal response = new MyMessageFinal("FALSE");
            output.writeObject(response);
            output.flush();
        }




    }
}
