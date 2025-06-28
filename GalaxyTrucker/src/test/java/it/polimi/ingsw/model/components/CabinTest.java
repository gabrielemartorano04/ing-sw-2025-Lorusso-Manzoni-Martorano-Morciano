package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.PlayerColors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cabin class test
 */
class CabinTest {

    /**
     * Represents an instance of the Cabin class used in testing
     */
    private Cabin cabin;

    /**
     * Sets up the test environment by initializing the necessary resources
     */
    @BeforeEach
    void setUp() {
        cabin=new Cabin(3,3,3,3,"Cabin","", PlayerColors.BLUE, true);
    }

    /**
     * Verifies that the alien can be successfully set using the setAlien method
     * and that the same alien can be removed with the getAlien method. Additionally,
     * it checks that the color of the alien matches the expected value
     */
    @Test
    void setAndGetAlien() {
        cabin.setAlien(new Alien(AlienColor.PURPLE));
        assertNotNull(cabin.getAlien());
        assertEquals(AlienColor.PURPLE,cabin.getAlien().getColor());
    }

    /**
     * Tests the functionality of setting and removing the quantity of crew members in the cabin.
     * This method validates that the `setQuantity` method correctly assigns the specified value
     * and that the `getQuantity` method accurately retrieves the assigned value.
     */
    @Test
    void setAndGetQuantity() {
        cabin.setQuantity(2);
        assertEquals(2,cabin.getQuantity());
    }

    /**
     * Validates the `getIsMain` method of the Cabin class to ensure it correctly returns
     * whether the cabin is designated as a main cabin
     */
    @Test
    void getIsMain() {
        assertTrue(cabin.getIsMain());
    }

    /**
     * This method validates that the `getCapacity` method correctly returns
     * the maximum number of crew members that the cabin can contain.
     */
    @Test
    void getCapacity() {
        assertEquals(2,cabin.getCapacity());
    }

    /**
     * Tests the `getColor` method of the `Cabin` class to verify that it returns the expected
     * color of the main cabin
     */
    @Test
    void getColor() {
        assertEquals(PlayerColors.BLUE,cabin.getColor());
    }

    /**
     * This method verifies that the quantity of crew members in the cabin decreases
     * by the specified amount and that the new quantity is correctly reflected through the
     * `getQuantity` method
     */
    @Test
    void removeHumans() {
        cabin.setQuantity(2);
        cabin.removeHumans(1);
        assertEquals(1,cabin.getQuantity());
    }
}