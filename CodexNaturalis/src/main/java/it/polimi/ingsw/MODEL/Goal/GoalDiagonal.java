package it.polimi.ingsw.MODEL.Goal;

import CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.GameField;

public class GoalDiagonal implements GoalStrategy {
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        int counter = 0;
        int tmp1, tmp2;
        switch (resource) {
            case INSECTS:
                for (int i = 1; i < Constants.MATRIXDIM - 1; i++) {
                    for (int j = 1; j < Constants.MATRIXDIM - 1; j++) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.INSECTS)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.INSECTS)) {
                            if (field.getCell(i + 1, j + 1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.INSECTS)&&field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.INSECTS)) {
                                counter++;
                            }
                        }
                    }
                }
                break;
            case PLANT:
                for (int i = 1; i < Constants.MATRIXDIM - 1; i++) {
                    for (int j = 1; j < Constants.MATRIXDIM - 1; j++) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT)) {
                            if (field.getCell(i + 1, j + 1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT)&&field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT)) {
                                counter++;
                            }
                        }
                    }
                }
                break;
            case MUSHROOMS:
                for (int i = 1; i < Constants.MATRIXDIM - 1; i++){
                    for (int j = Constants.MATRIXDIM - 2; j >=0 ; j--) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS)) {
                            if (field.getCell(i + 1, j - 1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS)&&field.getCell(i+1, j-1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS)) {
                                counter++;
                            }
                        }
                    }
                }
        case ANIMAL:
            for (int i = 1; i < Constants.MATRIXDIM - 1; i++){
                for (int j = Constants.MATRIXDIM - 2; j >=0 ; j--) {
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
