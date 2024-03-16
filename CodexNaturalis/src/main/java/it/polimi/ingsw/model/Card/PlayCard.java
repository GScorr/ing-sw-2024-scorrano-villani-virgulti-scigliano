package it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.ENUM.CentralEnum;

/*
* @Francesco_Virgulti
* TODO: - se flipp = true =>  getFrontalSide || flipp = false => getBackSide
*       - implementare metodo place()
*/
public class PlayCard {
    private final Side front_side ;
    private final  Side back_side;
    private boolean flipped;
    private final CentralEnum central_resources;



    public PlayCard(Side front_side, Side back_side, boolean flipped, CentralEnum central_resources) {
        // Chiamo il costruttore di default per inizializzare le variabili
        this.front_side = front_side;
        this.back_side = back_side;
        flipCard(flipped);
        this.central_resources = central_resources;
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

    public CentralEnum getCentralResources() {
        if(flipped){
        return central_resources;}
        else{return null;}
    }
}
