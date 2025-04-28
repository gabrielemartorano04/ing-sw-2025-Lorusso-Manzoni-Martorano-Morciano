package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HourglassTest {

    private Hourglass hourglass;
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        hourglass = new Hourglass(game);
    }

    @Test
    void getTimeTurns() {
        assertEquals(0, hourglass.getTimeTurns());
        hourglass.increaseTimeTurns();
        assertEquals(1, hourglass.getTimeTurns());
    }

    @Test
    void timeIsUp() {
        Object lock = new Object();
        synchronized (lock) {
            try {
                lock.wait(45000); // aspetta 45 secondi
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertFalse(hourglass.timeIsUp());
        synchronized (lock) {
            try {
                lock.wait(45000); // aspetta 45 secondi
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertTrue(hourglass.timeIsUp());
    }

    @Test
    void increaseTimeTurns() {
        hourglass.increaseTimeTurns();
        assertEquals(1, hourglass.getTimeTurns());
    }

    @Test
    void reset() {
        Object lock = new Object();
        assertEquals(0, hourglass.getTimeTurns());
        synchronized (lock) {
            try {
                lock.wait(91000); // aspetta 90 secondi
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertTrue(hourglass.timeIsUp());
        hourglass.reset();
        assertFalse(hourglass.timeIsUp());
        assertEquals(1, hourglass.getTimeTurns());
        synchronized (lock) {
            try {
                lock.wait(91000); // aspetta 90 secondi
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertTrue(hourglass.timeIsUp());
        hourglass.reset();
        assertFalse(hourglass.timeIsUp());
        assertEquals(2, hourglass.getTimeTurns());
    }
}