package it.polimi.ingsw.MODEL.ENUM;

/**
 * Represents different resource constraints required to activate card effects.
 */
public enum Costraint {
    /**
     * Two resource constraints
     */
    TWOMUSH_ONEANIM,
    TWOMUSH_ONEPLANT,
    TWOMUSH_ONEINS,
    TWOPLANT_ONEINS,
    TWOPLANT_ONEMUSH,
    TWOPLANT_ONEANIM,
    TWOANIM_ONEINS,
    TWOANIM_ONEMUSH,
    TWOANIM_ONEPLANT,
    TWOINS_ONEPLANT,
    TWOINS_ONEMUSH,
    TWOINS_ONEANIM,

    /**
     * Three resource constraints
     */
    THREEMUSH_ONEANIM,
    THREEMUSH_ONEPLANT,
    THREEMUSH_ONEINS,
    THREEMUSH,
    THREEPLANT,
    THREEPLANT_ONEANIM,
    THREEPLANT_ONEINS,
    THREEPLANT_ONEAMUSH,
    THREEANIM,
    THREEANIM_ONEPLANT,
    THREEANIM_ONEINS,
    THREEANIM_ONEAMUSH,
    THREEINS,
    THREEINS_ONEANIM,
    THREEINS_ONEPLANT,
    THREEINT_ONEAMUSH,

    /**
     * Five resource constraints
     */
    FIVEPLANT,
    FIVEANIM,
    FIVEINS,
    FIVEMUSH,
    /**
     * No constraint
     */
    NONE

}
