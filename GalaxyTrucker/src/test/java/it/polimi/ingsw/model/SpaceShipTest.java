package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Direction;
import it.polimi.ingsw.model.components.Load;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SpaceShipTest {

    private SpaceShip spaceShip;

    @BeforeEach
    void setUp() {
        spaceShip = new SpaceShip(PlayerColors.BLUE);
        spaceShip.setSpaceship(2,PlayerColors.BLUE);
    }

    @Test
    void increaseAndGetLost() {
        spaceShip.increaseLost(1);
        assertEquals(spaceShip.getLost(), 1);
        spaceShip.increaseLost(2);
        assertEquals(spaceShip.getLost(), 3);
    }

    @Test
    void setBattery() {
        spaceShip.setBattery();
        assertEquals(spaceShip.getNumBatteries(),0);
    }

    @Test
    void getGoodsCapacity() {
        assertEquals(spaceShip.getGoodsCapacity(), 0);
    }

    @Test
    void setAndGetNumHuman() {
        spaceShip.setNumHuman();
        assertEquals(spaceShip.getNumHuman(), 2);
    }

    @Test
    void setNumAlien() {

    }

    @Test
    void removeHuman() {
        spaceShip.setNumHuman();
        spaceShip.removeHuman(7,7);
        assertEquals(spaceShip.getNumHuman(), 1);
    }

    @Test
    void removeNumHuman() {
        spaceShip.setNumHuman();
        ArrayList<int[]> coord=new ArrayList<>();
        coord.add(new int[]{7,7,2});
        spaceShip.removeNumHuman(coord);
        assertEquals(spaceShip.getNumHuman(), 0);
    }

    @Test
    void getComponents() {
        assertNotNull(spaceShip.getComponents());
    }

    @Test
    void getNumDoubleCannon() {
        assertEquals(spaceShip.getNumDoubleCannon(), 0);
    }

    @Test
    void getNumDoubleEngine() {
        assertEquals(spaceShip.getNumDoubleEngine(), 0);
    }

    @Test
    void addComponent() {
        Component comp=new Load(3,3,3,3,"Load","",2,false);
        try {
            spaceShip.addComponent(comp,6,7);
        } catch (InvalidCoordinates e) {
            System.out.println(e);
        }

        try {
            spaceShip.addComponent(comp,4,4);
        } catch (InvalidCoordinates e) {
            System.out.println(e);
        }


    }

    @Test
    void removeComponent() {
        try {
            spaceShip.removeComponent(7,7);
        } catch (InvalidCoordinates e) {
            System.out.println(e);
        }
    }

    @Test
    void getSingleCannonPower() {
        assertEquals(spaceShip.getSingleCannonPower(), 0);
    }

    @Test
    void getDoubleCannon() {
        assertEquals(spaceShip.getDoubleCannon(), 0);
    }

    @Test
    void getSingleEnginePower() {
        assertEquals(spaceShip.getSingleEnginePower(), 0);
    }

    @Test
    void getDoubleEngine() {
        assertEquals(spaceShip.getDoubleEngine(), 0);
    }

    @Test
    void chooseComponent() {
        assertEquals(spaceShip.chooseComponent(7,7).getType(),"cabin");
    }

    @Test
    void getProtection() {
        ArrayList<Direction> p = new ArrayList<Direction>();
        assertEquals(spaceShip.getProtection(),p);
    }

    @Test
    void isValid() {

    }

    @Test
    void getTotalCannonPower() {
        assertEquals(spaceShip.getTotalCannonPower(),0);
    }

    @Test
    void getTotalEnginePower() {
        assertEquals(spaceShip.getTotalEnginePower(),0);
    }

    @Test
    void countExposedConnectors() {
        assertEquals(spaceShip.countExposedConnectors(),4);
    }

    @Test
    void wellDone1() {
        assertTrue(spaceShip.wellDone());

    }

    @Test
    void wellDone2() {
//        try {
//            spaceShip.addComponent(new Load(3,3,3,3,"Load","",2,false),6,7);
//            spaceShip.addComponent(new Load(3,3,3,3,"Load","",2,false),7,6);
//            spaceShip.addComponent(new Load(3,3,3,3,"Load","",2,false),8,7);
//            spaceShip.addComponent(new Load(3,3,3,3,"Load","",2,false),7,8);
//        } catch (InvalidCoordinates e) {
//            throw new RuntimeException(e);
//        }
//        assertTrue(spaceShip.wellDone());
    }

    @Test
    void isHit() {
        assertTrue(spaceShip.isHit(0,7));
    }

    @Test
    void destroyComponent() {

    }

    @Test
    void shootMeteor() {
    }

    @Test
    void blockSmallMeteor() {
    }

    @Test
    void blockLightCannonShot() {
    }

    @Test
    void epidemic() {
    }

    @Test
    void addGoodsList() {
    }

    @Test
    void addGoods() {
    }

    @Test
    void addGood() {
    }

    @Test
    void getGoodsList() {
    }

    @Test
    void removeBatteries() {
    }

    @Test
    void removeAllGoods() {
    }

    @Test
    void getNumBatteries() {
    }

    @Test
    void checkLoad() {
    }

    @Test
    void testCheckLoad() {
    }

    @Test
    void checkDoubleEngine() {
    }

    @Test
    void checkDoubleCannon() {
    }

    @Test
    void checkShield() {
    }

    @Test
    void checkBatteryStorage() {
    }

    @Test
    void checkCabin() {
    }

    @Test
    void swapGoods() {
    }

    @Test
    void getRedGoods() {
    }

    @Test
    void getGreenGoods() {
    }

    @Test
    void getBlueGoods() {
    }

    @Test
    void getYellowGoods() {
    }

    @Test
    void removeGoods() {
    }
}