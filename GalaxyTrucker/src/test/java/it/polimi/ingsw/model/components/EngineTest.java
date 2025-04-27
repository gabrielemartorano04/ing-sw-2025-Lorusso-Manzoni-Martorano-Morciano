package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

    private Engine engine;

    @BeforeEach
    void setUp() {
        engine=new Engine(1,1,1,"Engine","",true);
    }

    @Test
    void setAndGetIsActive() {
        engine.setIsActive(true);
        assertTrue(engine.getIsActive());
    }

    @Test
    void getIsDouble() {
        assertTrue(engine.getIsDouble());
    }


    @Test
    void setAndGetOrientation() {
        engine.setOrientation(Orientation.E);
        assertEquals(Orientation.E, engine.getOrientation());
    }

    @Test
    void turn() {

    }
}