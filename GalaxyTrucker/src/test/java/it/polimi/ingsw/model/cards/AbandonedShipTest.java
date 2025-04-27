package it.polimi.ingsw.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipTest {

    private AbandonedShip card;

    @BeforeEach
    void setUp() {
        card = new AbandonedShip( false, 1,1,1,1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0117.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0117.jpg");
    }

    @Test
    void accept() {
    }

    @Test
    void testAccept() {
    }

    @Test
    void getDays() {
        assertEquals(1, card.getDays());
    }

    @Test
    void getCredits() {
    }

    @Test
    void getNumHuman() {
    }

    @Test
    void setTruckerThatHasDecided() {
    }

    @Test
    void setCoordinates() {
    }

    @Test
    void cardEffect() {
    }
}