package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Engine class test
 */
class EngineTest {

    /**
     * Represents an instance of the Engine class used in testing
     */
    private Engine engine;

    /**
     * Sets up the test environment by initializing the necessary resources
     */
    @BeforeEach
    void setUp() {
        engine=new Engine(1,1,1,"Engine","",true);
    }

    /**
     * This method verifies that the `setIsActive` method successfully updates the activation
     * status of the engine to the specified value and that the `getIsActive` method accurately
     * returns the updated value
     */
    @Test
    void setAndGetIsActive() {
        engine.setIsActive(true);
        assertTrue(engine.getIsActive());
    }

    /**
     * Validates the `getIsDouble` method of the `Engine` class to ensure it correctly
     * returns whether the engine is a double engine or a single engine
     */
    @Test
    void getIsDouble() {
        assertTrue(engine.getIsDouble());
    }

    /**
     * Verifies that the setOrientation method correctly assigns the specified orientation to the engine
     * and that the getOrientation method accurately returns the assigned orientation
     */
    @Test
    void setAndGetOrientation() {
        engine.setOrientation(Orientation.E);
        assertEquals(Orientation.E, engine.getOrientation());
    }

    /**
     * The test initializes the engine's orientation to a specific direction, calls the
     * `turn` method, and checks whether the orientation changes
     * to the expected following direction
     */
    @Test
    void turn() {
        engine.setOrientation(Orientation.E);
        engine.turn();
        assertEquals(Orientation.S, engine.getOrientation());
    }
}