package it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.ENUM.CentralEnum;
import it.polimi.ingsw.model.ENUM.Costraint;

/*
* @Virgulti Francesco
*
* */

public class ResourceCard extends PlayCard {
    private final int point;
    public ResourceCard(Side front_side, Side back_side, boolean flipped, int point){
        super( front_side, back_side,  flipped);
        this.point= point;
    }

    public  String getType(){return "Resource";}

    public int getPoint() {
        if(!super.flipped) return point;
        else return 0;
    }
    public Costraint getCostraint(){
        return Costraint.NONE;
    }
}
