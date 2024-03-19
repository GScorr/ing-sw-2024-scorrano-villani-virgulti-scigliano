package it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.ENUM.CentralEnum;
import it.polimi.ingsw.model.ENUM.Costraint;

/*
* @Virgulti Francesco
*
* TODO:
*   -Inserire pointBonus
* */
public class GoldCard extends ResourceCard{
    private final Costraint costraint;
    public GoldCard(Side front_side, Side back_side, boolean flipped, int point, Costraint costraint){
        super( front_side, back_side ,flipped,  point);
        this.costraint = costraint;
    }

    public  String getType(){return "Gold";}


    public Costraint getCostraint() {

        if(!super.flipped)return costraint;
        else return Costraint.NONE;
    }

}
