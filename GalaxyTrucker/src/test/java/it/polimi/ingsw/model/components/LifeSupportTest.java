package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LifeSupportTest {

    private LifeSupport lifeSupport;

    @BeforeEach
    void setUp() {
        lifeSupport=new LifeSupport(1,1,1,1,"Life Support", "",AlienColor.BROWN);
    }

    @Test
    void getColor() {
        assertEquals(AlienColor.BROWN,lifeSupport.getColor());
    }
}