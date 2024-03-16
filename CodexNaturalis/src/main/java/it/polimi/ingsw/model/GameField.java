package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ENUM.AnglesEnum;

//Class for the matrix field of each player
/*@Davide
* TODO:
*  - implementa metodo check placeable
*  - implementa metodo add e sub risorse
*  - gestione errori
* */
public class GameField {
    private GameFieldSingleCell[][] field;
    private int num_of_animal;
    private int num_of_mushroom;
    private int num_of_plant;
    private int num_of_insect;
    private int num_of_paper;
    private int num_of_pen;
    private int num_of_feather;


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

    public PlayCard getCardByPosition(int x, int y, int dim){
        if( ( x < 0 || x > dim ) || ( y < 0 || y > dim ) ) {System.out.print("ERROR: INDEX EXCEED MATRIX DIMENSION"); return null;}
        return field[x][y].getCard();
    }

    //Insert of a card, it checks if the card can be placed and updates the resources counter and
    // it changes the values of the matrix so that they are coherent with the new placed card
    public void insertCard(PlayCard card, int x, int y){

        if( !checkPlacing( x, y ) ) System.out.println("ERROR: YOU CANT PLACE THE CARD HERE");

        resourceCountChange(x, y);

        field[x][y].setFilled(true);
        field[x][y].setCard(card);
        field[x][y].setValue( card.getSide().angle_left_up );

        field[x+1][y].setFilled(true);
        field[x+1][y].setCard(card);
        field[x+1][y].setValue( card.getSide().angle_right_up );

        field[x][y+1].setFilled(true);
        field[x][y+1].setCard(card);
        field[x][y+1].setValue( card.getSide().angle_left_down );

        field[x+1][y+1].setFilled(true);
        field[x+1][y+1].setCard(card);
        field[x+1][y+1].setValue( card.getSide().angle_right_down );

    }

    public int addOne(int val){
        return val+1;
    }
    public int subOne(int val){
        return val-1;
    }


    /*
    * */
    public boolean checkPlacing(int x, int y){
        return false;
        }
    public void resourceCountChange(int x, int y){
        return ;
    }
}
