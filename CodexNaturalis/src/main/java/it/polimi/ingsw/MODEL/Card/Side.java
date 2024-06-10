package it.polimi.ingsw.MODEL.Card;
import it.polimi.ingsw.MODEL.ENUM.AnglesEnum;
import it.polimi.ingsw.MODEL.ENUM.CentralEnum;

import java.io.Serializable;


/**
 * Represents a side of a card in the game, containing corner angles and central resources.
 *
 * Note that `central_resource_list` is only
 * relevant for the back side of StartingCard instances. It will be null for other
 * card types and for the front side of StartingCards.
 */
public class Side implements Serializable {
    /**
     * rappresent each angles of the card
     */
    private final AnglesEnum angle_right_up;
    private final AnglesEnum angle_right_down;
    private final AnglesEnum angle_left_up;
    private final AnglesEnum angle_left_down;
    /**
     * (optional) rappresent the central resources
     */
    private final CentralEnum central_resource;
    private final CentralEnum central_resource2;
    private final CentralEnum central_resource3;

    /**
     * Creates a new Side with corner angles and central resources.
     *
     * @param angle_right_up Angle of the top right corner.
     * @param angle_right_down Angle of the bottom right corner.
     * @param angle_left_up Angle of the top left corner.
     * @param angle_left_down Angle of the bottom left corner.
     * @param central_resource Optional central resource type (may be null).
     * @param central_resource2 Optional second central resource type (may be null).
     * @param central_resource3 Optional third central resource type (may be null).
     */
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

    public CentralEnum getCentral_resource2() {
        return central_resource2;
    }

    public CentralEnum getCentral_resource3() {
        return central_resource3;
    }
}
