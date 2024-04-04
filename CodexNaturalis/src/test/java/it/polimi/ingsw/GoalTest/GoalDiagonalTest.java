package it.polimi.ingsw.GoalTest;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Goal.GoalDiagonal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class GoalDiagonalTest {
    Side s1 = new Side(AnglesEnum.EMPTY,AnglesEnum.EMPTY,AnglesEnum.EMPTY,AnglesEnum.EMPTY, CentralEnum.PLANT,CentralEnum.NONE,CentralEnum.NONE);
    Side s2 = new Side(AnglesEnum.EMPTY,AnglesEnum.EMPTY,AnglesEnum.EMPTY,AnglesEnum.EMPTY, CentralEnum.PLANT,CentralEnum.NONE,CentralEnum.NONE);
    Side s3 = new Side(AnglesEnum.EMPTY,AnglesEnum.EMPTY,AnglesEnum.EMPTY,AnglesEnum.EMPTY, CentralEnum.PLANT,CentralEnum.NONE,CentralEnum.NONE);
    Side s4 = new Side(AnglesEnum.EMPTY,AnglesEnum.EMPTY,AnglesEnum.EMPTY,AnglesEnum.EMPTY, CentralEnum.PLANT,CentralEnum.NONE,CentralEnum.NONE);
    Side s5 = new Side(AnglesEnum.NONE,AnglesEnum.NONE,AnglesEnum.NONE,AnglesEnum.NONE,CentralEnum.NONE,CentralEnum.NONE,CentralEnum.NONE);
    PlayCard c1 = new ResourceCard(s1,s2,true,0);
    PlayCard c2 = new ResourceCard(s1,s2,true,0);
    PlayCard c3 = new ResourceCard(s3,s4,true,0);
    PlayCard c4 = new ResourceCard(s3,s4,true,0);
    PlayCard fake = new ResourceCard(s5,s5,true,0);
    GameFieldSingleCell cell1= new GameFieldSingleCell(true,c1,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell2 = new GameFieldSingleCell(true,c1,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell3 = new GameFieldSingleCell(false,fake,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell4 = new GameFieldSingleCell(false,fake,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell5 = new GameFieldSingleCell(true,c1,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell6 = new GameFieldSingleCell(true,c1,AnglesEnum.NONE,c2);
    GameFieldSingleCell cell7 = new GameFieldSingleCell(true,c2,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell8 = new GameFieldSingleCell(false,fake,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell9 = new GameFieldSingleCell(false,fake,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell10 = new GameFieldSingleCell(true,c2,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell11 = new GameFieldSingleCell(true,c2,AnglesEnum.NONE,c3);
    GameFieldSingleCell cell12 = new GameFieldSingleCell(true,c3,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell13 = new GameFieldSingleCell(false,fake,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell14 = new GameFieldSingleCell(false,fake,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell15 = new GameFieldSingleCell(true,c3,AnglesEnum.NONE,fake);
    GameFieldSingleCell cell16 = new GameFieldSingleCell(true,c3,AnglesEnum.NONE,fake);
    GameFieldSingleCell[][] campo = new GameFieldSingleCell[4][4];
    GoalDiagonal objdiag;
    Goal obj= new Goal(objdiag,1,AnglesEnum.PLANT);





    @Test
    public void testGoalDiagonal(){
        campo[0][0] = cell1;
        campo[0][1] = cell2;
        campo[0][2] = cell3;
        campo[0][3] = cell4;
        campo[1][0] = cell5;
        campo[1][1] = cell6;
        campo[1][2] = cell7;
        campo[1][3] = cell8;
        campo[2][0] = cell9;
        campo[2][1] = cell10;
        campo[2][2] = cell11;
        campo[2][3] = cell12;
        campo[3][0] = cell13;
        campo[3][1] = cell14;
        campo[3][2] = cell15;
        campo[3][3] = cell16;
        GameField tavolo = new GameField(campo);
        int tot = obj.numPoints(tavolo);
        assertEquals(1, tot);
    }
}
