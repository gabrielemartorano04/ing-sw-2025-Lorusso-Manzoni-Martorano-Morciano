package it.polimi.ingsw.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HTest {
    private Hourglass hourglass;

    @Before
    public void setUp() {
        hourglass = new Hourglass(null);
    }

    @Test
    public void testInitialValues() {
        assertEquals("Il tempo iniziale deve essere 0 turni", 0, hourglass.getTimeTurns());
        assertFalse("Il timer non dovrebbe essere scaduto all'inizio", hourglass.timeIsUp());
    }

    @Test
    public void testIncreaseTimeTurns() {
        hourglass.increaseTimeTurns();
        hourglass.increaseTimeTurns();
        assertEquals("Dovrebbe essere stato incrementato di 2", 2, hourglass.getTimeTurns());
    }

    @Test
    public void testTimeIsUpAfterWait() throws InterruptedException {
        Thread.sleep(91000); // 91 secondi per sicurezza
        assertTrue("Dopo 90 secondi il tempo dovrebbe essere scaduto", hourglass.timeIsUp());
    }

    @Test
    public void testResetWhenTimeIsUp() throws InterruptedException {
        Thread.sleep(91000);
        assertTrue(hourglass.timeIsUp());
        int oldTurns = hourglass.getTimeTurns();
        hourglass.reset();
        Thread.sleep(100); // Aspetta un attimo per far ripartire il timer
        assertEquals("I turni dovrebbero essere incrementati di 1", oldTurns + 1, hourglass.getTimeTurns());
        assertFalse("Dopo il reset il tempo non deve essere scaduto", hourglass.timeIsUp());
    }

    @Test
    public void testResetWhenTimerStillRunning() {
        int oldTurns = hourglass.getTimeTurns();
        hourglass.reset();
        assertEquals("I turni non dovrebbero cambiare se il timer non è scaduto", oldTurns, hourglass.getTimeTurns());
    }
}
