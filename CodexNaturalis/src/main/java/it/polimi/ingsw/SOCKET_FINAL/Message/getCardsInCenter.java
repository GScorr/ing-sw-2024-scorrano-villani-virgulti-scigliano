package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.RMI_FINAL.VirtualRmiController;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class getCardsInCenter implements Message, Serializable {

    public Server server;
    public String token;
    ObjectOutputStream output;
    public VirtualServerF rmi_server;
    public VirtualRmiController rmi_controller;


    @Override
    public void setRmiController(VirtualRmiController rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setRmiServer(VirtualServerF rmi_server) {
        this.rmi_server = rmi_server;
    }


    public getCardsInCenter(){

    }

    public void setToken(String token) {
        this.token = token;
    }



    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void action() throws IOException {
        List<PlayCard> golds_cards_in_center = rmi_controller.getController().getGame().getCars_in_center().getGold_list();
        List<PlayCard> resources_cards_in_center = rmi_controller.getController().getGame().getCars_in_center().getResource_list();
        // Combine elements of both lists using Stream API (Java 8+)
        List<PlayCard> cards_in_center = Stream.concat(golds_cards_in_center.stream(), resources_cards_in_center.stream())
                .collect(Collectors.toList());
        output.writeObject(cards_in_center);
        output.flush();

    }
}
