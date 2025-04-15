package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.model.cards.Card;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CardControllerTest {
    CardController cardController = new CardController();
    @Test
    public void getCardName() {
    }

    @Test
    public void getCardDescription() {
    }

    @Test
    public void getCardLevel() {
    }

    @Test
    public void isTrialFlight() {
    }

    @Test
    public void getCardFrontImage() {
    }

    @Test
    public void getCardBackImage() {
    }

    @Test
    public void applyEffects() {
    }

    @Test
    public void getDays() {
        Card card = new AbandonedShip(false,2,2,3,4,"","");
        assertEquals(cardController.getDays(card),4);

    }
}