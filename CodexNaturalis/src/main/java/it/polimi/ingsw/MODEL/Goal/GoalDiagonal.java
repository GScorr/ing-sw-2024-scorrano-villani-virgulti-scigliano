package it.polimi.ingsw.MODEL.Goal;

import CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

public class GoalDiagonal implements GoalStrategy {
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        int counter = 0;
        int tmp1, tmp2;
        switch (resource) {
            case INSECTS:
                for (int i = 1; i < Constants.MATRIXDIM - 1; i++) {
                    for (int j = 1; j < Constants.MATRIXDIM - 1; j++) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(AnglesEnum.INSECTS)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(AnglesEnum.INSECTS)) {
                            if (field.getCell(i + 1, j + 1, Constants.MATRIXDIM).getCard().equals(AnglesEnum.INSECTS)&&field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown().equals(AnglesEnum.INSECTS)) {
                                counter++;
                            }
                        }
                    }
                }
                break;
            case PLANT:
                for (int i = 1; i < Constants.MATRIXDIM - 1; i++) {
                    for (int j = 1; j < Constants.MATRIXDIM - 1; j++) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getValue().equals(AnglesEnum.PLANT)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(AnglesEnum.PLANT)) {
                            if (field.getCell(i + 1, j + 1, Constants.MATRIXDIM).getCard().equals(AnglesEnum.PLANT)&&field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown().equals(AnglesEnum.PLANT)) {
                                counter++;
                            }
                        }
                    }
                }
                break;
            case MUSHROOMS:
                for (int i = 1; i < Constants.MATRIXDIM - 1; i++){
                    for (int j = Constants.MATRIXDIM - 2; j >=0 ; j--) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(AnglesEnum.MUSHROOMS)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(AnglesEnum.MUSHROOMS)) {
                            if (field.getCell(i + 1, j - 1, Constants.MATRIXDIM).getCard().equals(AnglesEnum.MUSHROOMS)&&field.getCell(i+1, j-1, Constants.MATRIXDIM).getCardDown().equals(AnglesEnum.MUSHROOMS)) {
                                counter++;
                            }
                        }
                    }
                }
        case ANIMAL:
            for (int i = 1; i < Constants.MATRIXDIM - 1; i++){
                for (int j = Constants.MATRIXDIM - 2; j >=0 ; j--) {
                    if (field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(AnglesEnum.ANIMAL)&&field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(AnglesEnum.ANIMAL)) {
                        if (field.getCell(i + 1, j - 1, Constants.MATRIXDIM).getCard().equals(AnglesEnum.ANIMAL)&&field.getCell(i+1, j-1, Constants.MATRIXDIM).getCardDown().equals(AnglesEnum.ANIMAL)) {
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
