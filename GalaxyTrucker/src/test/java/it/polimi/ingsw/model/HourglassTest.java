package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class HourglassTest {

    @BeforeEach
    void setUp() {
        Hourglass hourglass=new Hourglass();
        hourglass.reset();
        hourglass.increaseTimeTurns();
    }

    @Test
    void getTimeTurnsAndIncresaseTimeTurns() {
        Hourglass hourglass=new Hourglass();
        assertEquals(0,hourglass.getTimeTurns());
        hourglass.increaseTimeTurns();
        assertEquals(1,hourglass.getTimeTurns());
    }

    @Test
    void timeIsUp() {
        Object lock = new Object();
        Hourglass hourglass = new Hourglass();
        synchronized (lock) {
            try {
                lock.wait(45000); // waits 45 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertFalse(hourglass.timeIsUp());
        synchronized (lock) {
            try {
                lock.wait(45000); // waits 45 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertTrue(hourglass.timeIsUp());
    }

    @Test
    void reset() {
        Object lock = new Object();
        Hourglass hourglass = new Hourglass();
        assertEquals(0, hourglass.getTimeTurns());
        synchronized (lock) {
            try {
                lock.wait(91000); // wait 90 seconds
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
                lock.wait(91000); // waits 90 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertTrue(hourglass.timeIsUp());
        hourglass.reset();
        assertFalse(hourglass.timeIsUp());
        assertEquals(2, hourglass.getTimeTurns());
    }

    @Test
    void getSecondiRimanenti() {
        Object lock=new Object();
        Hourglass hourglass=new Hourglass();
        hourglass.reset();
        synchronized (lock){
            try {
                lock.wait(90000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(0, hourglass.getSecondsLeft());
    }
}