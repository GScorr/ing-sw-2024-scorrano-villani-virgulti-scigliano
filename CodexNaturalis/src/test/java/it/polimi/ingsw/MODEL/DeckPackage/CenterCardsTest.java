package it.polimi.ingsw.MODEL.DeckPackage;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Game.DeckCreation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CenterCardsTest {

    DeckCreation creation = new DeckCreation();

    Deck gold_deck = new Deck(creation.getMixGoldDeck());
    Deck resource_deck = new Deck(creation.getMixResourcesDeck());

    List<PlayCard> gold_list = new ArrayList<>();
    List<PlayCard> resource_list = new ArrayList<>();

    List<PlayCard> carte_in_mano = new ArrayList<>();
    List<PlayCard> carte_in_lista_pre_draw = new ArrayList<>();
    List<PlayCard> carte_in_lista_post_draw = new ArrayList<>();

    CenterCards carte_al_centro;

    void creaListe(){
        /*
        creo le
         */
        gold_list.add(gold_deck.drawCard());
        gold_list.add(gold_deck.drawCard());
        resource_list.add(resource_deck.drawCard());
        resource_list.add(resource_deck.drawCard());
        carte_al_centro = new CenterCards(gold_list, resource_list, gold_deck, resource_deck);

    }
/*
setter e getter non li testo
 */
    @Test
    void setGold_list() {
    }

    @Test
    void setResource_list() {
    }

    @Test
    void getGold_list() {
    }

    @Test
    void getResource_list() {
    }

    @Test
    void drawGoldCard() {
        int index=1;
        creaListe();

        /*
            stampo le carte che ho nelle liste
         */
        for(int i=0; i<carte_al_centro.getGold_list().size(); i++){
            System.out.println("carta "+(i+1));
            System.err.println(carte_al_centro.getGold_list().get(i).getSide().getAngleLeftUp()+ " " + carte_al_centro.getGold_list().get(i).getSide().getAngleRightUp());
            System.err.println("    " + carte_al_centro.getGold_list().get(i).getSide().getCentral_resource());
            System.err.println("    " + carte_al_centro.getGold_list().get(i).getSide().getCentral_resource2());
            System.err.println("    " + carte_al_centro.getGold_list().get(i).getSide().getCentral_resource3());
            System.err.println(carte_al_centro.getGold_list().get(i).getSide().getAngleLeftDown() + " " + carte_al_centro.getGold_list().get(i).getSide().getAngleRightDown());
        }
        carte_in_lista_pre_draw.addAll(carte_al_centro.getGold_list());
        for(int i=0; i<carte_in_lista_pre_draw.size(); i++){
            System.out.println("carta in lista "+(i+1));
            System.err.println(carte_in_lista_pre_draw.get(i).getSide().getAngleLeftUp()+ " " + carte_in_lista_pre_draw.get(i).getSide().getAngleRightUp());
            System.err.println("    " + carte_in_lista_pre_draw.get(i).getSide().getCentral_resource());
            System.err.println("    " + carte_in_lista_pre_draw.get(i).getSide().getCentral_resource2());
            System.err.println("    " + carte_in_lista_pre_draw.get(i).getSide().getCentral_resource3());
            System.err.println(carte_in_lista_pre_draw.get(i).getSide().getAngleLeftDown() + " " + carte_in_lista_pre_draw.get(i).getSide().getAngleRightDown());
        }
        carte_in_mano.add(carte_al_centro.drawGoldCard(index));

        /*
        ristampo le carte dopo aver pescato e aggiornato la carta
         */
        for(int i=0; i<carte_al_centro.getGold_list().size(); i++){
            System.out.println("carta "+(i+1));
            System.err.println(carte_al_centro.getGold_list().get(i).getSide().getAngleLeftUp()+ " " + carte_al_centro.getGold_list().get(i).getSide().getAngleRightUp());
            System.err.println("    " + carte_al_centro.getGold_list().get(i).getSide().getCentral_resource());
            System.err.println("    " + carte_al_centro.getGold_list().get(i).getSide().getCentral_resource2());
            System.err.println("    " + carte_al_centro.getGold_list().get(i).getSide().getCentral_resource3());
            System.err.println(carte_al_centro.getGold_list().get(i).getSide().getAngleLeftDown() + " " + carte_al_centro.getGold_list().get(i).getSide().getAngleRightDown());
        }
        carte_in_lista_post_draw.addAll(carte_al_centro.getGold_list());
        for(int i=0; i<carte_in_lista_post_draw.size(); i++){
            System.out.println("carta in lista "+(i+1));
            System.err.println(carte_in_lista_post_draw.get(i).getSide().getAngleLeftUp()+ " " + carte_in_lista_post_draw.get(i).getSide().getAngleRightUp());
            System.err.println("    " + carte_in_lista_post_draw.get(i).getSide().getCentral_resource());
            System.err.println("    " + carte_in_lista_post_draw.get(i).getSide().getCentral_resource2());
            System.err.println("    " + carte_in_lista_post_draw.get(i).getSide().getCentral_resource3());
            System.err.println(carte_in_lista_post_draw.get(i).getSide().getAngleLeftDown() + " " + carte_in_lista_post_draw.get(i).getSide().getAngleRightDown());
        }

        assertNotEquals(carte_in_lista_pre_draw, carte_in_lista_post_draw, "corretto");


        //System.err.println(carte_in_mano.get(0).getSide().getCentral_resource3());


    }

    @Test
    void drawResourceCard() {
        int index=1;
        creaListe();
        for(int i=0; i<carte_al_centro.getResource_list().size(); i++){
            System.out.println("carta "+(i+1));
            System.err.println(carte_al_centro.getResource_list().get(i).getSide().getAngleLeftUp()+ " " + carte_al_centro.getResource_list().get(i).getSide().getAngleRightUp());
            System.err.println("    " + carte_al_centro.getResource_list().get(i).getSide().getCentral_resource());
            System.err.println("    " + carte_al_centro.getResource_list().get(i).getSide().getCentral_resource2());
            System.err.println("    " + carte_al_centro.getResource_list().get(i).getSide().getCentral_resource3());
            System.err.println(carte_al_centro.getResource_list().get(i).getSide().getAngleLeftDown() + " " + carte_al_centro.getResource_list().get(i).getSide().getAngleRightDown());
        }

        carte_in_lista_pre_draw.addAll(carte_al_centro.getResource_list());
        for(int i=0; i<carte_in_lista_pre_draw.size(); i++){
            System.out.println("carta in lista "+(i+1));
            System.err.println(carte_in_lista_pre_draw.get(i).getSide().getAngleLeftUp()+ " " + carte_in_lista_pre_draw.get(i).getSide().getAngleRightUp());
            System.err.println("    " + carte_in_lista_pre_draw.get(i).getSide().getCentral_resource());
            System.err.println("    " + carte_in_lista_pre_draw.get(i).getSide().getCentral_resource2());
            System.err.println("    " + carte_in_lista_pre_draw.get(i).getSide().getCentral_resource3());
            System.err.println(carte_in_lista_pre_draw.get(i).getSide().getAngleLeftDown() + " " + carte_in_lista_pre_draw.get(i).getSide().getAngleRightDown());
        }

        carte_in_mano.add(carte_al_centro.drawResourceCard(index));
        /*
        ristampo le carte dopo aver pescato e aggiornato la carta
         */
        for(int i=0; i<carte_al_centro.getResource_list().size(); i++){
            System.out.println("carta "+(i+1));
            System.err.println(carte_al_centro.getResource_list().get(i).getSide().getAngleLeftUp()+ " " + carte_al_centro.getResource_list().get(i).getSide().getAngleRightUp());
            System.err.println("    " + carte_al_centro.getResource_list().get(i).getSide().getCentral_resource());
            System.err.println("    " + carte_al_centro.getResource_list().get(i).getSide().getCentral_resource2());
            System.err.println("    " + carte_al_centro.getResource_list().get(i).getSide().getCentral_resource3());
            System.err.println(carte_al_centro.getResource_list().get(i).getSide().getAngleLeftDown() + " " + carte_al_centro.getResource_list().get(i).getSide().getAngleRightDown());
        }

        carte_in_lista_post_draw.addAll(carte_al_centro.getResource_list());
        for(int i=0; i<carte_in_lista_post_draw.size(); i++){
            System.out.println("carta in lista "+(i+1));
            System.err.println(carte_in_lista_post_draw.get(i).getSide().getAngleLeftUp()+ " " + carte_in_lista_post_draw.get(i).getSide().getAngleRightUp());
            System.err.println("    " + carte_in_lista_post_draw.get(i).getSide().getCentral_resource());
            System.err.println("    " + carte_in_lista_post_draw.get(i).getSide().getCentral_resource2());
            System.err.println("    " + carte_in_lista_post_draw.get(i).getSide().getCentral_resource3());
            System.err.println(carte_in_lista_post_draw.get(i).getSide().getAngleLeftDown() + " " + carte_in_lista_post_draw.get(i).getSide().getAngleRightDown());
        }

        assertNotEquals(carte_in_lista_pre_draw, carte_in_lista_post_draw, "corretto");


    }
}