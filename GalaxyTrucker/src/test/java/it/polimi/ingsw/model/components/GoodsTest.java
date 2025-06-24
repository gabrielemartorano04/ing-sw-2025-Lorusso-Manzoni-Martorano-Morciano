package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoodsTest {

    private Goods goods,good2,good3,good1;

    @BeforeEach
    void setUp() {
        goods=new Goods(4);
        good2=new Goods(2);
        good3=new Goods(3);
        good1=new Goods(1);
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
        assertEquals("blue",good1.getColor());
        assertEquals("green",good2.getColor());
        assertEquals("yellow",good3.getColor());

        Goods nonValid=new Goods(8);
        assertEquals("",nonValid.getColor());
    }

}