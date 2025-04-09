package it.polimi.ingsw;

import it.polimi.ingsw.Model.AvailableComponents;
import it.polimi.ingsw.Model.Components.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AvailableComponentsTest {
    private AvailableComponents availableComponents;

    @BeforeEach
    void setUp() {
        availableComponents = new AvailableComponents();
    }

    @Test
    void testInitializationNotEmpty() {
        ArrayList<Component> components = availableComponents.getComponents();
        assertNotNull(components, "La lista di componenti non dovrebbe essere null.");
        assertFalse(components.isEmpty(), "La lista di componenti non dovrebbe essere vuota.");
    }

    @Test
    void testRemoveComponent() {
        Component toRemove = availableComponents.getComponents().get(0);
        int initialSize = availableComponents.getComponents().size();
        availableComponents.remove(toRemove);
        int newSize = availableComponents.getComponents().size();

        assertEquals(initialSize - 1, newSize, "La dimensione dovrebbe diminuire di 1 dopo la rimozione.");
        assertFalse(availableComponents.getComponents().contains(toRemove), "Il componente dovrebbe essere stato rimosso.");
    }

    @Test
    void testShuffleChangesOrder() {
        ArrayList<Component> original = new ArrayList<>(availableComponents.getComponents());
        availableComponents.shuffle();
        ArrayList<Component> shuffled = availableComponents.getComponents();

        // Nota: Il test può occasionalmente fallire se lo shuffle restituisce lo stesso ordine per puro caso.
        boolean isOrderChanged = false;
        for (int i = 0; i < original.size(); i++) {
            if (!original.get(i).equals(shuffled.get(i))) {
                isOrderChanged = true;
                break;
            }
        }

        assertTrue(isOrderChanged, "Dopo lo shuffle, l'ordine dovrebbe essere cambiato.");
    }
}
