package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PipesTest {

    private Pipes pipes;

    @BeforeEach
    void setUp() {
        pipes=new Pipes(3,3,3,3,"Pipes","");
    }

    @Test
    void testPipes(){
        assertNotNull(pipes);
    }
}