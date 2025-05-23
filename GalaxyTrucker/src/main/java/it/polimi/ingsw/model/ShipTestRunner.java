package it.polimi.ingsw.model;

import it.polimi.ingsw.model.components.Cabin;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.EmptySpace;

//TODO: FINIRE LA CLASSE
public class ShipTestRunner {

    private Component[][] components = new Component[15][15];
    private int lost = 0;

    public static void main(String[] args) {
        ShipTestRunner ship = new ShipTestRunner();
        ship.run();
    }

    public void run() {
        // Riempie una nave completamente connessa
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                // TODO: creare una nave con un po di componenti
            }
        }
        // TODO: components[7][7] = new Cabin(); // centro nave

        System.out.println("Nave prima della verifica:");
        printComponents();

        boolean result = wellDone();
        System.out.println("\nRisultato di wellDone(): " + result);

        System.out.println("\nNave dopo la verifica:");
        printComponents();
    }

    public boolean wellDone() {
        boolean wellDone = true;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null && !isValid(i, j)) {
                    removeComponent(i, j);
                    wellDone = false;
                }
            }
        }
        if (!wellDone) {
            componentsVisited(7, 7);
            removeUnlinkedComponents();
        }
        return wellDone;
    }

    private boolean isValid(int i, int j) {
        // Simula validità: considera tutto valido tranne le EmptySpace
        return !(components[i][j] instanceof EmptySpace);
    }

    private void removeComponent(int i, int j) {
        components[i][j] = null;
    }

    private void componentsVisited(int x, int y) {
        if (x < 0 || x >= components.length || y < 0 || y >= components[0].length) {
            return;
        }
        if (components[x][y] == null || (components[x][y].isVisited() && (x != 7 || y != 7))) {
            return;
        }
        if (components[x][y] instanceof EmptySpace) {
            return;
        }
        if (components[6][7] != null && components[7][6] != null &&
                components[8][7] != null && components[7][8] != null) {
            if (components[6][7].isVisited() && components[7][6].isVisited() &&
                    components[8][7].isVisited() && components[7][8].isVisited()) {
                components[7][7].setVisited(true);
            }
        }

        components[x][y].setVisited(true);
        componentsVisited(x + 1, y);
        componentsVisited(x - 1, y);
        componentsVisited(x, y + 1);
        componentsVisited(x, y - 1);
    }

    private void removeUnlinkedComponents() {
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null && !(components[i][j] instanceof EmptySpace)) {
                    if (!components[i][j].isVisited()) {
                        removeComponent(i, j);
                        lost++;
                    }
                }
            }
        }
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    components[i][j].setVisited(false);
                }
            }
        }
    }

    private void printComponents() {
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] == null) {
                    System.out.print(" . ");
                } else if (components[i][j] instanceof Cabin) {
                    System.out.print(" M ");
                } else if (components[i][j] instanceof EmptySpace) {
                    System.out.print("   ");
                } else {
                    System.out.print(" X ");
                }
            }
            System.out.println();
        }
    }
}