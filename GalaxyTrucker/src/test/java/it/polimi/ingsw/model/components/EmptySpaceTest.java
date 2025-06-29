package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmptySpaceTest {

    private EmptySpace emptySpace;

    @BeforeEach
    void setUp() {
        emptySpace = new EmptySpace();
    }

    @Test
    void testEmptySpace() {
        assertNotNull(emptySpace);
    }
}