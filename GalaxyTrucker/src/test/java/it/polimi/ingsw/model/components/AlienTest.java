package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlienTest {

    private Alien alien;

    @BeforeEach
    void setUp() {
        alien=new Alien(AlienColor.PURPLE);
    }

    @Test
    void getColor() {
        assertEquals(AlienColor.PURPLE, alien.getColor());
    }
}