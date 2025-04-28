package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
    }

    @Test
    void getGoodsInBank() {
        assertEquals(12,bank.getGoodsInBank(1));
        assertEquals(17,bank.getGoodsInBank(2));
        assertEquals(13,bank.getGoodsInBank(3));
        assertEquals(14,bank.getGoodsInBank(4));
    }

    @Test
    void getGoods() {
        int[] goods = new int[]{12,17,13,14};
        assertEquals(12,bank.getGoods()[0]);
        assertEquals(17,bank.getGoods()[1]);
        assertEquals(13,bank.getGoods()[2]);
        assertEquals(14,bank.getGoods()[3]);
    }

    @Test
    void addGoodsInBank() {
        bank.addGoodsInBank(1);
        assertEquals(13, bank.getGoodsInBank(1));
    }

    @Test
    void removeGoodsInBank() {
        bank.removeGoodsInBank(1);
        assertEquals(11, bank.getGoodsInBank(1));
    }
}