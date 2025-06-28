package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Component class test
 */
class ComponentTest {

    /**
     * Represents an instance of the Component class used in testing
     */
    private Component component;

    /**
     * Sets up the test environment by initializing the necessary resources
     */
    @BeforeEach
    void setUp() {
        component=new Engine(1,1,1,"Engine","/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web.jpg",true);
    }

    /**
     * This method ensures that the front image of the component is not null
     * when returned through the `getFrontImage` method.
     */
    @Test
    void getFrontImage() {
        assertNotNull(component.getFrontImage());
    }

    /**
     * This method verifies that when the `setVisited` method is invoked with a
     * value of `true`, the `isVisited` method correctly reflects this change by returning `true`.
     */
    @Test
    void setAndGetVisited() {
        component.setVisited(true);
        assertTrue(component.isVisited());
    }

    /**
     * This method ensures that the rear image of the component is not null when returned.
     */
    @Test
    void getBackImage() {
        assertNotNull(component.getBackImage());
    }

    /**
     * It ensures that when the selection state of the component is set using the `setSelected`
     * method, the `getSelected` method correctly retrieves the updated state
     */
    @Test
    void setAndGetSelected() {
        component.setSelected(true);
        assertTrue(component.getSelected());
    }

    /**
     * This method verifies that when the `setCovered` method is invoked with a value of `true`,
     * the `getCovered` method correctly reflects this change by returning `true`.
     */
    @Test
    void setAndGetCovered() {
        component.setCovered(true);
        assertTrue(component.getCovered());
    }

    /**
     * Tests the `getLink` method of the `Component` class to verify
     * that the links on each side are correctly returned as an array
     */
    @Test
    void getLink() {
        assertEquals(1,component.getLink()[0]);
        assertEquals(1,component.getLink()[1]);
    }

    /**
     * The `turn` method rotates the component clockwise, which reassigns the link values
     * in accordance with the rotation
     */
    @Test
    void turn() {
        component.turn();
        assertEquals(1,component.getLink()[0]);
        assertEquals(1,component.getLink()[1]);
        assertEquals(1,component.getLink()[2]);
    }

    /**
     * Tests the `getType` method of the `Component` class to validate that it correctly
     * returns the type of the component as specified during its initialization
     */
    @Test
    void getType() {
        assertEquals("Engine",component.getType());
    }

    /**
     * It ensures that the returned rotation angle of the component matches the
     * expected value
     */
    @Test
    void getRotationAngle(){
        assertEquals(0,component.getRotationAngle());
    }

    /**
     * This test method verifies that the `toString` method of the `component` instance
     * returns a non-null value
     */
    @Test
    void toStringTest(){
        assertNotNull(component.toString());
    }
}