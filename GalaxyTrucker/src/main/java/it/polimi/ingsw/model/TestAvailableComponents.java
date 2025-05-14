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
        int batteryCount = 0;
        int cannonSingle = 0;
        int cannonDouble = 0;
        int engineSingle = 0;
        int engineDouble = 0;
        int emptySpace = 0;
        int lifeCount = 0;
        int loadCount = 0;
        int pipesCount = 0;
        int shieldCount = 0;
        int h = 0;
        for (Component c : components) {
            if (c instanceof Cabin) {
                cabinCount++;
            } else if (c instanceof BatteryStorage) {
                batteryCount++;
            } else if (c instanceof Cannon) {
                if (((Cannon) c).getIsDouble()) {
                    cannonDouble++;
                } else {
                    cannonSingle++;
                }
            } else if (c instanceof Engine) {
                if (((Engine) c).getIsDouble()) {
                    engineDouble++;
                } else {
                    engineSingle++;
                }
            } else if (c instanceof EmptySpace) {
                emptySpace++;
            } else if (c instanceof LifeSupport) {
                lifeCount++;
            } else if (c instanceof Load) {
                loadCount++;
            } else if (c instanceof Pipes) {
                pipesCount++;
            } else if (c instanceof Shield) {
                shieldCount++;
            }

            h++;
        }

        System.out.println("Numero totale di Cabin: " + cabinCount);
        System.out.println("Numero totale di BatteryStorage: " + batteryCount);
        System.out.println("Numero totale di Cannoni Singoli: " + cannonSingle);
        System.out.println("Numero totale di Cannoni Doppi: " + cannonDouble);
        System.out.println("Numero totale di Motori Singoli: " + engineSingle);
        System.out.println("Numero totale di Motori Doppi: " + engineDouble);
        System.out.println("Numero totale di Spazi Vuoti: " + emptySpace);
        System.out.println("Numero totale di LifeSupport: " + lifeCount);
        System.out.println("Numero totale di Load: " + loadCount);
        System.out.println("Numero totale di Pipes: " + pipesCount);
        System.out.println("Numero totale di Shield: " + shieldCount);
        System.out.println("Somma counter: " + (loadCount + pipesCount + shieldCount + emptySpace + cabinCount + batteryCount + cannonSingle + cannonDouble + engineSingle + engineDouble + lifeCount));

        System.out.println("Numero Cabin con riferimenti unici: " + cabinSet.size());

//        // 2. Prendi una Cabin, fai setSelected(true) e controlla che le altre Cabin restino false
//        Component target = null;
//        int targetIndex = -1;
//
//        for (int i = 0; i < components.size(); i++) {
//            Component c = components.get(i);
//            if (c instanceof Cabin) {
//                target = c;
//                targetIndex = i;
//                break;
//            }
//        }
//
//        if (target == null) {
//            System.out.println("Nessuna Cabin trovata per il test.");
//            return;
//        }
//
//        target.setSelected(true);
//        System.out.println("Impostato selected=true sulla Cabin all'indice: " + targetIndex);
//
//        for (int i = 0; i < components.size(); i++) {
//            Component c = components.get(i);
//            if (c instanceof Cabin) {
//                System.out.println("Cabin index " + i + ": selected = " + c.getSelected());
//                if (i != targetIndex && c.getSelected()) {
//                    System.out.println("❌ ERRORE: Cabin all'indice " + i + " è selected = true senza motivo.");
//                }
//            }
//        }
//
//        // 3. Testa removeComponent
//        int initialSize = components.size();
//        Component removed = components.get(0);
//        available.removeComponent(0);
//        int newSize = components.size();
//        System.out.println("Componente rimosso: " + removed);
//        System.out.println("Taglia prima: " + initialSize + ", dopo: " + newSize);
//        assert newSize == initialSize - 1;
//
//        // 4. Testa addComponent
//        Component newComponent = new Cabin(9, 9, 9, 9, "CabinTest", "/fake/path.jpg", null, false);
//        available.addComponent(newComponent);
//        if (components.get(0) == newComponent) {
//            System.out.println("✅ Componente CabinTest aggiunto all'inizio della lista.");
//        } else {
//            System.out.println("❌ Errore: il componente aggiunto non è in prima posizione.");
//        }
//
//        System.out.println("Test terminato.");
    }
}
