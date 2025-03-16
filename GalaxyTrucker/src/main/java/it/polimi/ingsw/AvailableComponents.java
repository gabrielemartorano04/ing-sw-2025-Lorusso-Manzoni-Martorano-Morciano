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

    Component cabin1 = new Cabin(0, 0, 3, 1, "Cabin", "imagePath", null, false);
    Component cabin2 = new Cabin(1, 0, 2, 3, "Cabin", "imagePath", null, false);
    Component cabin3 = new Cabin(2, 0, 0, 1, "Cabin", "imagePath", null, false);
    Component cabin4 = new Cabin(2, 2, 0, 3, "Cabin", "imagePath", null, false);
    Component cabin5 = new Cabin(1, 2, 1, 1, "Cabin", "imagePath", null, false);
    Component cabin6 = new Cabin(2, 1, 2, 2, "Cabin", "imagePath", null, false);
    Component cabin7 = new Cabin(2, 0, 2, 3, "Cabin", "imagePath", null, false);
    Component cabin8 = new Cabin(0, 1, 1, 3, "Cabin", "imagePath", null, false);
    Component cabin9 = new Cabin(2, 1, 0, 1, "Cabin", "imagePath", null, false);
    Component cabin10 = new Cabin(1, 0, 1, 3, "Cabin", "imagePath", null, false);
    Component cabin11 = new Cabin(0, 1, 0, 3, "Cabin", "imagePath", null, false);
    Component cabin12 = new Cabin(0, 2, 0, 3, "Cabin", "imagePath", null, false);
    Component cabin13 = new Cabin(1, 2, 2, 1, "Cabin", "imagePath", null, false);
    Component cabin14 = new Cabin(2, 1, 2, 1, "Cabin", "imagePath", null, false);
    Component cabin15 = new Cabin(0, 1, 3, 2, "Cabin", "imagePath", null, false);
    Component cabin16 = new Cabin(1, 2, 0, 2, "Cabin", "imagePath", null, false);
    Component cabin17 = new Cabin(3, 0, 0, 2, "Cabin", "imagePath", null, false);

    Component mainCabin1 = new Cabin(3, 3, 3, 3, "Main Cabin", "imagePath", Color.RED, true);
    Component mainCabin2 = new Cabin(3, 3, 3, 3, "Main Cabin", "imagePath", Color.GREEN, true);
    Component mainCabin3 = new Cabin(3, 3, 3, 3, "Main Cabin", "imagePath", Color.BLUE, true);
    Component mainCabin4 = new Cabin(3, 3, 3, 3, "Main Cabin", "imagePath", Color.YELLOW, true);

    Component pipes1 = new Pipes(3, 2, 1, 3, "Pipes", "imagePath");
    Component pipes2 = new Pipes(3, 1, 2, 3, "Pipes", "imagePath");
    Component pipes3 = new Pipes(1, 3, 0, 3, "Pipes", "imagePath");
    Component pipes4 = new Pipes(3, 2, 2, 3, "Pipes", "imagePath");
    Component pipes5 = new Pipes(2, 3, 0, 3, "Pipes", "imagePath");
    Component pipes6 = new Pipes(1, 2, 1, 3, "Pipes", "imagePath");
    Component pipes7 = new Pipes(1, 3, 2, 3, "Pipes", "imagePath");
    Component pipes8 = new Pipes(3, 3, 0, 1, "Pipes", "imagePath");

    Component shield1 = new Shield(2, 1, 2, 1, "Shield", "imagePath");
    Component shield2 = new Shield(0, 2, 2, 3, "Shield", "imagePath");
    Component shield3 = new Shield(0, 1, 3, 1, "Shield", "imagePath");
    Component shield4 = new Shield(1, 0, 1, 1, "Shield", "imagePath");
    Component shield5 = new Shield(0, 0, 3, 2, "Shield", "imagePath");
    Component shield6 = new Shield(0, 2, 2, 2, "Shield", "imagePath");
    Component shield7 = new Shield(1, 2, 1, 2, "Shield", "imagePath");
    Component shield8 = new Shield(0, 0, 1, 3, "Shield", "imagePath");

    Component lifeSupportB1 = new LifeSupport(1, 1, 0, 1, "Life Support", "imagePath", LifeSupportColor.BROWN);
    Component lifeSupportB2 = new LifeSupport(0, 0, 1, 3, "Life Support", "imagePath", LifeSupportColor.BROWN);
    Component lifeSupportB3 = new LifeSupport(0, 0, 0, 3, "Life Support", "imagePath", LifeSupportColor.BROWN);
    Component lifeSupportB4 = new LifeSupport(1, 0, 0, 3, "Life Support", "imagePath", LifeSupportColor.BROWN);
    Component lifeSupportB5 = new LifeSupport(0, 3, 0, 2, "Life Support", "imagePath", LifeSupportColor.BROWN);
    Component lifeSupportB6 = new LifeSupport(2, 1, 0, 1, "Life Support", "imagePath", LifeSupportColor.BROWN);

    Component lifeSupportP1 = new LifeSupport(2, 2, 0, 2, "Life Support", "imagePath", LifeSupportColor.PURPLE);
    Component lifeSupportP2 = new LifeSupport(1, 2, 0, 2, "Life Support", "imagePath", LifeSupportColor.PURPLE);
    Component lifeSupportP3 = new LifeSupport(0, 0, 0, 3, "Life Support", "imagePath", LifeSupportColor.PURPLE);
    Component lifeSupportP4 = new LifeSupport(0, 1, 0, 3, "Life Support", "imagePath", LifeSupportColor.PURPLE);
    Component lifeSupportP5 = new LifeSupport(2, 0, 0, 3, "Life Support", "imagePath", LifeSupportColor.PURPLE);
    Component lifeSupportP6 = new LifeSupport(0, 0, 2, 3, "Life Support", "imagePath", LifeSupportColor.PURPLE);

    Component specialLoad1 = new Load(2, 2, 2, 3, "Special Load", "imagePath", 1, true);
    Component specialLoad2 = new Load(0, 2, 1, 3, "Special Load", "imagePath", 1, true);
    Component specialLoad3 = new Load(3, 0, 0, 3, "Special Load", "imagePath", 1, true);
    Component specialLoad4 = new Load(0, 3, 0, 3, "Special Load", "imagePath", 1, true);
    Component specialLoad5 = new Load(1, 1, 1, 3, "Special Load", "imagePath", 1, true);
    Component specialLoad6 = new Load(2, 1, 0, 3, "Special Load", "imagePath", 1, true);
    Component specialLoad7 = new Load(0, 2, 0, 1, "Special Load", "imagePath", 2, true);
    Component specialLoad8 = new Load(0, 0, 0, 2, "Special Load", "imagePath", 2, true);
    Component specialLoad9 = new Load(0, 0, 0, 1, "Special Load", "imagePath", 2, true);

    Component load1 = new Load(2, 1, 3, 0, "Load", "imagePath", 2, false);
    Component load2 = new Load(0, 0, 0, 3, "Load", "imagePath", 2, false);
    Component load3 = new Load(0, 2, 0, 3, "Load", "imagePath", 2, false);
    Component load4 = new Load(2, 3, 0, 1, "Load", "imagePath", 2, false);
    Component load5 = new Load(2, 1, 2, 3, "Load", "imagePath", 2, false);
    Component load6 = new Load(0, 0, 0, 3, "Load", "imagePath", 2, false);
    Component load7 = new Load(2, 1, 3, 1, "Load", "imagePath", 2, false);
    Component load8 = new Load(0, 1, 0, 3, "Load", "imagePath", 2, false);
    Component load9 = new Load(3, 0, 0, 3, "Load", "imagePath", 2, false);
    Component load10 = new Load(0, 0, 0, 3, "Load", "imagePath", 3, false);
    Component load11 = new Load(2, 0, 2, 0, "Load", "imagePath", 3, false);
    Component load12 = new Load(1, 0, 1, 0, "Load", "imagePath", 3, false);
    Component load13 = new Load(1, 0, 2, 1, "Load", "imagePath", 3, false);
    Component load14 = new Load(2, 0, 1, 2, "Load", "imagePath", 3, false);
    Component load15 = new Load(0, 0, 0, 1, "Load", "imagePath", 3, false);

    Component cannon1 = new Cannon(0, 3, 2, "Cannon", "imagePath", false);
    Component cannon2 = new Cannon(2, 0, 3, "Cannon", "imagePath", false);
    Component cannon3 = new Cannon(1, 3, 0, "Cannon", "imagePath", false);
    Component cannon4 = new Cannon(2, 3, 1, "Cannon", "imagePath", false);
    Component cannon5 = new Cannon(3, 0, 1, "Cannon", "imagePath", false);
    Component cannon6 = new Cannon(2, 0, 0, "Cannon", "imagePath", false);
    Component cannon7 = new Cannon(1, 0, 0, "Cannon", "imagePath", false);
    Component cannon8 = new Cannon(0, 0, 1, "Cannon", "imagePath", false);
    Component cannon9 = new Cannon(2, 2, 2, "Cannon", "imagePath", false);
    Component cannon10 = new Cannon(0, 1, 0, "Cannon", "imagePath", false);
    Component cannon11 = new Cannon(2, 3, 0, "Cannon", "imagePath", false);
    Component cannon12 = new Cannon(1, 0, 2, "Cannon", "imagePath", false);
    Component cannon13 = new Cannon(0, 2, 1, "Cannon", "imagePath", false);
    Component cannon14 = new Cannon(0, 2, 3, "Cannon", "imagePath", false);
    Component cannon15 = new Cannon(3, 1, 0, "Cannon", "imagePath", false);
    Component cannon16 = new Cannon(1, 2, 0, "Cannon", "imagePath", false);
    Component cannon17 = new Cannon(0, 1, 0, "Cannon", "imagePath", false);
    Component cannon18 = new Cannon(1, 1, 1, "Cannon", "imagePath", false);
    Component cannon19 = new Cannon(2, 1, 0, "Cannon", "imagePath", false);
    Component cannon20 = new Cannon(1, 3, 2, "Cannon", "imagePath", false);
    Component cannon21 = new Cannon(0, 0, 2, "Cannon", "imagePath", false);
    Component cannon22 = new Cannon(0, 3, 1, "Cannon", "imagePath", false);
    Component cannon23 = new Cannon(0, 2, 0, "Cannon", "imagePath", false);
    Component cannon24 = new Cannon(0, 1, 2, "Cannon", "imagePath", false);
    Component cannon25 = new Cannon(0, 2, 0, "Cannon", "imagePath", false);

    Component doubleCannon1 = new Cannon(1, 2, 1, "Double Cannon", "imagePath", true);
    Component doubleCannon2 = new Cannon(2, 0, 1, "Double Cannon", "imagePath", true);
    Component doubleCannon3 = new Cannon(0, 1, 3, "Double Cannon", "imagePath", true);
    Component doubleCannon4 = new Cannon(2, 1, 2, "Double Cannon", "imagePath", true);
    Component doubleCannon5 = new Cannon(3, 0, 0, "Double Cannon", "imagePath", true);
    Component doubleCannon6 = new Cannon(1, 3, 0, "Double Cannon", "imagePath", true);
    Component doubleCannon7 = new Cannon(0, 0, 3, "Double Cannon", "imagePath", true);
    Component doubleCannon8 = new Cannon(0, 1, 0, "Double Cannon", "imagePath", true);
    Component doubleCannon9 = new Cannon(0, 3, 2, "Double Cannon", "imagePath", true);
    Component doubleCannon10 = new Cannon(0, 2, 0, "Double Cannon", "imagePath", true);
    Component doubleCannon11 = new Cannon(3, 2, 0, "Double Cannon", "imagePath", true);

    Component engine1 = new Engine(2, 0, 0, "Engine", "imagePath", false);
    Component engine2 = new Engine(3, 2, 0, "Engine", "imagePath", false);
    Component engine3 = new Engine(0, 3, 0, "Engine", "imagePath", false);
    Component engine4 = new Engine(2, 3, 0, "Engine", "imagePath", false);
    Component engine5 = new Engine(0, 3, 1, "Engine", "imagePath", false);
    Component engine6 = new Engine(0, 1, 2, "Engine", "imagePath", false);
    Component engine7 = new Engine(2, 1, 1, "Engine", "imagePath", false);
    Component engine8 = new Engine(2, 0, 2, "Engine", "imagePath", false);
    Component engine9 = new Engine(0, 0, 3, "Engine", "imagePath", false);
    Component engine10 = new Engine(1, 2, 2, "Engine", "imagePath", false);
    Component engine11 = new Engine(3, 2, 1, "Engine", "imagePath", false);
    Component engine12 = new Engine(0, 3, 0, "Engine", "imagePath", false);
    Component engine13 = new Engine(1, 0, 0, "Engine", "imagePath", false);
    Component engine14 = new Engine(0, 2, 3, "Engine", "imagePath", false);
    Component engine15 = new Engine(1, 1, 0, "Engine", "imagePath", false);
    Component engine16 = new Engine(1, 0, 3, "Engine", "imagePath", false);
    Component engine17 = new Engine(3, 1, 2, "Engine", "imagePath", false);
    Component engine18 = new Engine(3, 0, 1, "Engine", "imagePath", false);
    Component engine19 = new Engine(0, 0, 3, "Engine", "imagePath", false);
    Component engine20 = new Engine(1, 0, 0, "Engine", "imagePath", false);
    Component engine21 = new Engine(2, 0, 0, "Engine", "imagePath", false);

    Component doubleEngine1 = new Engine(1, 1, 1, "Double Engine", "imagePath", true);
    Component doubleEngine2 = new Engine(2, 0, 0, "Double Engine", "imagePath", true);
    Component doubleEngine3 = new Engine(0, 3, 3, "Double Engine", "imagePath", true);
    Component doubleEngine4 = new Engine(1, 3, 0, "Double Engine", "imagePath", true);
    Component doubleEngine5 = new Engine(2, 2, 2, "Double Engine", "imagePath", true);
    Component doubleEngine6 = new Engine(1, 0, 0, "Double Engine", "imagePath", true);
    Component doubleEngine7 = new Engine(3, 1, 0, "Double Engine", "imagePath", true);
    Component doubleEngine8 = new Engine(3, 0, 2, "Double Engine", "imagePath", true);
    Component doubleEngine9 = new Engine(2, 0, 3, "Double Engine", "imagePath", true);

    Component batteryStorage1 = new BatteryStorage(3, 2, 1, 0, "Battery Storage", "imagePath", 2);
    Component batteryStorage2 = new BatteryStorage(2, 1, 2, 1, "Battery Storage", "imagePath", 2);
    Component batteryStorage3 = new BatteryStorage(0, 3, 0, 3, "Battery Storage", "imagePath", 2);
    Component batteryStorage4 = new BatteryStorage(3, 0, 0, 1, "Battery Storage", "imagePath", 2);
    Component batteryStorage5 = new BatteryStorage(3, 0, 0, 3, "Battery Storage", "imagePath", 2);
    Component batteryStorage6 = new BatteryStorage(3, 2, 0, 0, "Battery Storage", "imagePath", 2);
    Component batteryStorage7 = new BatteryStorage(3, 1, 1, 1, "Battery Storage", "imagePath", 2);
    Component batteryStorage8 = new BatteryStorage(0, 0, 0, 3, "Battery Storage", "imagePath", 2);
    Component batteryStorage9 = new BatteryStorage(0, 0, 0, 3, "Battery Storage", "imagePath", 2);
    Component batteryStorage10 = new BatteryStorage(3, 2, 2, 2, "Battery Storage", "imagePath", 2);
    Component batteryStorage11 = new BatteryStorage(3, 1, 2, 0, "Battery Storage", "imagePath", 2);
    Component batteryStorage12 = new BatteryStorage(0, 0, 0, 1, "Battery Storage", "imagePath", 3);
    Component batteryStorage13 = new BatteryStorage(1, 1, 2, 0, "Battery Storage", "imagePath", 3);
    Component batteryStorage14 = new BatteryStorage(2, 1, 0, 0, "Battery Storage", "imagePath", 3);
    Component batteryStorage15 = new BatteryStorage(2, 2, 1, 0, "Battery Storage", "imagePath", 3);
    Component batteryStorage16 = new BatteryStorage(1, 0, 2, 0, "Battery Storage", "imagePath", 3);
    Component batteryStorage17 = new BatteryStorage(2, 0, 0, 0, "Battery Storage", "imagePath", 3);

}
