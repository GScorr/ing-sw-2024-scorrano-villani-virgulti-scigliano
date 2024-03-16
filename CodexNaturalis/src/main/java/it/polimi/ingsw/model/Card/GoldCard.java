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
    public GoldCard(Side front_side, Side back_side, boolean flipped, CentralEnum central_resources, int point, Costraint costraint){
        super( front_side, back_side ,flipped,  central_resources,  point);
        this.costraint = costraint;
    }

    public Costraint getCostraint() {
        return costraint;
    }
}
