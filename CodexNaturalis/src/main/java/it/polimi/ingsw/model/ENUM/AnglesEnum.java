package it.polimi.ingsw.model.ENUM;



public enum AnglesEnum{
    ANIMAL,
    MUSHROOMS,
    INSECTS,
    PLANT,
    NONE,    //The angle doesn't exist, you can't put a card on top of here
    EMPTY,  // angle exist but it's empty, it doesn't have any resources or bonus
    PAPER,
    PEN,
    FEATHER,
    MIX // special enumeration for the management of goals
}
