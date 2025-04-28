package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck=new Deck();
    }

    @Test
    void getCard() {
        assertNotNull(deck.getCard(3));
    }

    @Test
    void getDeck() {
        assertNotNull(deck.getDeck());
    }
}