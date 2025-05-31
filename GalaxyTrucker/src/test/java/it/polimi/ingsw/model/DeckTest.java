package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck1;
    private Deck deck2;

    @BeforeEach
    void setUp() {
        deck1=new Deck(0);
        deck2=new Deck(2);
    }

    @Test
    void getCard() {
        assertNotNull(deck1.getCard(3));
        assertNotNull(deck2.getCard(3));
    }

    @Test
    void getDeck() {
        assertNotNull(deck1.getDeck());
        assertEquals(8,deck1.getDeck().size());
        assertNotNull(deck2.getDeck());
        assertEquals(40,deck2.getDeck().size());
    }
}