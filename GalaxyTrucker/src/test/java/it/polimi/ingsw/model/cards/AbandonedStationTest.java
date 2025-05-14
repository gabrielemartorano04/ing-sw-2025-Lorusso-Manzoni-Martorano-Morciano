package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationTest {

    private AbandonedStation card;
    ArrayList<Goods> goods;

    @BeforeEach
    void setUp() {
        goods = new ArrayList<>();
        card=new AbandonedStation(false,1,goods,1,1,"/it/polimi/ingsw/model/cards/GT-cards_I_IT_0117.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0117.jpg");
    }

    @Test
    void accept() {
    }

    @Test
    void testAccept() {
    }

    @Test
    void getDays() {
        assertEquals(1,card.getDays());
    }

    @Test
    void getNumHuman() {
        assertEquals(1,card.getNumHuman());
    }

    @Test
    void getGoods() {
        assertNotNull(card.getGoods());
        assertEquals(goods,card.getGoods());
    }

    @Test
    void truckerLanding() {

    }

    @Test
    void setChoiceToGain() {
        card.setCardState(CardState.CHOOSE_TO_GAIN);
        try {
            assertNull(card.setChoiceToGain(false));
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void setWinnerCoordinatesToAddGoods() {
    }

    @Test
    void rearrange() {
    }

    @Test
    void cardEffect() {
    }

    @Test
    void getAndSetCardState() {
        card.setCardState(CardState.ESECUZIONE);
        assertEquals(card.getCardState(),CardState.ESECUZIONE);
    }

}
