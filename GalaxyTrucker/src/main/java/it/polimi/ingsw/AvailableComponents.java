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

    Component pipes1 = new Pipes(3, 2, 1, 3, "Pipes");
    Component pipes2 = new Pipes(3, 1, 2, 3, "Pipes");
    Component pipes3 = new Pipes(1, 3, 0, 3, "Pipes");
    Component pipes4 = new Pipes(3, 2, 2, 3, "Pipes");
    Component pipes5 = new Pipes(2, 3, 0, 3, "Pipes");
    Component pipes6 = new Pipes(1, 2, 1, 3, "Pipes");
    Component pipes7 = new Pipes(1, 3, 2, 3, "Pipes");
    Component pipes8 = new Pipes(3, 3, 0, 1, "Pipes");

    Component shield1 = new Shield(2, 1, 2, 1, "Shield");
    Component shield2 = new Shield(0, 2, 2, 3, "Shield");
    Component shield3 = new Shield(0, 1, 3, 1, "Shield");
    Component shield4 = new Shield(1, 0, 1, 1, "Shield");
    Component shield5 = new Shield(0, 0, 3, 2, "Shield");
    Component shield6 = new Shield(0, 2, 2, 2, "Shield");
    Component shield7 = new Shield(1, 2, 1, 2, "Shield");
    Component shield8 = new Shield(0, 0, 1, 3, "Shield");

    Component lifeSupportB1 = new LifeSupport(1,1,0,1, "Life Support", LifeSupportColor.BROWN);
    Component lifeSupportB2 = new LifeSupport(0,0,1,3, "Life Support", LifeSupportColor.BROWN);
    Component lifeSupportB3 = new LifeSupport(0,0,0,3, "Life Support", LifeSupportColor.BROWN);
    Component lifeSupportB4 = new LifeSupport(1,0,0,3, "Life Support", LifeSupportColor.BROWN);
    Component lifeSupportB5 = new LifeSupport(0,3,0,2, "Life Support", LifeSupportColor.BROWN);
    Component lifeSupportB6 = new LifeSupport(2,1,0,1, "Life Support", LifeSupportColor.BROWN);

    Component lifeSupportP1 = new LifeSupport(2,2,0,2, "Life Support", LifeSupportColor.PURPLE);
    Component lifeSupportP2 = new LifeSupport(1,2,0,2, "Life Support", LifeSupportColor.PURPLE);
    Component lifeSupportP3 = new LifeSupport(0,0,0,3, "Life Support", LifeSupportColor.PURPLE);
    Component lifeSupportP4 = new LifeSupport(0,1,0,3, "Life Support", LifeSupportColor.PURPLE);
    Component lifeSupportP5 = new LifeSupport(2,0,0,3, "Life Support", LifeSupportColor.PURPLE);
    Component lifeSupportP6 = new LifeSupport(0,0,2,3, "Life Support", LifeSupportColor.PURPLE);

    Component specialLoad1 = new Load(2, 2, 2, 3, "Load", 1, true);
    Component specialLoad2 = new Load(0, 2, 1, 3, "Load", 1, true);
    Component specialLoad3 = new Load(3, 0, 0, 3, "Load", 1, true);
    Component specialLoad4 = new Load(0, 3, 0, 3, "Load", 1, true);
    Component specialLoad5 = new Load(1, 1, 1, 3, "Load", 1, true);
    Component specialLoad6 = new Load(2, 1, 0, 3, "Load", 1, true);
    Component specialLoad7 = new Load(0, 2, 0, 1, "Load", 2, true);
    Component specialLoad8 = new Load(0, 0, 0, 2, "Load", 2, true);
    Component specialLoad9 = new Load(0, 0, 0, 1, "Load", 2, true);

    Component load1 = new Load(0, 0, 0, 3, "Load", 2, false);













}
