package it.polimi.ingsw.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PiratesTest {

    private Pirates card;

    @BeforeEach
    void setUp() {
        card=new Pirates(false,1,1,1,1, new ArrayList<int[]>(),"","");
    }

    @Test
    void accept() {
    }

    @Test
    void testAccept() {
    }

    @Test
    void getCredits() {
        assertEquals(1,card.getCredits());
    }

    @Test
    void getDays() {
        assertEquals(1,card.getDays());
    }

    @Test
    void getPiratesPower() {
        assertEquals(1,card.getPiratesPower());
    }

    @Test
    void getShoots() {
        assertNotNull(card.getShoots());
    }

    @Test
    void setDefeatedTruckers() {
    }

    @Test
    void addDoubleCannonsCoord() {
    }

    @Test
    void setChooseToGain() {
    }

    @Test
    void setWinnerTrucker() {
    }

    @Test
    void getDirections() {

    }

    @Test
    void getCurrentShoot() {
    }

    @Test
    void rollDice() {
    }

    @Test
    void setAnswers() {
    }

    @Test
    void cardEffect() {
    }
}