package it.polimi.ingsw;

import java.util.Random;
import javax.swing.ImageIcon;

public class AvailableComponents {
    private Component[] components;
    private final ImageIcon image;
    private int[] link;

    public AvailableComponents(ImageIcon image) {
        this.components = new Component[156];
        this.image = image;
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

    public void remove(Component component) {
        int index = -1;
        for (int i = 0; i < components.length; i++) {
            if (components[i].equals(component)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            return;
        }
        Component[] newComponents = new Component[components.length - 1];
        System.arraycopy(components, 0, newComponents, 0, index);
        System.arraycopy(components, index + 1, newComponents, index, components.length - index - 1);
        components = newComponents;
    }

    Component Cabin1 = new Component(0, 0, 3, 1, "Cabin");
    Component Cabin2 = new Component(1, 0, 2, 3, "Cabin");
    Component Cabin3 = new Component(2, 0, 0, 1, "Cabin");
    Component Cabin4 = new Component(2, 2, 0, 3, "Cabin");
    Component Cabin5 = new Component(1, 2, 1, 1, "Cabin");
    Component Cabin6 = new Component(2, 1, 2, 2, "Cabin");
}
