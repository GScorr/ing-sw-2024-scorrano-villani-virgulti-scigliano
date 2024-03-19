package it.polimi.ingsw.MODEL.Card;
import it.polimi.ingsw.MODEL.ENUM.BonusEnum;
import it.polimi.ingsw.MODEL.ENUM.Costraint;

/*
* @Virgulti Francesco
*
* TODO:
*   -Inserire pointBonus
* */
public class GoldCard extends ResourceCard{
    private final Costraint costraint;

    private final BonusEnum point_bonus;
    public GoldCard(Side front_side, Side back_side, boolean flipped, int point, Costraint costraint, BonusEnum point_bonus){
        super( front_side, back_side ,flipped,  point);
        this.costraint = costraint;
        this.point_bonus = point_bonus;
    }

    public  String getType(){return "Gold";}


    public Costraint getCostraint() {

        if(!super.flipped)return costraint;
        else return Costraint.NONE;
    }

    public BonusEnum getPointBonus(){
        return  point_bonus;
    }
}
