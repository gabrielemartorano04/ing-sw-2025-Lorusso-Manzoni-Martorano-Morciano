package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.components.Goods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlanetsTest {

    private Planets card;

    @BeforeEach
    void setUp() {
        card=new Planets(false,1,1,new ArrayList<ArrayList<Goods>>(),"","");
    }

    @Test
    void accept() {
    }

    @Test
    void testAccept() {
    }

    @Test
    void getPlanets() {
        assertEquals(card.getPlanets(),new ArrayList<ArrayList<Goods>>());
    }

//    @Test
//    void getBank() {
//        assertNotNull(card.getBank(new Bank()));
//    }

    @Test
    void setPlayerLanding() {
    }

    @Test
    void initialize() {
    }

    @Test
    void plotGoodFromPlanet() {
    }

    @Test
    void plotGoodFromList() {
    }

    @Test
    void chooseToGain() {
    }

    @Test
    void chooseFromList() {
    }

    @Test
    void setCoordinatesToAddGoods() {
    }

    @Test
    void coordinatesToAddFromList() {
    }

    @Test
    void rearrange() {
    }

    @Test
    void cardEffect() {
    }
}