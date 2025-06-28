package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Empty Space class test
 */
class EmptySpaceTest {

    /**
     * Represents an instance of the Empty Space class used in testing
     */
    private EmptySpace emptySpace;

    /**
     * Sets up the test environment by initializing the necessary resources
     */
    @BeforeEach
    void setUp() {
        emptySpace = new EmptySpace();
    }

    /**
     * Tests whether the `emptySpace` object is successfully initialized and not null before usage
     */
    @Test
    void testEmptySpace() {
        assertNotNull(emptySpace);
    }
}