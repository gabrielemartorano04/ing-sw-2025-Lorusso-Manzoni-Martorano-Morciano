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

    Component cabin1 = new Cabin(0, 0, 3, 1, "Cabin", false);
    Component cabin2 = new Cabin(1, 0, 2, 3, "Cabin", false);
    Component cabin3 = new Cabin(2, 0, 0, 1, "Cabin", false);
    Component cabin4 = new Cabin(2, 2, 0, 3, "Cabin", false);
    Component cabin5 = new Cabin(1, 2, 1, 1, "Cabin", false);
    Component cabin6 = new Cabin(2, 1, 2, 2, "Cabin", false);
    Component cabin7 = new Cabin(2, 0, 2, 3, "Cabin", false);
    Component cabin8 = new Cabin(0, 1, 1, 3, "Cabin", false);
    Component cabin9 = new Cabin(2, 1, 0, 1, "Cabin", false);
    Component cabin10 = new Cabin(1, 0, 1, 3, "Cabin", false);
    Component cabin11 = new Cabin(0, 1, 0, 3, "Cabin", false);
    Component cabin12 = new Cabin(0, 2, 0, 3, "Cabin", false);
    Component cabin13 = new Cabin(1, 2, 3, 1, "Cabin", false);
    Component cabin14 = new Cabin(2, 1, 2, 1, "Cabin", false);
    Component cabin15 = new Cabin(0, 1, 3, 2, "Cabin", false);
    Component cabin16 = new Cabin(3, 0, 0, 2, "Cabin", false);

    Component mainCabinR = new Cabin(3, 3, 3, 3, "Cabin", true);
    Component mainCabinG = new Cabin(3, 3, 3, 3, "Cabin", true);
    Component mainCabinB = new Cabin(3, 3, 3, 3, "Cabin", true);
    Component mainCabinY = new Cabin(3, 3, 3, 3, "Cabin", true);






}
