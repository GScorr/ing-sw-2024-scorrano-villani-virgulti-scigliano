package it.polimi.ingsw.MODEL.ENUM;


import java.io.Serializable;

public enum AnglesEnum implements Serializable {
    ANIMAL,
    MUSHROOMS,
    INSECTS,
    PLANT,
    NONE,    //The angle doesn't exist, you can't put a card on top of here
    EMPTY,  // angle exist but it's empty, it doesn't have any resources or bonus
    PAPER,
    PEN,
    FEATHER, //piuma
    MIX; // special enumeration for the management of goals

    public static AnglesEnum fromString(String value) {
        if (value != null) {
            for (AnglesEnum angle : AnglesEnum.values()) {
                if (value.equalsIgnoreCase(angle.name())) {
                    return angle;
                }
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found in AnglesEnum");
    }
}
