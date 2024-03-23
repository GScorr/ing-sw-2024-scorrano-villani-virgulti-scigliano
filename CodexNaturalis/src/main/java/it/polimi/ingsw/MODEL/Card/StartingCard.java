package it.polimi.ingsw.MODEL.Card;

import it.polimi.ingsw.MODEL.ENUM.Costraint;

/*
* @Virgulti Francesco
*
* La StartingCard NON è trattata come una carta normale.
* Ha comunque un BackSide e un FronSide
* front_side è simile al front_side delle carte normali
* il back_side è simile al back_side delle carte normali ma al suo interno è inizializzato il central_resources_list, cioè l'elenco dei central_resources
*
* */
public class StartingCard extends PlayCard{

    public StartingCard(Side front_side, Side back_side, boolean flipped) {
        // Chiamo il costruttore di default per inizializzare le variabili
        super(front_side,back_side,flipped);

    }


    public  String getType(){return "Starting Card";}

    //override sulle sottoclassi per il point
    public  int getPoint(){return 0;}

    //override sulle sottoclassi per il costraint -> Resource Card tornerà NONE
    public Costraint getCostraint(){return Costraint.NONE;};



}
