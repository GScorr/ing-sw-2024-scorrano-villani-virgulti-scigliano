package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.GameField;

import java.io.Serializable;
import java.sql.SQLOutput;

public class GoalDiagonal implements GoalStrategy, Serializable {
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        int counter = 0;
        int tmp1, tmp2;
        switch (resource) {
            case INSECTS:
                //System.out.println("1");
                for (int i = 1; i < Constants.MATRIXDIM - 2; i++) {
                    for (int j = 1; j < Constants.MATRIXDIM - 2; j++) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.INSECTS)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.INSECTS)) {
                            if (field.getCell(i + 1, j + 1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.INSECTS)&&field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.INSECTS)) {
                                counter++;
                            }
                        }
                    }
                }
                break;
            case PLANT:
               // System.out.println("2");
                for (int i = 1; i < Constants.MATRIXDIM - 2; i++) {
                    for (int j = 1; j < Constants.MATRIXDIM - 2; j++) {
                        //System.out.println(field.getCell(i,j,Constants.MATRIXDIM).getCard().getColore()+"sono la carta su");
                        //System.out.println(field.getCell(i,j,Constants.MATRIXDIM).getCardDown().getColore()+"sono la carta giù");
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT)) {
                            if (field.getCell(i + 1, j + 1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT)&&field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT)) {
                                counter++;
                            }
                        }
                    }
                }
                break;
            case MUSHROOMS:
                //System.out.println("3");
                for (int i = 1; i < Constants.MATRIXDIM - 2; i++){
                    for (int j = Constants.MATRIXDIM - 2; j >1 ; j--) {
                        //System.out.println(field.getCell(i,j,Constants.MATRIXDIM).getCard().getColore()+"sono la carta su");
                        //System.out.println(field.getCell(i,j,Constants.MATRIXDIM).getCardDown().getColore()+"sono la carta giù");
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS)) {
                            if (field.getCell(i + 1, j - 1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS)&&field.getCell(i+1, j-1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS)) {
                                //System.out.println("le due intersezioni sono alle posizioni "+ i +"x" + j + " e " + (i+1) + (j-1));
                                counter++;
                            }
                        }
                    }
                }
                break;
            case ANIMAL:
                //System.out.println("4");
                for (int i = 1; i < Constants.MATRIXDIM - 2; i++){
                    for (int j = Constants.MATRIXDIM - 2; j >1 ; j--) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL)) {
                            if (field.getCell(i + 1, j - 1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL)&&field.getCell(i+1, j-1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL)) {
                                counter++;
                            }
                        }
                    }
                }
                break;
        }
        return counter*points;
    }
}
