package it.polimi.ingsw.MODEL;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;

//@Davide
// Class for the single cell present in the matrix of GameField
public class GameFieldSingleCell2 {
    /*
    provo a mettere la carta trasparente tc e a considerarla sempre come carta_down,
    inizio carta trasparente
     */
 /*   private final Side tc_front_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    private final Side tc_back_side = new Side(AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, AnglesEnum.EMPTY, CentralEnum.NONE, CentralEnum.NONE, CentralEnum.NONE);
    private final PlayCard tc = new ResourceCard(tc_front_side, tc_back_side,false, 0);
    //fine carta trasparente
*/
    private boolean filled;
    private PlayCard card;
    private AnglesEnum value;
    private PlayCard card_down;
    //card_down means the angles that the card will cover -mirko-
    public GameFieldSingleCell2(boolean filled, PlayCard card, AnglesEnum value, PlayCard card_down) {
        this.filled = filled;
        this.card = card;
        this.value = value;
        this.card_down = card_down;
    }
    public boolean isEmpty() {
        return !filled;
    }

    public boolean isFilled() { //ritorna 1 se c'è la carta metodo aggiunto -mirko-
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }
    public PlayCard getCard() {
        if(!isFilled() && getValue().equals(AnglesEnum.EMPTY)){ //check se ho tc come carta up
            //System.out.println("carta trasparente nel getCard");
            return card; //non sono certo che sia da fare così
            //ritorno di nuovo la stessa tc in modo che diventi la card_down
        }
        //se è filled ritorno la carta che è adesso in up per settarla come down
        else if ( filled ) return card;
        else { //non dovrei entrare mai in questo ramo salvo errori particolari che analizzerò quando testo questa classe
            System.out.println("ERROR: THERE IS NO CARD HERE pos " + value); //value = valore dell'angolo nella cella
            return null;}
    }
    public void setCard(PlayCard card) {
        this.card = card;
    }
    public AnglesEnum getValue() {
        return value;
    }
    public void setValue(AnglesEnum value) {
        this.value = value;
    }

    public PlayCard getCardDown() { //filled=0 angleseenum.empty
        if(!isFilled() && getValue().equals(AnglesEnum.EMPTY)){
            //System.out.println("carta trasparente");
            return card_down; //non sono certo che sia da fare così
        }
        else{ //credo non sia nemmeno più necessario se inizializzo tutte le celle a tc
            if ( !isEmpty() ) return card_down;
            System.out.println("ERROR: THERE IS NO CARD HERE");
            return null;
        }

    }
    public void setCardDown(PlayCard card) {
        this.card_down = card;
    }
}
