package it.polimi.ingsw.model;

import it.polimi.ingsw.model.components.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AvailableComponentsTest {

    AvailableComponents av;
    Component component;
    int index;


    @BeforeEach
    void setUp() {
        index=10;
        av = new AvailableComponents();
        component=av.getComponents().get(index);
        av.getComponents().get(index).setSelected(false);
        av.getComponents().get(index).setCovered(true);
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
    void removeComponent() {
        av.removeComponent(index);
        assertFalse(av.getComponents().contains(component));
    }

    @Test
    void setComponentAsSelected() {
        av.setComponentAsSelected(index);
        assertTrue(av.getComponents().get(index).getSelected());
    }

    @Test
    void setComponentAsUnselected() {
        av.setComponentAsUnselected(index);
        assertFalse(av.getComponents().get(index).getSelected());
    }

    @Test
    void setComponentAsUncovered() {
        av.setComponentAsUncovered(index);
        assertFalse(av.getComponents().get(index).getCovered());
    }
}