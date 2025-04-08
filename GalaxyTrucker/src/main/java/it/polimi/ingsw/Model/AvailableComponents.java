package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Components.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that manages the components as a whole
 */
public class AvailableComponents {
    private ArrayList<Component> components;

    /**
     * Available components constructor, initialise the array of 156 component
     */
    public AvailableComponents() {
        Component cabin1 = new Cabin(0, 0, 3, 1, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web35.jpg", null, false);
        Component cabin2 = new Cabin(1, 0, 2, 3, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web49.jpg", null, false);
        Component cabin3 = new Cabin(2, 0, 0, 1, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web38.jpg", null, false);
        Component cabin4 = new Cabin(2, 2, 0, 3, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web51.jpg", null, false);
        Component cabin5 = new Cabin(1, 2, 1, 1, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web36.jpg", null, false);
        Component cabin6 = new Cabin(2, 1, 2, 2, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web43.jpg", null, false);
        Component cabin7 = new Cabin(2, 0, 2, 3, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web50.jpg", null, false);
        Component cabin8 = new Cabin(0, 1, 1, 3, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web46.jpg", null, false);
        Component cabin9 = new Cabin(2, 1, 0, 1, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web39.jpg", null, false);
        Component cabin10 = new Cabin(1, 0, 1, 3, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web48.jpg", null, false);
        Component cabin11 = new Cabin(0, 1, 0, 3, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web45.jpg", null, false);
        Component cabin12 = new Cabin(0, 2, 0, 3, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web47.jpg", null, false);
        Component cabin13 = new Cabin(1, 2, 2, 1, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web37.jpg", null, false);
        Component cabin14 = new Cabin(2, 1, 2, 1, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web40.jpg", null, false);
        Component cabin15 = new Cabin(0, 1, 3, 2, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web41.jpg", null, false);
        Component cabin16 = new Cabin(1, 2, 0, 2, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web42.jpg", null, false);
        Component cabin17 = new Cabin(3, 0, 0, 2, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web44.jpg", null, false);

        Component mainCabin1 = new Cabin(3, 3, 3, 3, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web52.jpg", PlayerColors.RED, true);
        Component mainCabin2 = new Cabin(3, 3, 3, 3, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web34.jpg", PlayerColors.GREEN, true);
        Component mainCabin3 = new Cabin(3, 3, 3, 3, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web33.jpg", PlayerColors.BLUE, true);
        Component mainCabin4 = new Cabin(3, 3, 3, 3, "Cabin", "src/main/resources/components/tiles/GT-new_tiles_16_for web61.jpg", PlayerColors.YELLOW, true);

        Component pipes1 = new Pipes(3, 2, 0, 3, "Pipes", "src/main/resources/components/tiles/GT-new_tiles_16_for web59.jpg");
        Component pipes2 = new Pipes(3, 1, 2, 3, "Pipes", "src/main/resources/components/tiles/GT-new_tiles_16_for web58.jpg");
        Component pipes3 = new Pipes(1, 3, 0, 3, "Pipes", "src/main/resources/components/tiles/GT-new_tiles_16_for web54.jpg");
        Component pipes4 = new Pipes(3, 2, 2, 3, "Pipes", "src/main/resources/components/tiles/GT-new_tiles_16_for web60.jpg");
        Component pipes5 = new Pipes(2, 3, 0, 3, "Pipes", "src/main/resources/components/tiles/GT-new_tiles_16_for web57.jpg");
        Component pipes6 = new Pipes(1, 3, 2, 3, "Pipes", "src/main/resources/components/tiles/GT-new_tiles_16_for web56.jpg");
        Component pipes7 = new Pipes(1, 3, 1, 3, "Pipes", "src/main/resources/components/tiles/GT-new_tiles_16_for web55.jpg");
        Component pipes8 = new Pipes(3, 3, 0, 1, "Pipes", "src/main/resources/components/tiles/GT-new_tiles_16_for web53.jpg");

        Component shield1 = new Shield(2, 1, 2, 1, "Shield", "src/main/resources/components/tiles/GT-new_tiles_16_for web151.jpg");
        Component shield2 = new Shield(0, 2, 2, 3, "Shield", "src/main/resources/components/tiles/GT-new_tiles_16_for web156.jpg");
        Component shield3 = new Shield(0, 1, 3, 1, "Shield", "src/main/resources/components/tiles/GT-new_tiles_16_for web149.jpg");
        Component shield4 = new Shield(1, 0, 1, 1, "Shield", "src/main/resources/components/tiles/GT-new_tiles_16_for web150.jpg");
        Component shield5 = new Shield(0, 0, 3, 2, "Shield", "src/main/resources/components/tiles/GT-new_tiles_16_for web152.jpg");
        Component shield6 = new Shield(0, 2, 2, 2, "Shield", "src/main/resources/components/tiles/GT-new_tiles_16_for web153.jpg");
        Component shield7 = new Shield(1, 2, 1, 2, "Shield", "src/main/resources/components/tiles/GT-new_tiles_16_for web154.jpg");
        Component shield8 = new Shield(0, 0, 1, 3, "Shield", "src/main/resources/components/tiles/GT-new_tiles_16_for web155.jpg");

        Component lifeSupportB1 = new LifeSupport(1, 1, 0, 1, "Life Support", "src/main/resources/components/tiles/GT-new_tiles_16_for web137.jpg", AlienColor.BROWN);
        Component lifeSupportB2 = new LifeSupport(0, 0, 1, 3, "Life Support", "src/main/resources/components/tiles/GT-new_tiles_16_for web140.jpg", AlienColor.BROWN);
        Component lifeSupportB3 = new LifeSupport(0, 0, 0, 3, "Life Support", "src/main/resources/components/tiles/GT-new_tiles_16_for web139.jpg", AlienColor.BROWN);
        Component lifeSupportB4 = new LifeSupport(1, 0, 0, 3, "Life Support", "src/main/resources/components/tiles/GT-new_tiles_16_for web142.jpg", AlienColor.BROWN);
        Component lifeSupportB5 = new LifeSupport(0, 2, 0, 3, "Life Support", "src/main/resources/components/tiles/GT-new_tiles_16_for web141.jpg", AlienColor.BROWN);
        Component lifeSupportB6 = new LifeSupport(2, 1, 0, 1, "Life Support", "src/main/resources/components/tiles/GT-new_tiles_16_for web138.jpg", AlienColor.BROWN);

        Component lifeSupportP1 = new LifeSupport(2, 2, 0, 2, "Life Support", "src/main/resources/components/tiles/GT-new_tiles_16_for web144.jpg", AlienColor.PURPLE);
        Component lifeSupportP2 = new LifeSupport(1, 2, 0, 2, "Life Support", "src/main/resources/components/tiles/GT-new_tiles_16_for web143.jpg", AlienColor.PURPLE);
        Component lifeSupportP3 = new LifeSupport(0, 0, 0, 3, "Life Support", "src/main/resources/components/tiles/GT-new_tiles_16_for web145.jpg", AlienColor.PURPLE);
        Component lifeSupportP4 = new LifeSupport(0, 1, 0, 3, "Life Support", "src/main/resources/components/tiles/GT-new_tiles_16_for web147.jpg", AlienColor.PURPLE);
        Component lifeSupportP5 = new LifeSupport(2, 0, 0, 3, "Life Support", "src/main/resources/components/tiles/GT-new_tiles_16_for web148.jpg", AlienColor.PURPLE);
        Component lifeSupportP6 = new LifeSupport(0, 0, 2, 3, "Life Support", "src/main/resources/components/tiles/GT-new_tiles_16_for web146.jpg", AlienColor.PURPLE);

        Component specialLoad1 = new Load(2, 2, 2, 3, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web66.jpg", 1, true);
        Component specialLoad2 = new Load(0, 2, 1, 3, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web62.jpg", 1, true);
        Component specialLoad3 = new Load(3, 0, 0, 3, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web67.jpg", 1, true);
        Component specialLoad4 = new Load(0, 3, 0, 3, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web63.jpg", 1, true);
        Component specialLoad5 = new Load(1, 1, 1, 3, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web64.jpg", 1, true);
        Component specialLoad6 = new Load(2, 1, 0, 3, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web65.jpg", 1, true);
        Component specialLoad7 = new Load(0, 2, 0, 1, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web69.jpg", 2, true);
        Component specialLoad8 = new Load(0, 0, 0, 2, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web70.jpg", 2, true);
        Component specialLoad9 = new Load(0, 0, 0, 1, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web68.jpg", 2, true);

        Component load1 = new Load(2, 1, 3, 0, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web18.jpg", 2, false);
        Component load2 = new Load(0, 0, 0, 3, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web21.jpg", 2, false);
        Component load3 = new Load(0, 2, 0, 3, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web24.jpg", 2, false);
        Component load4 = new Load(2, 3, 0, 1, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web20.jpg", 2, false);
        Component load5 = new Load(2, 1, 2, 3, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web25.jpg", 2, false);
        Component load6 = new Load(0, 0, 0, 3, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web22.jpg", 2, false);
        Component load7 = new Load(2, 1, 3, 1, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web19.jpg", 2, false);
        Component load8 = new Load(0, 1, 0, 3, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web23.jpg", 2, false);
        Component load9 = new Load(3, 0, 0, 3, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web26.jpg", 2, false);
        Component load10 = new Load(0, 0, 0, 2, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web29.jpg", 3, false);
        Component load11 = new Load(2, 0, 2, 0, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web30.jpg", 3, false);
        Component load12 = new Load(1, 0, 1, 0, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web28.jpg", 3, false);
        Component load13 = new Load(1, 0, 2, 1, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web31.jpg", 3, false);
        Component load14 = new Load(2, 0, 1, 2, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web32.jpg", 3, false);
        Component load15 = new Load(0, 0, 0, 1, "Load", "src/main/resources/components/tiles/GT-new_tiles_16_for web27.jpg", 3, false);

        Component cannon1 = new Cannon(0, 3, 2, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web120.jpg", false);
        Component cannon2 = new Cannon(2, 0, 3, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web125.jpg", false);
        Component cannon3 = new Cannon(1, 3, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web107.jpg", false);
        Component cannon4 = new Cannon(2, 3, 1, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web116.jpg", false);
        Component cannon5 = new Cannon(3, 0, 1, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web117.jpg", false);
        Component cannon6 = new Cannon(2, 0, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web108.jpg", false);
        Component cannon7 = new Cannon(1, 0, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web105.jpg", false);
        Component cannon8 = new Cannon(0, 0, 1, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web112.jpg", false);
        Component cannon9 = new Cannon(2, 2, 2, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web123.jpg", false);
        Component cannon10 = new Cannon(0, 1, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web101.jpg", false);
        Component cannon11 = new Cannon(2, 3, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web110.jpg", false);
        Component cannon12 = new Cannon(1, 0, 2, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web121.jpg", false);
        Component cannon13 = new Cannon(0, 2, 1, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web113.jpg", false);
        Component cannon14 = new Cannon(0, 2, 3, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web124.jpg", false);
        Component cannon15 = new Cannon(3, 1, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web111.jpg", false);
        Component cannon16 = new Cannon(1, 2, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web106.jpg", false);
        Component cannon17 = new Cannon(0, 1, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web102.jpg", false);
        Component cannon18 = new Cannon(1, 1, 1, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web115.jpg", false);
        Component cannon19 = new Cannon(2, 1, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web109.jpg", false);
        Component cannon20 = new Cannon(1, 3, 2, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web122.jpg", false);
        Component cannon21 = new Cannon(0, 0, 2, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web118.jpg", false);
        Component cannon22 = new Cannon(0, 3, 1, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web114.jpg", false);
        Component cannon23 = new Cannon(0, 2, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web103.jpg", false);
        Component cannon24 = new Cannon(0, 1, 2, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web119.jpg", false);
        Component cannon25 = new Cannon(0, 2, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web104.jpg", false);

        Component doubleCannon1 = new Cannon(1, 2, 1, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web131.jpg", true);
        Component doubleCannon2 = new Cannon(2, 0, 1, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web132.jpg", true);
        Component doubleCannon3 = new Cannon(0, 1, 3, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web136.jpg", true);
        Component doubleCannon4 = new Cannon(2, 1, 2, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web134.jpg", true);
        Component doubleCannon5 = new Cannon(3, 0, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web129.jpg", true);
        Component doubleCannon6 = new Cannon(1, 3, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web128.jpg", true);
        Component doubleCannon7 = new Cannon(0, 0, 3, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web135.jpg", true);
        Component doubleCannon8 = new Cannon(0, 1, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web126.jpg", true);
        Component doubleCannon9 = new Cannon(0, 3, 2, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web133.jpg", true);
        Component doubleCannon10 = new Cannon(0, 2, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web127.jpg", true);
        Component doubleCannon11 = new Cannon(3, 2, 0, "Cannon", "src/main/resources/components/tiles/GT-new_tiles_16_for web130.jpg", true);

        Component engine1 = new Engine(2, 0, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web76.jpg", false);
        Component engine2 = new Engine(3, 2, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web79.jpg", false);
        Component engine3 = new Engine(0, 3, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web71.jpg", false);
        Component engine4 = new Engine(2, 3, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web78.jpg", false);
        Component engine5 = new Engine(0, 3, 1, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web80.jpg", false);
        Component engine6 = new Engine(0, 1, 2, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web84.jpg", false);
        Component engine7 = new Engine(2, 1, 1, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web81.jpg", false);
        Component engine8 = new Engine(2, 0, 2, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web86.jpg", false);
        Component engine9 = new Engine(0, 0, 3, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web88.jpg", false);
        Component engine10 = new Engine(1, 2, 2, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web85.jpg", false);
        Component engine11 = new Engine(3, 2, 1, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web83.jpg", false);
        Component engine12 = new Engine(0, 3, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web72.jpg", false);
        Component engine13 = new Engine(1, 0, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web73.jpg", false);
        Component engine14 = new Engine(0, 2, 3, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web90.jpg", false);
        Component engine15 = new Engine(1, 1, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web75.jpg", false);
        Component engine16 = new Engine(1, 0, 3, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web91.jpg", false);
        Component engine17 = new Engine(3, 1, 2, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web87.jpg", false);
        Component engine18 = new Engine(3, 0, 1, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web82.jpg", false);
        Component engine19 = new Engine(0, 0, 3, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web89.jpg", false);
        Component engine20 = new Engine(1, 0, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web74.jpg", false);
        Component engine21 = new Engine(2, 0, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web77.jpg", false);

        Component doubleEngine1 = new Engine(1, 1, 1, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web96.jpg", true);
        Component doubleEngine2 = new Engine(2, 0, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web94.jpg", true);
        Component doubleEngine3 = new Engine(0, 3, 3, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web99.jpg", true);
        Component doubleEngine4 = new Engine(1, 3, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web93.jpg", true);
        Component doubleEngine5 = new Engine(2, 2, 2, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web97.jpg", true);
        Component doubleEngine6 = new Engine(1, 0, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web92.jpg", true);
        Component doubleEngine7 = new Engine(3, 1, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web95.jpg", true);
        Component doubleEngine8 = new Engine(3, 0, 2, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web98.jpg", true);
        Component doubleEngine9 = new Engine(2, 0, 3, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web100.jpg", true);

        Component batteryStorage1 = new BatteryStorage(3, 2, 1, 0, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web3.jpg", 2);
        Component batteryStorage2 = new BatteryStorage(2, 1, 2, 1, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web4.jpg", 2);
        Component batteryStorage3 = new BatteryStorage(0, 3, 0, 3, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web10.jpg", 2);
        Component batteryStorage4 = new BatteryStorage(3, 0, 0, 1, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web5.jpg", 2);
        Component batteryStorage5 = new BatteryStorage(3, 0, 0, 3, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web11.jpg", 2);
        Component batteryStorage6 = new BatteryStorage(3, 2, 0, 0, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web2.jpg", 2);
        Component batteryStorage7 = new BatteryStorage(3, 1, 1, 1, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web6.jpg", 2);
        Component batteryStorage8 = new BatteryStorage(0, 0, 0, 3, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web8.jpg", 2);
        Component batteryStorage9 = new BatteryStorage(0, 0, 0, 3, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web9.jpg", 2);
        Component batteryStorage10 = new BatteryStorage(3, 2, 2, 2, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web7.jpg", 2);
        Component batteryStorage11 = new BatteryStorage(3, 1, 2, 0, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web.jpg", 2);
        Component batteryStorage12 = new BatteryStorage(0, 0, 0, 1, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web17.jpg", 3);
        Component batteryStorage13 = new BatteryStorage(1, 1, 2, 0, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web13.jpg", 3);
        Component batteryStorage14 = new BatteryStorage(2, 1, 0, 0, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web15.jpg", 3);
        Component batteryStorage15 = new BatteryStorage(2, 2, 1, 0, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web16.jpg", 3);
        Component batteryStorage16 = new BatteryStorage(1, 0, 2, 0, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web12.jpg", 3);
        Component batteryStorage17 = new BatteryStorage(2, 0, 0, 0, "Battery Storage", "src/main/resources/components/tiles/GT-new_tiles_16_for web14.jpg", 3);

        components.add(cabin1);
        components.add(cabin2);
        components.add(cabin3);
        components.add(cabin4);
        components.add(cabin5);
        components.add(cabin6);
        components.add(cabin7);
        components.add(cabin8);
        components.add(cabin9);
        components.add(cabin10);
        components.add(cabin11);
        components.add(cabin12);
        components.add(cabin13);
        components.add(cabin14);
        components.add(cabin15);
        components.add(cabin16);
        components.add(cabin17);
        components.add(mainCabin1);
        components.add(mainCabin2);
        components.add(mainCabin3);
        components.add(mainCabin4);
        components.add(pipes1);
        components.add(pipes2);
        components.add(pipes3);
        components.add(pipes4);
        components.add(pipes5);
        components.add(pipes6);
        components.add(pipes7);
        components.add(pipes8);
        components.add(shield1);
        components.add(shield2);
        components.add(shield3);
        components.add(shield4);
        components.add(shield5);
        components.add(shield6);
        components.add(shield7);
        components.add(shield8);
        components.add(lifeSupportB1);
        components.add(lifeSupportB2);
        components.add(lifeSupportB3);
        components.add(lifeSupportB4);
        components.add(lifeSupportB5);
        components.add(lifeSupportB6);
        components.add(lifeSupportP1);
        components.add(lifeSupportP2);
        components.add(lifeSupportP3);
        components.add(lifeSupportP4);
        components.add(lifeSupportP5);
        components.add(lifeSupportP6);
        components.add(specialLoad1);
        components.add(specialLoad2);
        components.add(specialLoad3);
        components.add(specialLoad4);
        components.add(specialLoad5);
        components.add(specialLoad6);
        components.add(specialLoad7);
        components.add(specialLoad8);
        components.add(specialLoad9);
        components.add(load1);
        components.add(load2);
        components.add(load3);
        components.add(load4);
        components.add(load5);
        components.add(load6);
        components.add(load7);
        components.add(load8);
        components.add(load9);
        components.add(load10);
        components.add(load11);
        components.add(load12);
        components.add(load13);
        components.add(load14);
        components.add(load15);
        components.add(cannon1);
        components.add(cannon2);
        components.add(cannon3);
        components.add(cannon4);
        components.add(cannon5);
        components.add(cannon6);
        components.add(cannon7);
        components.add(cannon8);
        components.add(cannon9);
        components.add(cannon10);
        components.add(cannon11);
        components.add(cannon12);
        components.add(cannon13);
        components.add(cannon14);
        components.add(cannon15);
        components.add(cannon16);
        components.add(cannon17);
        components.add(cannon18);
        components.add(cannon19);
        components.add(cannon20);
        components.add(cannon21);
        components.add(cannon22);
        components.add(cannon23);
        components.add(cannon24);
        components.add(cannon25);
        components.add(doubleCannon1);
        components.add(doubleCannon2);
        components.add(doubleCannon3);
        components.add(doubleCannon4);
        components.add(doubleCannon5);
        components.add(doubleCannon6);
        components.add(doubleCannon7);
        components.add(doubleCannon8);
        components.add(doubleCannon9);
        components.add(doubleCannon10);
        components.add(doubleCannon11);
        components.add(engine1);
        components.add(engine2);
        components.add(engine3);
        components.add(engine4);
        components.add(engine5);
        components.add(engine6);
        components.add(engine7);
        components.add(engine8);
        components.add(engine9);
        components.add(engine10);
        components.add(engine11);
        components.add(engine12);
        components.add(engine13);
        components.add(engine14);
        components.add(engine15);
        components.add(engine16);
        components.add(engine17);
        components.add(engine18);
        components.add(engine19);
        components.add(engine20);
        components.add(engine21);
        components.add(doubleEngine1);
        components.add(doubleEngine2);
        components.add(doubleEngine3);
        components.add(doubleEngine4);
        components.add(doubleEngine5);
        components.add(doubleEngine6);
        components.add(doubleEngine7);
        components.add(doubleEngine8);
        components.add(doubleEngine9);
        components.add(batteryStorage1);
        components.add(batteryStorage2);
        components.add(batteryStorage3);
        components.add(batteryStorage4);
        components.add(batteryStorage5);
        components.add(batteryStorage6);
        components.add(batteryStorage7);
        components.add(batteryStorage8);
        components.add(batteryStorage9);
        components.add(batteryStorage10);
        components.add(batteryStorage11);
        components.add(batteryStorage12);
        components.add(batteryStorage13);
        components.add(batteryStorage14);
        components.add(batteryStorage15);
        components.add(batteryStorage16);
        components.add(batteryStorage17);

        shuffle();
    }

    /**
     * Returns the array of components available
     * @return array of components
     */
    public ArrayList<Component> getComponents() {
        return components;
    }

    /**
     * Shuffles the components that are available before the truckers start the game
     */
    public void shuffle() {
        Random random = new Random();
        for (int i = components.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);  // Indice casuale tra 0 e i
            Component tmp = components.get(i);
            components.add(i,components.get(j));
            components.add(j,tmp);
        }
    }

    /**
     * Removes the component that has been chosen by the trucker for his spaceship from the whole of available components
     * @param component component to remove
     */
    public void remove(Component component) {
        components.remove(component);
    }


}
