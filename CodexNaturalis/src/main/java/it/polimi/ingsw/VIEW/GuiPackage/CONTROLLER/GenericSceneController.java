package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;

import java.io.IOException;

/**
 * Abstract base class for scene controllers in the graphical user interface (GUI).
 *
 *
 * @author (assuming package structure) package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER
 * @since (version not specified)
 */
public abstract class GenericSceneController {

    VirtualViewF client;
    SceneController scene_controller;

    public void setClient(VirtualViewF client){
        this.client = client;
    }

    public String returnString(){return null;}

    public void setController (SceneController scene){
        this.scene_controller = scene;
    }

    public void emptyField(){}

    public void startInitialize() throws IOException, ClassNotFoundException, InterruptedException{};
}
