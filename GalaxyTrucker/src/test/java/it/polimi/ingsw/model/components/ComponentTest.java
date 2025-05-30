package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {

    private Component component;

    @BeforeEach
    void setUp() {
        component=new Engine(1,1,1,"Engine","/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web.jpg",true);
    }

    @Test
    void getFrontImage() {
//        Image image = new ImageIcon(Objects.requireNonNull(getClass().getResource("it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web.jpg"))).getImage();
//        assertEquals(image,component.getFrontImage());
    }

    @Test
    void setAndGetVisited() {
        component.setVisited(true);
        assertTrue(component.isVisited());
    }

    @Test
    void getBackImage() {
//        ImageIcon image=new ImageIcon(Objects.requireNonNull(getClass().getResource("it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web.jpg")));
//        assertEquals(image,component.getBackImage());
    }

    @Test
    void setAndGetSelected() {
        component.setSelected(true);
        assertTrue(component.getSelected());
    }

    @Test
    void setAndGetCovered() {
        component.setCovered(true);
        assertTrue(component.getCovered());
    }

    @Test
    void getLink() {
        assertEquals(1,component.getLink()[0]);
        assertEquals(1,component.getLink()[1]);
    }


    @Test
    void turn() {
        component.turn();
        assertEquals(1,component.getLink()[0]);
        assertEquals(1,component.getLink()[1]);
        assertEquals(1,component.getLink()[2]);
    }

    @Test
    void getType() {
        assertEquals("Engine",component.getType());
    }
}