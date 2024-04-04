package it.polimi.ingsw.view;
import java.util.Scanner;
public class View {

    /*
    questa è la classe main della view
    TODO:
        -input e output in main
        -gestione delle rechieste tramite sottoclassi
     */
    GestioneView view = new GestioneView();

    public void inizio(){
        view.getInput();
        
    }


}
