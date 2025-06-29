package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.PlayerColors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CabinTest {

    private Cabin cabin;

    @BeforeEach
    void setUp() {
        cabin=new Cabin(3,3,3,3,"Cabin","", PlayerColors.BLUE, true);
    }

    @Test
    void setAndGetAlien() {
        cabin.setAlien(new Alien(AlienColor.PURPLE));
        assertNotNull(cabin.getAlien());
        assertEquals(AlienColor.PURPLE,cabin.getAlien().getColor());
    }

    @Test
    void setAndGetQuantity() {
        cabin.setQuantity(2);
        assertEquals(2,cabin.getQuantity());
    }

    @Test
    void getIsMain() {
        assertTrue(cabin.getIsMain());
    }

    @Test
    void getCapacity() {
        assertEquals(2,cabin.getCapacity());
    }

    @Test
    void getColor() {
        assertEquals(PlayerColors.BLUE,cabin.getColor());
    }

    @Test
    void removeHumans() {
        cabin.setQuantity(2);
        cabin.removeHumans(1);
        assertEquals(1,cabin.getQuantity());
    }
}