package it.polimi.ingsw.MODEL.Card;

/*
* @Virgulti Francesco
*
* La StartingCard NON è trattata come una carta normale.
* Ha comunque un BackSide e un FronSide
* front_side è simile al front_side delle carte normali
* il back_side è simile al back_side delle carte normali ma al suo interno è inizializzato il central_resources_list, cioè l'elenco dei central_resources
*
* */
public class StartingCard {
    private final Side front_side ;
    private final  Side back_side;
    private boolean flipped;




    public StartingCard(Side front_side, Side back_side, boolean flipped) {
        // Chiamo il costruttore di default per inizializzare le variabili
        this.front_side = front_side;
        this.back_side = back_side;
        flipCard(flipped);

    }


    public void flipCard(boolean flipped){
        this.flipped = flipped;
    }


    private Side getFrontSide() {
        return front_side;
    }


    private Side getBackSide() {
        return back_side;
    }

    public  Side getSide(){
        if( !flipped){
            return getFrontSide();
        }
        else{
            return getBackSide();
        }
    }







}
