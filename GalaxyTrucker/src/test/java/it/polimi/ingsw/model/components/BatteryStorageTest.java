package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.MBeanAttributeInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Battery storage class test
 */
class BatteryStorageTest {

    /**
     * Represents an instance of the Battery Storage class used in testing
     */
    private BatteryStorage batteryStorage;

    /**
     * Sets up the test environment by initializing the necessary resources
     */
    @BeforeEach
    void setUp() {
        batteryStorage=new BatteryStorage(1,2,3,1,"Battery Storage","",2);
    }

    /**
     * Tests the functionality of setting and removing the quantity of batteries
     * stored in the BatteryStorage instance. Verifies that the `setQuantity`
     * method correctly assigns the given value and that the `getQuantity` method
     * removes the updated value accurately.
     */
    @Test
    void setAndGetQuantity() {
        batteryStorage.setQuantity(1);
        assertEquals(1, batteryStorage.getQuantity());
    }

    /**
     * Verifies that the method correctly returns the maximum number of batteries
     * that can be stored as specified during the initialization
     */
    @Test
    void getCapacity() {
        assertEquals(2,batteryStorage.getCapacity());
    }

    /**
     * Verifies that the specified number of batteries is successfully removed from the current quantity,
     * ensuring the remaining quantity is updated correctly
     */
    @Test
    void removeNumOfBatteries() {
        batteryStorage.setQuantity(2);
        batteryStorage.removeNumOfBatteries(1);
        assertEquals(1,batteryStorage.getQuantity());
    }
}