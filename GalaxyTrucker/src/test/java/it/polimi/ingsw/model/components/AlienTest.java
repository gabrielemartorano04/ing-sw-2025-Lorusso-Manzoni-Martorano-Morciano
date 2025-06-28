package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Alien class test
 */
class AlienTest {

    /**
     * Represents an instance of the Alien class used in testing
     */
    private Alien alien;

    /**
     * Sets up the test environment by initializing the necessary resources
     */
    @BeforeEach
    void setUp() {
        alien=new Alien(AlienColor.PURPLE);
    }

    /**
     * Validates that the color of the alien is correctly returned as specified during its initialization
     */
    @Test
    void getColor() {
        assertEquals(AlienColor.PURPLE, alien.getColor());
    }
}