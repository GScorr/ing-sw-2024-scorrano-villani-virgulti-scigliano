package it.polimi.ingsw.MODEL.Goal;

import CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

public class GoalLFigure implements GoalStrategy {
    int counter;
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        switch (resource) {
            case INSECTS:
                for (int i = 0; i < Constants.MATRIXDIM - 4; i++) {
                    for (int j = 0; j < Constants.MATRIXDIM - 4; j++) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(AnglesEnum.ANIMAL)) {
                            if (field.getCell(i + 2, j + 2, Constants.MATRIXDIM).getCard().equals(AnglesEnum.INSECTS)) {
                                if (field.getCell(i + 4, j + 4, Constants.MATRIXDIM).getCard().equals(AnglesEnum.INSECTS)) {
                                    counter++;
                                }
                            }
                        }
                    }
                }
                break;
            case PLANT:
                for (int i = 0; i < Constants.MATRIXDIM - 4; i++) {
                    for (int j = 0; j < Constants.MATRIXDIM - 4; j++) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(AnglesEnum.PLANT)) {
                            if (field.getCell(i + 2, j + 2, Constants.MATRIXDIM).getCard().equals(AnglesEnum.PLANT)) {
                                if (field.getCell(i + 4, j + 4, Constants.MATRIXDIM).getCard().equals(AnglesEnum.PLANT)) {
                                    counter++;
                                }
                            }
                        }
                    }
                }
                break;
            case MUSHROOMS:
                for (int i = 0; i < Constants.MATRIXDIM - 4; i++) {
                    for (int j = 0; j < Constants.MATRIXDIM - 4; j++) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(AnglesEnum.MUSHROOMS)) {
                            if (field.getCell(i + 2, j + 2, Constants.MATRIXDIM).getCard().equals(AnglesEnum.MUSHROOMS)) {
                                if (field.getCell(i + 4, j + 4, Constants.MATRIXDIM).getCard().equals(AnglesEnum.MUSHROOMS)) {
                                    counter++;
                                }
                            }
                        }
                    }
                }
                break;
            case ANIMAL:
                for (int i = 0; i < Constants.MATRIXDIM - 4; i++) {
                    for (int j = 0; j < Constants.MATRIXDIM - 4; j++) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(AnglesEnum.ANIMAL)) {
                            if (field.getCell(i + 2, j + 2, Constants.MATRIXDIM).getCard().equals(AnglesEnum.ANIMAL)) {
                                if (field.getCell(i + 4, j + 4, Constants.MATRIXDIM).getCard().equals(AnglesEnum.ANIMAL)) {
                                    counter++;
                                }
                            }
                        }
                    }
                }
                break;
        }
    }
}
