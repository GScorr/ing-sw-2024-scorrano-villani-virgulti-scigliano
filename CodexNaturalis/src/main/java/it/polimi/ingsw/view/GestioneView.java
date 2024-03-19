package it.polimi.ingsw.view;


import java.util.Scanner;

public class GestioneView {

    /*
    in questa classe gestisco gli output
     */

    private Scanner scanner;

    /*
    da questa classe leggo da input
     */
    public GestioneView(){
        this.scanner = new Scanner(System.in);
    }

    public void getInput(){
        System.out.println("inserisci un numero:");
        int scelta = scanner.nextInt();
        scegliCarta(scelta);
    }

    private void scegliCarta(int scelta){
        switch (scelta){
            case 1:
                System.out.println("gira carta");
            case 2:
                System.out.println("posiziona carta");
            default:
                System.out.println("scelta non valida");
        }
    }

}
