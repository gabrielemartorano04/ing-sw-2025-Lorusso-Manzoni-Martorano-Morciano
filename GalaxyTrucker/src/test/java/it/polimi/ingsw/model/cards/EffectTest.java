package it.polimi.ingsw.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EffectTest {

    Effect effect;
    @BeforeEach
    void setUp() {
        effect=new Effect(TypeOfEffect.GET_SHOT,1);
    }

    @Test
    void getType() {
        assertEquals(effect.getType(),TypeOfEffect.GET_SHOT);
    }

    @Test
    void getValue() {
        assertEquals(1,effect.getValue());
    }

    @Test
    void testToString() {
        assertNotNull(effect.toString());
    }
}