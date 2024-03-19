package it.polimi.ingsw.MODEL.Goal;

import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.GameField;

public class GoalDiagonal implements GoalStrategy{
    public int totalPoints(GameField field, int points, AnglesEnum resource) {
        switch (resource){
            case AnglesEnum.INSECTS:
                for (int i = 0; i < field.MATRIXDIM; i++) {
                    for(int j = 0; j < field.MATRIXDIM; j++){
                        if (field.getCell(i, j, MATRIXDIM).) {

                        }
                    }
                }
                break;
            case AnglesEnum.PLANT:
                for (int i = 0; i < field.MATRIXDIM; i++) {
                    for(int j = 0; j < field.MATRIXDIM; j++){

                    }
                }
                break;
            case AnglesEnum.MUSHROOMS:
                for (int i = 0; i < field.MATRIXDIM; i++) {
                    for(int j = 0; j < field.MATRIXDIM; j++){

                    }
                }
                break;
            case AnglesEnum.ANIMAL:
                for (int i = 0; i < field.MATRIXDIM; i++) {
                    for(int j = 0; j < field.MATRIXDIM; j++){

                    }
                }
                break;

        }
    }
}
