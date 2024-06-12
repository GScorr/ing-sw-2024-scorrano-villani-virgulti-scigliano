package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.MODEL.ENUM.ColorsEnum;
import it.polimi.ingsw.MODEL.Player.Player;
import javafx.scene.paint.Color;

public class ColorCoordinatesHelper {

    public Color fromEnumtoColor (ColorsEnum color) {
        switch (color) {
            case RED:
                return Color.RED;
            case BLU:
                return Color.BLUE;
            case YELLOW:
                return Color.YELLOW;
            case GREEN:
                return Color.GREEN;
            default:
                return null;
        }
    }













    public int fromPointstoX(Player p, ColorsEnum color) {
        int x = calculateBaseX(p);
        // Adjust x based on color to avoid overlap
        switch (color) {
            case RED:
                x += 5;
                break;
            case BLU:
                x -= 5;
                break;
            case YELLOW:
                x += 5;
                break;
            case GREEN:
                x -= 5;
                break;
        }
        return x;
    }






    
    public int fromPointstoY(Player p, ColorsEnum color) {
        int y = calculateBaseY(p);
        // Adjust y based on color to avoid overlap
        switch (color) {
            case RED:
                y += 5;
                break;
            case BLU:
                y -= 5;
                break;
            case YELLOW:
                y -= 5;
                break;
            case GREEN:
                y += 5;
                break;
        }
        return y;
    }

    private int calculateBaseX(Player p) {
        switch (p.getPlayerPoints()) {
            case 0: return 80;
            case 1: case 20: case 25: case 29: return 155;
            case 2: return 230;
            case 3: case 10: case 11: case 18: case 19: case 27: case 28: return 270;
            case 4: case 9: case 12: case 17: return 195;
            case 5: case 8: case 13: case 16: return 120;
            case 6: case 7: case 14: case 15: case 21: case 22: case 23: return 45;
            case 24: return 90;
            case 26: return 225;
            default: return 0;
        }
    }

    private int calculateBaseY(Player p) {
        switch (p.getPlayerPoints()) {
            case 0: case 1: case 2: return 585;
            case 3: case 4: case 5: case 6: return 525;
            case 7: case 8: case 9: case 10: return 455;
            case 11: case 12: case 13: case 14: return 385;
            case 15: case 16: case 17: case 18: return 315;
            case 19: case 21: return 245;
            case 22: case 28: return 175;
            case 20: return 215;
            case 23: case 27: return 110;
            case 24: case 26: return 55;
            case 25: return 45;
            case 29: return 130;
            default: return 0;
        }
    }
}