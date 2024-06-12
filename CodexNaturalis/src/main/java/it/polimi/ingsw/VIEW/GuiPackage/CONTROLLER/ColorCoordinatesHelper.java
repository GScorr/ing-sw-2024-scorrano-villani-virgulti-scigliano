package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.Player.Player;
import javafx.scene.paint.Color;

public class ColorCoordinatesHelper {
    public Color fromEnumtoColor (ColorsEnum color){
        if (color.equals(ColorsEnum.RED)){
            return Color.RED;
        }
        if (color.equals(ColorsEnum.BLU)){
            return Color.BLUE;
        }
        if (color.equals(ColorsEnum.YELLOW)){
            return Color.YELLOW;
        }
        if (color.equals(ColorsEnum.GREEN)){
            return Color.GREEN;
        }
        return null;
    }

    public int fromPointstoX(Player p){
        if(p.getPlayerPoints()==0){
            return 80;
        }
        if(p.getPlayerPoints()==1){
            return 155;
        }
        if(p.getPlayerPoints()==2){
            return 230;
        }
        if(p.getPlayerPoints()==18){
            return 270;
        }
        if(p.getPlayerPoints()==20){
            return 160;
        }
        if(p.getPlayerPoints()==23){

        }
        return 0;
    }

    public int fromPointstoY(Player p){
        if(p.getPlayerPoints()==0
        ||p.getPlayerPoints()==1
        ||p.getPlayerPoints()==2){
            return 585;
        }
        if(p.getPlayerPoints()==18){
            return 315;
        }
        if(p.getPlayerPoints()==20){
            return 215;
        }
        if(p.getPlayerPoints()==23){

        }
        return 0;
    }
}
