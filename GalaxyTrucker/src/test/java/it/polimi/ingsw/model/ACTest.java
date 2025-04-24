package it.polimi.ingsw.model;

import it.polimi.ingsw.model.components.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ACTest {
    private AvailableComponents availableComponents;

    @Before
    public void setUp() {
        this.availableComponents = new AvailableComponents();
    }

    @Test
    public void testConstructorInitializesComponents() {
        ArrayList<Component> components = availableComponents.getComponents();
        assertNotNull("La lista di componenti non dovrebbe essere null", components);
        assertTrue("Dovrebbero esserci almeno alcuni componenti inizializzati", components.size() > 0);
    }

    @Test
    public void testRemoveComponent() {
        ArrayList<Component> components = availableComponents.getComponents();
        Component firstComponent = components.get(0);
        availableComponents.remove(firstComponent);
        assertFalse("Il componente dovrebbe essere stato rimosso", availableComponents.getComponents().contains(firstComponent));
    }

    @Test
    public void testShuffleChangesOrder() {
        ArrayList<Component> beforeShuffle = new ArrayList<>(availableComponents.getComponents());
        availableComponents.shuffle();
        ArrayList<Component> afterShuffle = availableComponents.getComponents();

        // Controllo che l'ordine non sia esattamente lo stesso (non sempre garantito)
        boolean isSameOrder = true;
        for (int i = 0; i < beforeShuffle.size(); i++) {
            if (beforeShuffle.get(i) != afterShuffle.get(i)) {
                isSameOrder = false;
                break;
            }
        }
        assertFalse("La lista dovrebbe essere stata mescolata", isSameOrder);
    }
}
