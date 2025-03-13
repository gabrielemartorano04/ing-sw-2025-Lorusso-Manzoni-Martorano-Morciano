package it.polimi.ingsw;

import java.util.Random;

public class AvailableComponents {
    private Component[] components;

    public AvailableComponents() {
        this.components = new Component[0];
    }

    public Component[] getComponents() {
        return components;
    }

    public void shuffle() {
        Random random = new Random();
        for (int i = components.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);  // Indice casuale tra 0 e i
            Component tmp = components[i];
            components[i] = components[j];
            components[j] = tmp;
        }
    }

    public void remove() {
        ;//come si fa?
    }
}
