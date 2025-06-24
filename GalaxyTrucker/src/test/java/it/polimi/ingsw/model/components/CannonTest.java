package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CannonTest {

    private Cannon cannon;

    @BeforeEach
    void setUp() {
        cannon=new Cannon(1,2,3,"Cannon","",false);
    }

    @Test
    void setAndGetOrientation() {
        cannon.setOrientation(Orientation.E);
        assertEquals(Orientation.E,cannon.getOrientation());
    }

    @Test
    void setAndGetIsActive() {
        cannon.setIsActive(true);
        assertTrue(cannon.getIsActive());
    }

    @Test
    void getIsDouble() {
        assertFalse(cannon.getIsDouble());
    }

    @Test
    void turn() {
        cannon.setOrientation(Orientation.N);
        cannon.turn();
        assertEquals(Orientation.E,cannon.getOrientation());

        cannon.setOrientation(Orientation.E);
        cannon.turn();
        assertEquals(Orientation.S,cannon.getOrientation());

        cannon.setOrientation(Orientation.S);
        cannon.turn();
        assertEquals(Orientation.W,cannon.getOrientation());

        cannon.setOrientation(Orientation.W);
        cannon.turn();
        assertEquals(Orientation.N,cannon.getOrientation());
    }
}