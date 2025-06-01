package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SpaceShipTest {

    private SpaceShip spaceShip1;
    private SpaceShip spaceShip2;
    private CreateShip support=new CreateShip();

    @BeforeEach
    void setUp() {
        try {
            //spaceShip1=support.createShip1(spaceShip1);
            spaceShip2=support.createShip3(spaceShip2); //spaceship di livello 0
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        spaceShip1=new SpaceShip();
        spaceShip1.setSpaceship(2,PlayerColors.GREEN);
//        spaceShip2=new SpaceShip();
//        spaceShip2.setSpaceship(0,PlayerColors.RED);
    }

    @Test
    void setSpaceship() {
        spaceShip1.setSpaceship(2,PlayerColors.RED);

        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(i==7 && j==7){
                    assertNotNull(spaceShip1.getComponents()[i][j]);
                    switch(spaceShip1.getComponents()[i][j]){
                        case Cabin cabin->{
                            assertEquals(PlayerColors.RED, cabin.getColor());
                            assertTrue(cabin.getIsMain());
                        }
                        default -> {}
                    }
                }
                else{
                    if(i==5){
                        if(j==6 || j==8) assertNull(spaceShip1.getComponents()[i][j]);
                        else assertNotNull(spaceShip1.getComponents()[i][j]);
                    }
                    else if(i==6){
                        if(j==4 || j==10) assertNotNull(spaceShip1.getComponents()[i][j]);
                        else  assertNull(spaceShip1.getComponents()[i][j]);
                    }
                    else if(i==7 || i==8)  assertNull(spaceShip1.getComponents()[i][j]);
                    else if(i==9){
                        if(j==7) assertNotNull(spaceShip1.getComponents()[i][j]);
                        else  assertNull(spaceShip1.getComponents()[i][j]);
                    }
                }

            }
        }

        spaceShip2.setSpaceship(0,PlayerColors.GREEN);
        for(int i=5;i<=9;i++){
            for(int j=4;j<=10;j++){
                if(i==7 && j==7){
                    assertNotNull(spaceShip2.getComponents()[i][j]);
                    switch(spaceShip2.getComponents()[i][j]){
                        case Cabin cabin->{
                            assertEquals(PlayerColors.GREEN, cabin.getColor());
                            assertTrue(cabin.getIsMain());
                        }
                        default -> {}
                    }
                }else{
                    if(j==4 || j==10) assertNotNull(spaceShip2.getComponents()[i][j]);
                    else if(j==5 || j==9){
                        if(i==5 || i==6)
                            assertNotNull(spaceShip2.getComponents()[i][j]);
                        else assertNull(spaceShip2.getComponents()[i][j]);
                    }
                    else if(j==6 || j==8){
                        if(i==5){
                            assertNotNull(spaceShip2.getComponents()[i][j]);
                        }
                        else assertNull(spaceShip2.getComponents()[i][j]);
                    }
                    else if(j==7){
                        if(i==9) assertNotNull(spaceShip2.getComponents()[i][j]);
                        else assertNull(spaceShip2.getComponents()[i][j]);
                    }
                }
            }
        }

    }

    @Test
    void increaseAndGetLost() {
        spaceShip1.increaseLost(2);
        assertEquals(2,spaceShip1.getLost());
        spaceShip1.increaseLost(1);
        assertEquals(3,spaceShip1.getLost());

        spaceShip2.increaseLost(2);
        assertEquals(2,spaceShip2.getLost());
        spaceShip2.increaseLost(1);
        assertEquals(3,spaceShip2.getLost());
    }

    @Test
    void setAndGetBattery() {
        spaceShip2.setBattery();
        assertEquals(5, spaceShip2.getNumBatteries());
    }

    @Test
    void getGoodsCapacity() {
        assertEquals(5, spaceShip2.getGoodsCapacity());
    }

    @Test
    void setAndGetNumHuman() {
        spaceShip1.setNumHuman();
        assertEquals(2,spaceShip1.getNumHuman());

        spaceShip2.setNumHuman();
        assertEquals(6,spaceShip2.getNumHuman());
    }


    @Test
    void setNumAlien() {
        spaceShip1.setNumAlien(new ArrayList<>());
        spaceShip1.setNumHuman();
        assertEquals(2,spaceShip1.getNumHuman());


    }

    @Test
    void removeHuman() {
        spaceShip1.setNumHuman();
        spaceShip1.removeHuman(7,7);
        assertEquals(1,spaceShip1.getNumHuman());

        spaceShip2.setNumHuman();
        spaceShip2.removeHuman(8,7);
        assertEquals(5,spaceShip2.getNumHuman());
    }

    @Test
    void removeNumHuman() {
        spaceShip1.setNumHuman();
        ArrayList<int[]> coord1=new ArrayList<>();
        coord1.add(new int[]{7,7,1});
        spaceShip1.removeNumHuman(coord1);
        assertEquals(1,spaceShip1.getNumHuman());

        spaceShip2.setNumHuman();
        ArrayList<int[]> coord2=new ArrayList<>();
        coord2.add(new int[]{7,7,2});
        coord2.add(new int[]{7,8,2});
        coord2.add(new int[]{8,7,1});
        spaceShip2.removeNumHuman(coord2);
        assertEquals(1,spaceShip2.getNumHuman());
    }

    @Test
    void getComponents() {
        assertNotNull(spaceShip2.getComponents());
        for(int i=5;i<=9;i++){
            for(int j=4;j<=10;j++){
                if(j==4 || j==10) assertNotNull(spaceShip2.getComponents()[i][j]);
                if(j==5 || j==9){
                    assertNotNull(spaceShip2.getComponents()[i][j]);
                }
                if(j==6 || j==8){
                    if(i==9 && j==6) assertNull(spaceShip2.getComponents()[i][j]);
                    else assertNotNull(spaceShip2.getComponents()[i][j]);
                }
                if(j==7){
                    assertNotNull(spaceShip2.getComponents()[i][j]);
                }
            }
        }
    }

    @Test
    void getNumDoubleCannon() {
        assertEquals(0,spaceShip2.getNumDoubleCannon());
    }

    @Test
    void getNumDoubleEngine() {
        assertEquals(1,spaceShip2.getNumDoubleEngine());
    }

    @Test
    void addAndRemoveComponent() {
        try {
            spaceShip2.removeComponent(9,8);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        assertNull(spaceShip2.getComponents()[9][8]);
        Component component=new Engine(2,2,0,"Engine","",false);
        try {
            spaceShip2.addComponent(component,9,8);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        assertNotNull(spaceShip2.getComponents()[9][8]);
    }



    @Test
    void getSingleCannonPower() {
        assertEquals(2,spaceShip2.getSingleCannonPower());
    }

    @Test
    void getDoubleCannon() {
        assertEquals(0,spaceShip2.getDoubleCannon());

    }

    @Test
    void getSingleEnginePower() {
        assertEquals(2,spaceShip2.getSingleEnginePower());
    }

    @Test
    void activateAndGetDoubleEngine() {
        ArrayList<int[]> coord=new ArrayList<>();
        ArrayList<int[]> batt=new ArrayList<>();
        try {
            spaceShip2.activateDoubleEngines(coord,batt);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }
        assertEquals(0,spaceShip2.getDoubleEngine());

        spaceShip2.setBattery();
        coord.add(new int[]{8,6});
        batt.add(new int[]{8,8,1});
        try {
            spaceShip2.activateDoubleEngines(coord,batt);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }
        assertEquals(1,spaceShip2.getDoubleEngine());

    }

    @Test
    void getTotalCannonPower() {
    }

    @Test
    void deactivateDoubleCannons() {
    }

    @Test
    void deactivateDoubleEngines() {
    }

    @Test
    void activateDoubleCannons() {
    }

    @Test
    void activateShield() {
    }

    @Test
    void deActivateShields() {
    }

    @Test
    void getTotalEnginePower() {
    }

    @Test
    void countExposedConnectors() {
    }

    @Test
    void wellDone() {
    }

    @Test
    void wellDoneForTrialFlight() {
    }

    @Test
    void isHit() {
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

    @Test
    void getAlienCabin() {
    }

    @Test
    void removeAllHumans() {
    }
}