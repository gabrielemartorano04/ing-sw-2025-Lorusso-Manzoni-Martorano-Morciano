package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoodsTest {

    private Goods goods;

    @BeforeEach
    void setUp() {
        goods=new Goods(4);
    }

    @Test
    void getValue() {
        assertEquals(4,goods.getValue());
    }

    @Test
    void setAndGetX() {
        goods.setX(2);
        assertEquals(2,goods.getX());
    }

    @Test
    void setAndGetY() {
        goods.setY(2);
        assertEquals(2,goods.getY());
    }

    @Test
    void getColor() {
        assertEquals("red",goods.getColor());
    }

}