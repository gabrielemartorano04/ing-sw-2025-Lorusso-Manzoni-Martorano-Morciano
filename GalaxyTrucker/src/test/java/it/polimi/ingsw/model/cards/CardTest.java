package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    Card card;

    @BeforeEach
    void setUp() {
        card=new AbandonedShip( false, 1,1,1,1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0117.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0117.jpg");
    }

    @Test
    void accept() {
    }

    @Test
    void testAccept() {
    }

    @Test
    void getName() {
        assertEquals(card.getName(),"Abandoned Ship");
    }

    @Test
    void getDescription() {
        assertEquals(card.getDescription(),"A drifting ghost ship");
    }

    @Test
    void getLevel() {
        assertEquals(card.getLevel(),1);
    }

    @Test
    void isTrialFlight() {
        assertFalse(card.isTrialFlight());
    }

    @Test
    void getFrontImage() {
//        ImageIcon image=new ImageIcon(getClass().getResource("/it/polimi/ingsw/model/cards/GT-cards_I_IT_0117.jpg"));
//        assertEquals(card.getFrontImage(),image);
    }

    @Test
    void getBackImage() {
//        ImageIcon image=new ImageIcon(getClass().getResource("/it/polimi/ingsw/model/cards/GT-cards_I_IT_0117.jpg"));
//        assertEquals(card.getBackImage(),image);
    }

    @Test
    void cardEffect() {
    }
}