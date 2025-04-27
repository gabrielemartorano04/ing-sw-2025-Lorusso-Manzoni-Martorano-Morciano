package it.polimi.ingsw.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlaversTest {

    private Slavers card;

    @BeforeEach
    void setUp() {
        card=new Slavers(false,1,2,3,1,2,"","");
    }

    @Test
    void accept() {
    }

    @Test
    void testAccept() {
    }

    @Test
    void getSlaversPower() {
        assertEquals(2,card.getSlaversPower());
    }

    @Test
    void getAstronautsLost() {
        assertEquals(1,card.getAstronautsLost());
    }

    @Test
    void getDays() {
        assertEquals(3,card.getDays());
    }

    @Test
    void getCredits() {
        assertEquals(2,card.getCredits());
    }

    @Test
    void setDefeatedTruckers() {
    }

    @Test
    void setWinnerTrucker() {
    }

    @Test
    void addTruckersCoordinatesToRemoveHumans() {
    }

    @Test
    void addDoubleCannonsCoord() {
    }

    @Test
    void setChooseToGain() {
    }

    @Test
    void cardEffect() {
    }
}