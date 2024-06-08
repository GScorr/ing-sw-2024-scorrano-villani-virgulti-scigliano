package it.polimi.ingsw.MODEL;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;
import it.polimi.ingsw.MODEL.ENUM.EdgeEnum;
import it.polimi.ingsw.MODEL.Player.Player;

import java.io.Serializable;

//Class for the matrix field of each player
/*@Davide   XXX= DONE
* TODO:
*  - finisci metodo check placeable       XXX
*  - implementa metodo add e sub risorse  XXX
*  - gestione errori
*  - implementa gestione punti per carte risorsa e non XXX
* */
public class GameField implements Serializable {
    public int card_inserted = 0;
    private GameFieldSingleCell[][] field;
    private Player player;
    private int num_of_animal;
    private int num_of_mushroom;
    private int num_of_plant;
    private int num_of_insect;
    private int num_of_paper;
    private int num_of_pen;
    private int num_of_feather;

    public GameField(GameFieldSingleCell[][] field, Player player) {
        this.field = field;
        this.player = player;
    }

    public void setField(GameFieldSingleCell[][] field) {
        this.field = field;
    }
    public GameFieldSingleCell[][] getField() {
        return field;
    }

    //Get a single cell given index X,Y and dimension of the Matrix
    public GameFieldSingleCell getCell(int x, int y, int dim){
        if( ( x < 0 || x > dim ) || ( y < 0 || y > dim ) ) {System.out.print("ERROR: INDEX EXCEED MATRIX DIMENSION"); return null;}
        return field[x][y];
    }

    public int getNumOf(AnglesEnum val){
        switch ( val ){
            case ANIMAL:
                return num_of_animal;
            case MUSHROOMS:
                return num_of_mushroom;
            case PLANT:
                return num_of_plant;
            case INSECTS:
                return num_of_insect;
            case PEN:
                return num_of_pen;
            case PAPER:
                return num_of_paper;
            case FEATHER:
                return num_of_feather;

        }return 0;
    }
    public void setNumOfAnimal(int num_of_animal) {
        this.num_of_animal = num_of_animal;
    }
    public int getNumOfAnimal() {
        return num_of_animal;
    }
    public void setNumOfMushroom(int num_of_mushroom) {
        this.num_of_mushroom = num_of_mushroom;
    }
    public int getNumOfMushroom() {
        return num_of_mushroom;
    }
    public void setNumOfInsect(int num_of_insect) {
        this.num_of_insect = num_of_insect;
    }
    public int getNumOfInsect() {
        return num_of_insect;
    }
    public void setNumOfPlant(int num_of_plant) {
        this.num_of_plant = num_of_plant;
    }
    public int getNumOfPlant() {
        return num_of_plant;
    }
    public void setNumOfPaper(int num_of_paper) {
        this.num_of_paper = num_of_paper;
    }
    public int getNumOfPaper() {
        return num_of_paper;
    }
    public void setNumOfPen(int num_of_pen) {
        this.num_of_pen = num_of_pen;
    }
    public int getNumOfPen() {
        return num_of_pen;
    }
    public void setNumOfFeather(int num_of_feather) {
        this.num_of_feather = num_of_feather;
    }
    public int getNumOfFeather() {
        return num_of_feather;
    }

    public Player getPlayer(){return  this.player;}

    //Insert of a card, it checks if the card can be placed and updates the resources counter and
    // it changes the values of the matrix so that they are coherent with the new placed card
    public boolean insertCard(PlayCard card, int x, int y){
        card_inserted++;

        if( !field[x][y].isEmpty() ){  //ho cambiato .getCard con .getCardDown, penso che l'errore sia qua
            field[x][y].setCardDown(field[x][y].getCard());
            field[x][y].setOrder_below(field[x][y].getOrder_above());
            field[x][y].setValues( card.getSide().getAngleLeftUp(), 1); //probably useless, but I'm waiting for the Gui_Initialization
            field[x][y].setEdges( EdgeEnum.LEFTUP, 1); //probably useless, but I'm waiting for the Gui_Initialization
            //System.out.println("cella base");
            //System.out.println(field[x][y].getCard().getSide().getAngleLeftDown());
        }
        if( !field[x+1][y].isEmpty() ){
            field[x+1][y].setCardDown(field[x+1][y].getCard());
            field[x+1][y].setOrder_below(field[x+1][y].getOrder_above());
            field[x+1][y].setValues( card.getSide().getAngleLeftDown(), 1);
            field[x+1][y].setEdges( EdgeEnum.LEFTDOWN, 1);
            //System.out.println("cella riga sotto");
            //System.out.println(field[x][y].getCard().getSide().getAngleLeftDown());
        }
        if( !field[x][y+1].isEmpty() ){
            field[x][y+1].setCardDown(field[x][y+1].getCard());
            field[x][y+1].setOrder_below(field[x][y+1].getOrder_above());
            field[x][y+1].setValues( card.getSide().getAngleRightUp(), 1);
            field[x][y+1].setEdges( EdgeEnum.RIGHTUP, 1);
            //System.out.println("cella colonna a dx");
            //System.out.println(field[x][y].getCard().getSide().getAngleRightUp());
        }
        if( !field[x+1][y+1].isEmpty() ){
            field[x+1][y+1].setCardDown(field[x+1][y+1].getCard());
            field[x+1][y+1].setOrder_below(field[x+1][y+1].getOrder_above());
            field[x+1][y+1].setValues( card.getSide().getAngleRightDown(), 1 );
            field[x+1][y+1].setEdges( EdgeEnum.RIGHTDOWN, 1);
            //System.out.println("cella colonna + riga +1");
            //System.out.println(field[x][y].getCard().getSide().getAngleRightDown());
        }

        //insert card in the 4 cells
        field[x][y].setFilled(true);
        field[x][y].setCard(card);
        field[x][y].setOrder_above(card_inserted);
        field[x][y].setValue( card.getSide().getAngleLeftUp() );
        field[x][y].setValues( card.getSide().getAngleLeftUp(), 0 );
        field[x][y].setEdges( EdgeEnum.LEFTUP, 0);

        field[x+1][y].setFilled(true);
        field[x+1][y].setCard(card);
        field[x+1][y].setOrder_above(card_inserted);
        //field[x+1][y].setValue( card.getSide().getAngleRightUp() );
        field[x+1][y].setValue( card.getSide().getAngleLeftDown() );
        field[x+1][y].setValues( card.getSide().getAngleLeftDown(), 0 );
        field[x+1][y].setEdges( EdgeEnum.LEFTDOWN, 0);


        field[x][y+1].setFilled(true);
        field[x][y+1].setCard(card);
        field[x][y+1].setOrder_above(card_inserted);
        //field[x][y+1].setValue( card.getSide().getAngleLeftDown() ); erano invertiti gli angoli che salvava -mirko-
        field[x][y+1].setValue( card.getSide().getAngleRightUp() );
        field[x][y+1].setValues( card.getSide().getAngleRightUp(), 0 );
        field[x][y+1].setEdges( EdgeEnum.RIGHTUP, 0);

        field[x+1][y+1].setFilled(true);
        field[x+1][y+1].setCard(card);
        field[x+1][y+1].setOrder_above(card_inserted);
        field[x+1][y+1].setValue( card.getSide().getAngleRightDown() );
        field[x+1][y+1].setValues( card.getSide().getAngleRightDown(), 0 );
        field[x+1][y+1].setEdges( EdgeEnum.RIGHTDOWN, 0);
        return true;
    }

    public void cardNotPlaceable(){};

    public void addOne(CentralEnum val){
        switch ( val ){
            case ANIMAL:
                num_of_animal++; break;
            case MUSHROOMS:
                num_of_mushroom++; break;
            case PLANT:
                num_of_plant++; break;
            case INSECTS:
                num_of_insect++; break;
            case NONE:
                break;
        }
    }
    public void addOne(AnglesEnum val){
        switch ( val ){
            case ANIMAL:
                num_of_animal++; break;
            case MUSHROOMS:
                num_of_mushroom++; break;
            case PLANT:
                num_of_plant++; break;
            case INSECTS:
                num_of_insect++; break;
            case PEN:
                num_of_pen++; break;
            case PAPER:
                num_of_paper++; break;
            case FEATHER:
                num_of_feather++; break;
            case NONE:
            case EMPTY:
                break;
        }
    }
    public void subOne(AnglesEnum val){
        switch ( val ){
            case ANIMAL:
                num_of_animal--; break;
            case MUSHROOMS:
                num_of_mushroom--; break;
            case PLANT:
                num_of_plant--; break;
            case INSECTS:
                num_of_insect--; break;
            case PEN:
                num_of_pen--; break;
            case PAPER:
                num_of_paper--; break;
            case FEATHER:
                num_of_feather--; break;
        }
    }


}










/* Equivalent if
if( ( !field[x][y].isEmpty() && field[x][y].getValue().equals( AnglesEnum.NONE) ) ||
        ( !field[x+1][y].isEmpty() && field[x+1][y].getValue().equals( AnglesEnum.NONE) ) ||
        ( !field[x][y+1].isEmpty() && field[x][y+1].getValue().equals( AnglesEnum.NONE) ) ||
        ( !field[x+1][y+1].isEmpty() && field[x+1][y+1].getValue().equals( AnglesEnum.NONE) ) ) return false;
        return true;
 */