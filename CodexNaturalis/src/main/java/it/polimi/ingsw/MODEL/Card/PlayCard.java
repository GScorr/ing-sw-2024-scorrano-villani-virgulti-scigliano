package it.polimi.ingsw.MODEL.Card;
import it.polimi.ingsw.MODEL.ENUM.Costraint;

/*
* @Francesco_Virgulti
* TODO: - se flipp = true =>  getFrontalSide || flipp = false => getBackSide
*       - implementare metodo place()
*
*/
public abstract class  PlayCard {
    private final Side front_side ;
    private final  Side back_side;
    protected boolean flipped;




    public PlayCard(Side front_side, Side back_side, boolean flipped) {
        // Chiamo il costruttore di default per inizializzare le variabili
        this.front_side = front_side;
        this.back_side = back_side;
        flipCard(flipped);

    }


    public void flipCard(boolean flipped){
        this.flipped = flipped;
    }


    private Side getFrontSide() {
        return front_side;
    }


    private Side getBackSide() {
        return back_side;
    }
    public  Side getSide(){
        if( !flipped){
            return getFrontSide();
        }
        else{
            return getBackSide();
        }
    }

    //override sulle sottoclassi per il metodo
    public abstract String getType();

    //override sulle sottoclassi per il point
    public abstract int getPoint();

    //override sulle sottoclassi per il costraint -> Resource Card tornerà NONE
    public abstract Costraint getCostraint();


}
