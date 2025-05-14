package it.polimi.ingsw.model;

import it.polimi.ingsw.model.components.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AvailableComponentsTest {

    AvailableComponents av;


    @BeforeEach
    void setUp() {
        av = new AvailableComponents();
    }

    @Test
    void getComponents() {
        assertNotNull(av.getComponents());
    }

    @Test
    void shuffle() {
        assertNotNull(av.getComponents());
    }

    @Test
    void remove() {
        int index=10;
        Component component=av.getComponents().get(index);
        av.removeComponent(index);
        //assertFalse(av.getComponents().contains(component));
    }
}