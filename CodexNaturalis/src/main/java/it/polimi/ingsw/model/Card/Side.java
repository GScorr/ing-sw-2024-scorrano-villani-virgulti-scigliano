package it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.ENUM.AnglesEnum;
import it.polimi.ingsw.model.ENUM.CentralEnum;

import java.util.List;

/*
*  @Francesco_Virgulti
* central_resources_list è passato dal main solamente se la carta è una StartingCard
* In tutte le altre carte central_resources_list sarà nullo
* Nel front_side della StartingCard central_resources_list sarà nullo
* */
public class Side {
    private final AnglesEnum angle_right_up;
    private final AnglesEnum angle_right_down;
    private final AnglesEnum angle_left_up;
    private final AnglesEnum angle_left_down;
    private final CentralEnum central_resource;
    private final CentralEnum central_resource2;
    private final CentralEnum central_resource3;


    public Side(AnglesEnum angle_right_up, AnglesEnum angle_right_down, AnglesEnum angle_left_up, AnglesEnum angle_left_down, CentralEnum central_resource, CentralEnum central_resource2, CentralEnum central_resource3) {
        this.angle_right_up = angle_right_up;
        this.angle_right_down = angle_right_down;
        this.angle_left_up = angle_left_up;
        this.angle_left_down = angle_left_down;
        this.central_resource= central_resource;
        this.central_resource2= central_resource2;
        this.central_resource3= central_resource3;

    }

    public AnglesEnum getAngleRightUp() {
        return angle_right_up;
    }

    public AnglesEnum getAngleRightDown() {
        return angle_right_down;
    }

    public AnglesEnum getAngleLeftUp() {
        return angle_left_up;
    }

    public AnglesEnum getAngleLeftDown() {
        return angle_left_down;
    }

    public CentralEnum getCentral_resource() {
        return central_resource;
    }
}
