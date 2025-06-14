package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShieldTest {

    private Shield shield;

    @BeforeEach
    void setUp() {
        shield=new Shield(1,2,3,1,"Shield","");
    }

    @Test
    void setAndGetDirection() {
        shield.setDirection(Direction.NE);
        assertEquals(Direction.NE, shield.getDirection());
    }

    @Test
    void setAndGetIsActive() {
        shield.setIsActive(true);
        assertTrue(shield.getIsActive());

        shield.setIsActive(false);
        assertFalse(shield.getIsActive());
    }

    @Test
    void turn() {
        shield.turn();
        assertEquals(Direction.SE, shield.getDirection());
        assertEquals(shield.getLink()[0],1);
        assertEquals(shield.getLink()[1],1);
        assertEquals(shield.getLink()[2],2);
        assertEquals(shield.getLink()[3],3);
    }
}