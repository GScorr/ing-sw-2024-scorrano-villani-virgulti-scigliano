package it.polimi.ingsw.MODEL.Goal;

import CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.GameField;

public class GoalLFigure implements GoalStrategy {
    int counter;
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        switch (resource) {
            //used the lower-left edge of the lowest purple card as a pointer
            case INSECTS:
                for (int i = 1; i < Constants.MATRIXDIM - 1; i++) {
                    for (int j = 4; j < Constants.MATRIXDIM ; j++) {
                        if ((field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j-1, Constants.MATRIXDIM).getCard())&& field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT))||
                                ( field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j-1, Constants.MATRIXDIM).getCard()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT) )||
                                ( field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j-1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT) )||
                                (field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j-1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT))) {
                            if ((field.getCell(i, j-2, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j-3, Constants.MATRIXDIM).getCard())&& field.getCell(i, j-2, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT))||
                                    ( field.getCell(i, j-2, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j-3, Constants.MATRIXDIM).getCard()) && field.getCell(i, j-2, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT) )||
                                    ( field.getCell(i, j-2, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j-3, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j-2, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT) )||
                                    (field.getCell(i, j-2, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j-3, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j-2, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT))) {
                                if ((field.getCell(i, j - 3, Constants.MATRIXDIM).getCard().equals(CentralEnum.MUSHROOMS) && field.getCell(i, j - 3, Constants.MATRIXDIM).getCardDown().equals(CentralEnum.PLANT)) ||
                                        (field.getCell(i, j - 3, Constants.MATRIXDIM).getCard().equals(CentralEnum.PLANT) && field.getCell(i, j - 3, Constants.MATRIXDIM).getCard().equals(CentralEnum.MUSHROOMS))) {
                                    counter++;
                                }
                            }
                        }
                    }
                }
                break;
            //used the upper-left edge of the uppermost green card as a pointer
            case PLANT:
                for (int i = 1; i < Constants.MATRIXDIM - 1; i++) {
                    for (int j = 0; j < Constants.MATRIXDIM - 4; j++) {
                        if ((field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j+1, Constants.MATRIXDIM).getCard())&& field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT))||
                                ( field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j+1, Constants.MATRIXDIM).getCard()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT) )||
                                ( field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT) )||
                                (field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT))) {
                            if ((field.getCell(i, j+2, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j+3, Constants.MATRIXDIM).getCard())&& field.getCell(i, j+2, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT))||
                                    ( field.getCell(i, j+2, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j+3, Constants.MATRIXDIM).getCard()) && field.getCell(i, j+2, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT) )||
                                    ( field.getCell(i, j+2, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j+3, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j+2, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT) )||
                                    (field.getCell(i, j+2, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j+3, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j+2, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT))) {
                                if ((field.getCell(i, j + 3, Constants.MATRIXDIM).getCard().equals(CentralEnum.INSECTS) && field.getCell(i, j + 3, Constants.MATRIXDIM).getCardDown().equals(CentralEnum.PLANT)) ||
                                        (field.getCell(i, j + 3, Constants.MATRIXDIM).getCard().equals(CentralEnum.PLANT) && field.getCell(i, j + 3, Constants.MATRIXDIM).getCard().equals(CentralEnum.INSECTS))) {
                                    counter++;
                                }
                            }
                        }
                    }
                }
                break;
            //used the upper-left edge of the uppermost red card as a pointer
            case MUSHROOMS:
                for (int i = 0; i < Constants.MATRIXDIM - 2; i++) {
                    for (int j = 0; j < Constants.MATRIXDIM - 4; j++) {
                        if ((field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j+1, Constants.MATRIXDIM).getCard())&& field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS))||
                                ( field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j+1, Constants.MATRIXDIM).getCard()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS) )||
                                ( field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS) )||
                                (field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS))) {
                            if ((field.getCell(i, j+2, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j+2, Constants.MATRIXDIM).getCard())&& field.getCell(i, j+2, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS))||
                                    ( field.getCell(i, j+2, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j+3, Constants.MATRIXDIM).getCard()) && field.getCell(i, j+2, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS) )||
                                    ( field.getCell(i, j+2, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j+3, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j+2, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS) )||
                                    (field.getCell(i, j+2, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j+3, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j+2, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS))) {
                                if ((field.getCell(i + 1, j + 3, Constants.MATRIXDIM).getCard().equals(CentralEnum.MUSHROOMS) && field.getCell(i + 1, j + 3, Constants.MATRIXDIM).getCardDown().equals(CentralEnum.PLANT)) ||
                                        (field.getCell(i + 1, j + 3, Constants.MATRIXDIM).getCard().equals(CentralEnum.PLANT) && field.getCell(i + 1, j + 3, Constants.MATRIXDIM).getCard().equals(CentralEnum.MUSHROOMS))) {
                                    counter++;
                                }
                            }
                        }
                    }
                }
                break;
            //used the lower-left edge of the lowest blue card as a pointer
            case ANIMAL:
                for (int i = 0; i < Constants.MATRIXDIM-2; i++) {
                    for (int j = 4; j < Constants.MATRIXDIM; j++) {
                        if ((field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j-1, Constants.MATRIXDIM).getCard())&& field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL))||
                                ( field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j-1, Constants.MATRIXDIM).getCard()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL) )||
                                ( field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j-1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL) )||
                                (field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j-1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL))) {
                            if ((field.getCell(i, j-2, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j-3, Constants.MATRIXDIM).getCard())&& field.getCell(i, j-2, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL))||
                                    ( field.getCell(i, j-2, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j-3, Constants.MATRIXDIM).getCard()) && field.getCell(i, j-2, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL) )||
                                    ( field.getCell(i, j-2, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j-3, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j-2, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL) )||
                                    (field.getCell(i, j-2, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j-3, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j-2, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL))) {
                                if ((field.getCell(i+1, j - 3, Constants.MATRIXDIM).getCard().equals(CentralEnum.MUSHROOMS) && field.getCell(i+1, j - 3, Constants.MATRIXDIM).getCardDown().equals(CentralEnum.PLANT)) ||
                                        (field.getCell(i+1, j - 3, Constants.MATRIXDIM).getCard().equals(CentralEnum.PLANT) && field.getCell(i+1, j - 3, Constants.MATRIXDIM).getCard().equals(CentralEnum.MUSHROOMS))) {
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
