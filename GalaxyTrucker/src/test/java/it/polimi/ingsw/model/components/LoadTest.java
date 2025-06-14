package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoadTest {

    private Load load;

    @BeforeEach
    void setUp() {
        load=new Load(1,1,1,1,"Load","",1,true);
    }

    @Test
    void getIsSpecial() {
        assertTrue(load.getIsSpecial());
    }

    @Test
    void getCapacity() {
        assertEquals(1,load.getCapacity());
    }

    @Test
    void getQuantity() {
        assertEquals(0,load.getQuantity());
    }

    @Test
    void addGood() {
        load.addGood(0,new Goods(2));
        assertEquals(1,load.getQuantity());
    }

    @Test
    void getGoods() {
        load.addGood(0,new Goods(2));
        assertEquals(1,load.getGoods().size());
    }
}