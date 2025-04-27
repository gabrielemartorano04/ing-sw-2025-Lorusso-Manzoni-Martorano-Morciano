package it.polimi.ingsw.model;

import it.polimi.ingsw.model.components.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AvailableComponentsTest {

    AvailableComponents av;
    Component component;

    @BeforeEach
    void setUp() {
        av = new AvailableComponents();
        component = new Load(1,1,1,1,"Load","",2,false);
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
        av.remove(component);
        assertFalse(av.getComponents().contains(component));
    }
}