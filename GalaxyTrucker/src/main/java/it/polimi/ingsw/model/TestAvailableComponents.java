package it.polimi.ingsw.model;

import it.polimi.ingsw.model.components.*;
import java.util.*;

public class TestAvailableComponents {

    public static void main(String[] args) {
        AvailableComponents available = new AvailableComponents();

        ArrayList<Component> components = available.getComponents();

        System.out.println("Numero totale di componenti: " + components.size());

        // 1. Verifica che gli oggetti Cabin abbiano riferimenti diversi
        Set<Component> cabinSet = new HashSet<>();
        int cabinCount = 0;

        for (Component c : components) {
            if (c instanceof Cabin) {
                cabinCount++;
                if (!cabinSet.add(c)) {
                    System.out.println("Trovato riferimento duplicato per Cabin!");
                }
            }
        }

        System.out.println("Numero totale di Cabin: " + cabinCount);
        System.out.println("Numero Cabin con riferimenti unici: " + cabinSet.size());

        // 2. Prendi una Cabin, fai setSelected(true) e controlla che le altre Cabin restino false
        Component target = null;
        int targetIndex = -1;

        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (c instanceof Cabin) {
                target = c;
                targetIndex = i;
                break;
            }
        }

        if (target == null) {
            System.out.println("Nessuna Cabin trovata per il test.");
            return;
        }

        target.setSelected(true);
        System.out.println("Impostato selected=true sulla Cabin all'indice: " + targetIndex);

        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (c instanceof Cabin) {
                System.out.println("Cabin index " + i + ": selected = " + c.getSelected());
                if (i != targetIndex && c.getSelected()) {
                    System.out.println("❌ ERRORE: Cabin all'indice " + i + " è selected = true senza motivo.");
                }
            }
        }

        // 3. Testa removeComponent
        int initialSize = components.size();
        Component removed = components.get(0);
        available.removeComponent(0);
        int newSize = components.size();
        System.out.println("Componente rimosso: " + removed);
        System.out.println("Taglia prima: " + initialSize + ", dopo: " + newSize);
        assert newSize == initialSize - 1;

        // 4. Testa addComponent
        Component newComponent = new Cabin(9, 9, 9, 9, "CabinTest", "/fake/path.jpg", null, false);
        available.addComponent(newComponent);
        if (components.get(0) == newComponent) {
            System.out.println("✅ Componente CabinTest aggiunto all'inizio della lista.");
        } else {
            System.out.println("❌ Errore: il componente aggiunto non è in prima posizione.");
        }

        System.out.println("Test terminato.");
    }
}
