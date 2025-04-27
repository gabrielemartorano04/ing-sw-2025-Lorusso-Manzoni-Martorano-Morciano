package it.polimi.ingsw.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmugglersTest {

    private Smugglers card;

    @BeforeEach
    void setUp() {
        card=new Smugglers(false,2,2,1,null,1,"","");
    }

    @Test
    void accept() {
    }

    @Test
    void testAccept() {
    }

    @Test
    void getSmugglersPower() {
        assertEquals(2,card.getSmugglersPower());
    }

    @Test
    void getGoodsLost() {
        assertEquals(2,card.getGoodsLost());
    }

    @Test
    void getGoodsGained() {
        assertNull(card.getGoodsGained());
    }

    @Test
    void getDays() {
        assertEquals(1,card.getDays());
    }

    @Test
    void setDefeatedTruckers() {
    }

    @Test
    void setWinnerTrucker() {
    }

    @Test
    void addDoubleCannonsCoord() {
    }

    @Test
    void plotCurrGood() {
    }

    @Test
    void setChoiceToGain() {
    }

    @Test
    void setWinnerCoordinatesToAddGoods() {
    }

    @Test
    void rearrange() {
    }

    @Test
    void manageRemoveGoods() {
    }

    @Test
    void plotGoodsList() {
    }

    @Test
    void removeAsking() {
    }

    @Test
    void getNumOfBatteriesToRemove() {
    }

    @Test
    void removeBatteries() {
    }

    @Test
    void cardEffect() {
    }
}