package it.polimi.ingsw.model;
import it.polimi.ingsw.model.Side;
import it.polimi.ingsw.model.ENUM.CentralEnum;

/*
* @Francesco_Virgulti
* TODO: - se flipp = true =>  getFrontalSide || flipp = false => getBackSide
*       - implementare metodo place()
*/
public class PlayCard {
    private final Side frontal_side ;
    private final  Side back_side;
    private boolean flipped;
    private final CentralEnum central_resources;

    public PlayCard(Side frontal_side, Side back_side, boolean flipped, CentralEnum central_resources) {
        this.frontal_side = frontal_side;
        this.back_side = back_side;
        flippCard(flipped);
        this.central_resources = central_resources;
    }

    public void flippCard(boolean flipped){
        this.flipped = flipped;
    }

    public Side getFrontalSide() {
        return frontal_side;
    }

    public Side getBackSide() {
        return back_side;
    }

    public CentralEnum getCentralResources() {
        return central_resources;
    }
}
