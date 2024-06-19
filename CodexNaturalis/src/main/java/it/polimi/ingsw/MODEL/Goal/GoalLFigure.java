package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.GameField;

import java.io.Serializable;

/*
    todo
        capire cosa fare dei casi commentati
        come commentare la classe
        toglierei i commenti di stampa
 */


/**
 * find the
 */
public class GoalLFigure implements GoalStrategy, Serializable {

    int counter;

    /**
     *
     * @param field the player's game field
     * @param points the base point value for each resource in the minimum set
     * @param resource
     * @return the total number of points earned based on the minimum resource count
     */
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        //System.out.println("sono nella classe");
        switch (resource) {
            //used the lower-left edge of the lowest purple card as a pointer
            case INSECTS:
                for (int i = 4; i < Constants.MATRIXDIM; i++) {
                    for (int j = 1; j < Constants.MATRIXDIM - 1; j++) {
                        if ((field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i-1, j+1, Constants.MATRIXDIM).getCard()) && field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.INSECTS)) ||
                                (field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i-1, j+1, Constants.MATRIXDIM).getCard()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.INSECTS)) ||
                                (field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i-1, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.INSECTS)) ||
                                (field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i-1, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.INSECTS))) {
                            //System.out.println("sono nel primo if");
                            if ((field.getCell(i-2, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i-3, j+1, Constants.MATRIXDIM).getCard()) && field.getCell(i-2, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.INSECTS)) ||
                                    (field.getCell(i-2, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i-3, j+1, Constants.MATRIXDIM).getCard()) && field.getCell(i-2, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.INSECTS)) ||
                                    (field.getCell(i-2, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i-3, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i-2, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.INSECTS)) ||
                                    (field.getCell(i-2, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i-3, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i-2, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.INSECTS))) {
                                //System.out.println("sono nel 2 if");
                                if ((field.getCell(i - 3, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL) && field.getCell(i - 3, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.INSECTS)) ||
                                        (field.getCell(i - 3, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.INSECTS) && field.getCell(i - 3, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL))) {
                                    counter++;
                                }
                            }
                        }
                    }
                }


                break;
            //used the upper-left edge of the uppermost green card as a pointer
            case PLANT:
            for (int i = 0; i < Constants.MATRIXDIM - 4; i++) {
                for (int j = 1; j < Constants.MATRIXDIM - 1; j++) {
                    if ((field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i + 1, j + 1, Constants.MATRIXDIM).getCard()) && field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT)) ||
                            (field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i + 1, j + 1, Constants.MATRIXDIM).getCard()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT)) ||
                            (field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i + 1, j + 1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT)) ||
                            (field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i + 1, j + 1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT))) {
                        if ((field.getCell(i + 2, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i + 3, j + 1, Constants.MATRIXDIM).getCard()) && field.getCell(i + 2, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT)) ||
                                (field.getCell(i + 2, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i + 3, j + 1, Constants.MATRIXDIM).getCard()) && field.getCell(i + 2, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT)) ||
                                (field.getCell(i + 2, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i + 3, j + 1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i + 2, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT)) ||
                                (field.getCell(i + 2, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i + 3, j + 1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i + 2, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT))) {
                            if ((field.getCell(i + 3, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.INSECTS) && field.getCell(i + 3, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT)) ||
                                    (field.getCell(i + 3, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT) && field.getCell(i + 3, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.INSECTS))) {
                                counter++;
                            }
                        }
                    }
                }
            }
            break;
            //used the upper-left edge of the uppermost red card as a pointer
            case MUSHROOMS:
                for (int i = 0; i < Constants.MATRIXDIM - 4; i++) {
                    for (int j = 0; j < Constants.MATRIXDIM - 2; j++) {
                        if ((field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j+1, Constants.MATRIXDIM).getCard())&& field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS))||
                                ( field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j+1, Constants.MATRIXDIM).getCard()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS) )||
                                ( field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS) )||
                                (field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+1, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS))) {
                            //System.out.println("sono nel primo if");
                            if ((field.getCell(i+2, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i+3, j+1, Constants.MATRIXDIM).getCard())&& field.getCell(i+2, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS))||
                                    ( field.getCell(i+2, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+3, j+1, Constants.MATRIXDIM).getCard()) && field.getCell(i+2, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS) )||
                                    ( field.getCell(i+2, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i+3, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i+2, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS) )||
                                    (field.getCell(i+2, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i+3, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i+2, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS))) {
                                //System.out.println("sono nel 2 if");
                                if ((field.getCell(i+3, j+1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS) && field.getCell(i+3, j+1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.PLANT)) ||
                                        (field.getCell(i+3, j+1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.PLANT) && field.getCell(i+3, j+1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS))) {
                                    //System.out.println("sono nel 3 if");
                                    counter++;
                                }
                            }
                        }
                    }
                }
                break;
            //used the lower-left edge of the lowest blue card as a pointer
            case ANIMAL:
                //System.out.println("sono nel case giusto");
                for (int i = 4; i < Constants.MATRIXDIM; i++) {
                    for (int j = 0; j < Constants.MATRIXDIM-2; j++) {
                        if ((field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i-1, j+1, Constants.MATRIXDIM).getCard())&& field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL))||
                                ( field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i-1, j+1, Constants.MATRIXDIM).getCard()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL) )||
                                ( field.getCell(i, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i-1, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL) )||
                                (field.getCell(i, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i-1, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL))) {
                            //System.out.println("sono nel primo if");
                            if ((field.getCell(i-2, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i-3, j+1, Constants.MATRIXDIM).getCard())&& field.getCell(i-2, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL))||
                                    ( field.getCell(i-2, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i-3, j+1, Constants.MATRIXDIM).getCard()) && field.getCell(i-2, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL) )||
                                    ( field.getCell(i-2, j, Constants.MATRIXDIM).getCard().equals(field.getCell(i-3, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i-2, j, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL) )||
                                    (field.getCell(i-2, j, Constants.MATRIXDIM).getCardDown().equals(field.getCell(i-3, j+1, Constants.MATRIXDIM).getCardDown()) && field.getCell(i-2, j, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL))) {
                                //System.out.println("sono nel secondo if");
                                if ((field.getCell(i-3, j+1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.MUSHROOMS) && field.getCell(i-3, j+1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.ANIMAL)) ||
                                        (field.getCell(i-3, j +1, Constants.MATRIXDIM).getCard().getColore().equals(CentralEnum.ANIMAL) && field.getCell(i-3, j+1, Constants.MATRIXDIM).getCardDown().getColore().equals(CentralEnum.MUSHROOMS))) {
                                    //System.out.println("sono nel terzo if");
                                    counter++;
                                }
                            }
                        }
                    }
                }
                break;
        }
        return points*counter;
    }

}
