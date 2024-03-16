package it.polimi.ingsw.model;
import it.polimi.ingsw.model.Side;
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
        this.front_side = front_side;
        this.back_side = back_side;
        flipCard(flipped);
        this.central_resources = central_resources;
    }

    /*
    * flipped = true => abilito getFrontSide
    * flipped = false => abilito getBackSide*/
    public void flipCard(boolean flipped){
        this.flipped = flipped;
    }

    /*
    * flipped = true => result.contains(front_side)
    * flipped = false => result=null
    * throw new Error
    * */

    public Side getFrontSide() {
        return front_side;
    }

    public Side getBackSide() {
        return back_side;
    }

    public CentralEnum getCentralResources() {
        return central_resources;
    }
}
