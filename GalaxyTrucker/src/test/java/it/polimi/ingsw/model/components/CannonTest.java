package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cannon class test
 */
class CannonTest {

    /**
     * Represents an instance of the Cannon class used in testing
     */
    private Cannon cannon;

    /**
     * Sets up the test environment by initializing the necessary resources
     */
    @BeforeEach
    void setUp() {
        cannon=new Cannon(1,2,3,"Cannon","",false);
    }

    /**
     * The test asserts that the `setOrientation` method correctly assigns
     * the specified orientation to the cannon and that the `getOrientation`
     * method accurately returns the assigned value.
     */
    @Test
    void setAndGetOrientation() {
        cannon.setOrientation(Orientation.E);
        assertEquals(Orientation.E,cannon.getOrientation());
    }

    /**
     * This test verifies that the `setIsActive` method correctly modifies the "is active" state
     * of the cannon, and subsequently ensures that the `getIsActive` method accurately returns
     * the updated state
     */
    @Test
    void setAndGetIsActive() {
        cannon.setIsActive(true);
        assertTrue(cannon.getIsActive());
    }

    /**
     * Tests the `getIsDouble` method of the `Cannon` class to verify that it correctly returns whether
     * the cannon is a double cannon or a single cannon by validating that the value returned is
     * consistent with the expected boolean value
     */
    @Test
    void getIsDouble() {
        assertFalse(cannon.getIsDouble());
    }

    /**
     * The test verifies that calling `turn` results in a clockwise change of the cannon's orientation
     * through the predefined sequence (N -> E -> S -> W -> N)
     */
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