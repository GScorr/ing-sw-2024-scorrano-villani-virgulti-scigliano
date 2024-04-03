package it.polimi.ingsw.view;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GestioneViewTest {
    private Scanner scanner;

    public scanner GestioneView(){
        this.scanner = new Scanner(System.in);
        return this;
    }

    @Test
    void getInput() {
        System.out.println("inserisci un numero:");
        int scelta = scanner.nextInt();
        scegliCarta(scelta);
    }

    @Test
    void scegliCarta(int scelta) {
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