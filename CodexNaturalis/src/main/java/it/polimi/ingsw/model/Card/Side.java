package it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.ENUM.AnglesEnum;

/*
*  @Francesco_Virgulti
*
* */
public class Side {
    private final AnglesEnum angle_right_up;
    private final AnglesEnum angle_right_down;
    private final AnglesEnum angle_left_up;
    private final AnglesEnum angle_left_down;

    public Side(AnglesEnum angle_right_up, AnglesEnum angle_right_down, AnglesEnum angle_left_up, AnglesEnum angle_left_down) {
        this.angle_right_up = angle_right_up;
        this.angle_right_down = angle_right_down;
        this.angle_left_up = angle_left_up;
        this.angle_left_down = angle_left_down;
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


}
