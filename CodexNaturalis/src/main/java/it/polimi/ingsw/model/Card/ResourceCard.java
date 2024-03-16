package it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.ENUM.CentralEnum;

public class ResourceCard extends PlayCard {
    private final int point;
    public ResourceCard(Side front_side, Side back_side, boolean flipped, CentralEnum central_resources, int point){
        super( front_side, back_side,  flipped, central_resources);
        this.point= point;
    }

    public int getPoint() {
        return point;
    }
}
