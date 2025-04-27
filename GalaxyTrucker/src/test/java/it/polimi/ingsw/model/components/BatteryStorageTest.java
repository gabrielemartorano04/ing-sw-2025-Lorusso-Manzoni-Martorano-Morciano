package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.MBeanAttributeInfo;

import static org.junit.jupiter.api.Assertions.*;

class BatteryStorageTest {

    private BatteryStorage batteryStorage;

    @BeforeEach
    void setUp() {
        batteryStorage=new BatteryStorage(1,2,3,1,"Battery Storage","",2);
    }

    @Test
    void setAndGetQuantity() {
        batteryStorage.setQuantity(1);
        assertEquals(1, batteryStorage.getQuantity());
    }

    @Test
    void getCapacity() {
        assertEquals(2,batteryStorage.getCapacity());
    }
}