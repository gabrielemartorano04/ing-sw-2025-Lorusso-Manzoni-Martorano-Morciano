package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SpaceShipTest {

    private SpaceShip spaceShip1;
    private SpaceShip spaceShip2;
    private CreateShipTest support=new CreateShipTest();
    private SpaceShip spaceShip3;

    @BeforeEach
    void setUp() {
        try {
            spaceShip3=support.createShip1(spaceShip3); // level 2 spaceship
            spaceShip2=support.createShip3(spaceShip2); // level trial flight spaceship
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        spaceShip1=new SpaceShip();
        spaceShip1.setSpaceship(2,PlayerColors.GREEN); // empty spaceship
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

        spaceShip3.setBattery();
        assertEquals(5, spaceShip3.getNumBatteries());
    }

    @Test
    void getGoodsCapacity() {
        assertEquals(5, spaceShip2.getGoodsCapacity());

        assertEquals(6, spaceShip3.getGoodsCapacity());
    }

    @Test
    void setAndGetNumCrew() {
        spaceShip1.setNumHuman();
        assertEquals(2,spaceShip1.getNumCrew());

        spaceShip2.setNumHuman();
        assertEquals(6,spaceShip2.getNumCrew());
    }


    @Test
    void setNumAlien() {
        spaceShip1.setNumAlien(new ArrayList<>());
        spaceShip1.setNumHuman();
        assertEquals(2,spaceShip1.getNumCrew());

        ArrayList<int[]> aliencoord=new ArrayList<>();
        aliencoord.add(new int[]{7,6});
        spaceShip3.setNumAlien(aliencoord);
        spaceShip3.setNumHuman();
        switch (spaceShip3.getComponents()[7][6]){
            case Cabin cabin->{assertNotNull(cabin.getAlien());
            assertEquals(AlienColor.BROWN, cabin.getAlien().getColor());}
            default -> {}
        }

        assertEquals(7, spaceShip3.getNumCrew());
    }

    @Test
    void removeHuman() {
        spaceShip1.setNumHuman();
        spaceShip1.removeHuman(7,7);
        assertEquals(1,spaceShip1.getNumCrew());

        spaceShip2.setNumHuman();
        spaceShip2.removeHuman(8,7);
        assertEquals(5,spaceShip2.getNumCrew());
    }

    @Test
    void removeNumHuman() {
        spaceShip1.setNumHuman();
        ArrayList<int[]> coord1=new ArrayList<>();
        coord1.add(new int[]{7,7,1});
        spaceShip1.removeNumHuman(coord1);
        assertEquals(1,spaceShip1.getNumCrew());

        spaceShip2.setNumHuman();
        ArrayList<int[]> coord2=new ArrayList<>();
        coord2.add(new int[]{7,7,2});
        coord2.add(new int[]{7,8,2});
        coord2.add(new int[]{8,7,1});
        spaceShip2.removeNumHuman(coord2);
        assertEquals(1,spaceShip2.getNumCrew());
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

        assertEquals(1,spaceShip3.getNumDoubleCannon());
    }

    @Test
    void getNumDoubleEngine() {
        assertEquals(1,spaceShip2.getNumDoubleEngine());

        assertEquals(2,spaceShip3.getNumDoubleEngine());
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
        float expected=(1/2)+2;
        assertEquals(expected,spaceShip3.getSingleCannonPower());
    }

    @Test
    void activateAndGetDoubleCannon() {
        assertEquals(0,spaceShip2.getDoubleCannon());

        assertEquals(0,spaceShip3.getDoubleCannon());
        spaceShip3.setBattery();
        ArrayList<int[]> coord=new ArrayList<>();
        ArrayList<int[]> batt=new ArrayList<>();
        coord.add(new int[]{5,8});
        batt.add(new int[]{6,9,1});
        try {
            spaceShip3.activateDoubleCannons(coord,batt);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }
        assertEquals(2,spaceShip3.getDoubleCannon());
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
    void deactivateDoubleEngines() {
        spaceShip2.setBattery();
        ArrayList<int[]> coord=new ArrayList<>();
        ArrayList<int[]> batt=new ArrayList<>();
        coord.add(new int[]{8,6});
        batt.add(new int[]{8,8,1});
        try {
            spaceShip2.activateDoubleEngines(coord,batt);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }
        spaceShip2.deactivateDoubleEngines();
        switch(spaceShip2.getComponents()[8][6]){
            case Engine component->{
                assertFalse(component.getIsActive());
            }
            default -> {}
        }

    }

    @Test
    void getTotalEnginePower() {
        assertEquals(2,spaceShip2.getTotalEnginePower());

        spaceShip2.setBattery();
        ArrayList<int[]> coord=new ArrayList<>();
        ArrayList<int[]> batt=new ArrayList<>();
        coord.add(new int[]{8,6});
        batt.add(new int[]{8,8,1});
        try {
            spaceShip2.activateDoubleEngines(coord,batt);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }
        assertEquals(4,spaceShip2.getTotalEnginePower());
    }

    @Test
    void checkDoubleEngine() {
        ArrayList<int[]> coord=new ArrayList<>();
        coord.add(new int[]{8,6});
        assertTrue(spaceShip2.checkDoubleEngine(coord));

        coord.add(new int[]{8,8});
        assertFalse(spaceShip2.checkDoubleEngine(coord));
    }

    @Test
    void getTotalCannonPower() {
        assertEquals(2,spaceShip2.getTotalCannonPower());


        spaceShip3.setBattery();
        ArrayList<int[]> coord=new ArrayList<>();
        ArrayList<int[]> batt=new ArrayList<>();
        coord.add(new int[]{5,8});
        batt.add(new int[]{6,9,1});
        try {
            spaceShip3.activateDoubleCannons(coord,batt);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }
        float expected=(1/2)+4;
        assertEquals(expected,spaceShip3.getTotalCannonPower());

    }

    @Test
    void deactivateDoubleCannons() {
        spaceShip3.setBattery();
        ArrayList<int[]> coord=new ArrayList<>();
        ArrayList<int[]> batt=new ArrayList<>();
        coord.add(new int[]{5,8});
        batt.add(new int[]{6,9,1});
        try {
            spaceShip3.activateDoubleCannons(coord,batt);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }

        spaceShip3.deactivateDoubleCannons();
        assertEquals(0,spaceShip3.getDoubleCannon());
    }


    @Test
    void activateShield() {
        spaceShip2.setBattery();
        spaceShip2.activateShield(new int[]{6,6},new int[]{8,8,1});

        switch(spaceShip2.getComponents()[6][6]){
            case Shield component->{
                assertTrue(component.getIsActive());
            }
            default -> {}
        }
    }

    @Test
    void deActivateShields() {
        spaceShip2.setBattery();
        spaceShip2.activateShield(new int[]{6,6},new int[]{8,8,1});

        spaceShip2.deActivateShields();
        switch(spaceShip2.getComponents()[6][6]){
            case Shield component->{
                assertFalse(component.getIsActive());
            }
            default -> {}
        }
    }

    @Test
    void countExposedConnectors() {
        assertEquals(5, spaceShip2.countExposedConnectors());

        assertEquals(5, spaceShip2.countExposedConnectors());
    }

    @Test
    void wellDone() {
        assertTrue(spaceShip2.wellDone());

        assertTrue(spaceShip3.wellDone());
    }

    @Test
    void wellDoneForTrialFlight() {
        assertTrue(spaceShip2.wellDoneForTrialFlight());
    }

    @Test
    void isHit() {
        assertFalse(spaceShip2.isHit(0,4));
        assertFalse(spaceShip2.isHit(0,10));
        assertTrue(spaceShip2.isHit(0,5));
        assertTrue(spaceShip2.isHit(0,6));
        assertTrue(spaceShip2.isHit(0,7));
        assertTrue(spaceShip2.isHit(0,8));
        assertTrue(spaceShip2.isHit(0,9));
        assertFalse(spaceShip2.isHit(0,12));

        assertFalse(spaceShip2.isHit(0,13));

        assertFalse(spaceShip2.isHit(1,11));
    }

    @Test
    void destroyComponent() {
        spaceShip2.destroyComponent(9,0,new Bank());
        assertEquals(2,spaceShip2.getGoodsCapacity());

        spaceShip2.destroyComponent(8,1,new Bank());
        assertNull(spaceShip2.getComponents()[8][8]);
    }

    @Test
    void shootMeteor() {
        assertTrue(spaceShip2.shootMeteor(0,7));
        assertTrue(spaceShip2.shootMeteor(0,8));
    }

    @Test
    void blockSmallMeteor() {
        spaceShip2.setBattery();
        assertTrue( spaceShip2.blockSmallMeteor(0,7,false));
        assertTrue( spaceShip2.blockSmallMeteor(0,8,false));
        assertTrue( spaceShip2.blockSmallMeteor(0,9,false));
        assertFalse( spaceShip2.blockSmallMeteor(0,6,false));

        assertTrue( spaceShip2.blockSmallMeteor(1,9,true));
        assertTrue( spaceShip2.blockSmallMeteor(1,7,false));

        assertTrue( spaceShip2.blockSmallMeteor(2,7,false));
        assertTrue( spaceShip2.blockSmallMeteor(2,8,false));

        assertFalse( spaceShip2.blockSmallMeteor(3,7,false));
        assertTrue( spaceShip2.blockSmallMeteor(3,8,false));

    }

    @Test
    void blockLightCannonShot() {
        spaceShip2.setBattery();
        assertTrue(spaceShip2.blockLightCannonShot(0,true));
        assertTrue(spaceShip2.blockLightCannonShot(1,true));
        assertTrue(spaceShip2.blockLightCannonShot(2,true));
        assertFalse(spaceShip2.blockLightCannonShot(3,true));
        assertFalse(spaceShip2.blockLightCannonShot(3,false));
    }

    @Test
    void epidemic() {
        spaceShip2.setNumHuman();
        assertEquals(6,spaceShip2.getNumCrew());
        spaceShip2.epidemic();
        assertEquals(3,spaceShip2.getNumCrew());
    }

    @Test
    void addAndGetGoodsList() {
        Goods good1=new Goods(2);
        spaceShip2.addGoodsList(good1);
        assertEquals(1,spaceShip2.getGoodsList().size());
        Goods good2=new Goods(4);
        spaceShip2.addGoodsList(good2);
        assertEquals(2,spaceShip2.getGoodsList().size());
        assertEquals(good2,spaceShip2.getGoodsList().get(0));
        assertEquals(good1,spaceShip2.getGoodsList().get(1));
        Goods good3=new Goods(3);
        spaceShip2.addGoodsList(good3);
        assertEquals(good2,spaceShip2.getGoodsList().get(0));
        assertEquals(good3,spaceShip2.getGoodsList().get(1));
        assertEquals(good1,spaceShip2.getGoodsList().get(2));
    }

    @Test
    void addGoods() {
        ArrayList<Goods> goods=new ArrayList<>();
        Goods good1=new Goods(3);
        goods.add(good1);
        ArrayList<int[]> coord=new ArrayList<>();
        coord.add(new int[]{7,5});
        spaceShip2.addGoods(goods,coord,new Bank());

        switch(spaceShip2.getComponents()[7][5]){
            case Load load->{
                assertEquals(load.getGoods().size(),1);
            }
            default -> {}
        }
    }

    @Test
    void addGood() {
        Goods good1=new Goods(3);
        try {
            good1.setX(7);
            good1.setY(8);
            spaceShip3.addGood(good1,new int[]{7,8},0,new Bank());
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }
        switch(spaceShip3.getComponents()[7][8]){
            case Load load->{
                assertNotNull(load.getGoods().get(0));
                assertEquals(load.getGoods().get(0),good1);
                assertEquals(load.getGoods().get(0).getValue(),3);
            }
            default -> {}
        }

    }

    @Test
    void removeBatteries() {
        spaceShip3.setBattery();
        //spaceShip3.removeBatteries();
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
        ArrayList<Goods> goods=new ArrayList<>();
        Goods good1=new Goods(4);
        goods.add(good1);
        ArrayList<int[]> coord=new ArrayList<>();
        coord.add(new int[]{7,5});
        spaceShip2.addGoods(goods,coord,new Bank());

        assertEquals(1,spaceShip2.getRedGoods());
    }

    @Test
    void getGreenGoods() {
        ArrayList<Goods> goods=new ArrayList<>();
        Goods good1=new Goods(2);
        goods.add(good1);
        ArrayList<int[]> coord=new ArrayList<>();
        coord.add(new int[]{7,5});
        spaceShip2.addGoods(goods,coord,new Bank());

        assertEquals(1,spaceShip2.getGreenGoods());
    }

    @Test
    void getBlueGoods() {
        ArrayList<Goods> goods=new ArrayList<>();
        Goods good1=new Goods(1);
        goods.add(good1);
        ArrayList<int[]> coord=new ArrayList<>();
        coord.add(new int[]{7,5});
        spaceShip2.addGoods(goods,coord,new Bank());

        assertEquals(1,spaceShip2.getBlueGoods());
    }

    @Test
    void getYellowGoods() {
        ArrayList<Goods> goods=new ArrayList<>();
        Goods good1=new Goods(3);
        goods.add(good1);
        ArrayList<int[]> coord=new ArrayList<>();
        coord.add(new int[]{7,5});
        spaceShip2.addGoods(goods,coord,new Bank());

        assertEquals(1,spaceShip2.getYellowGoods());

    }

    @Test
    void removeGoods() {
        ArrayList<Goods> goods=new ArrayList<>();
        Goods good=new Goods(4);
        good.setX(7);
        good.setY(5);
        goods.add(good);
        switch(spaceShip2.getComponents()[7][5]){
            case Load component->{
                Goods good1=new Goods(4);
                good1.setX(7);
                good1.setY(5);
                component.addGood(good1);
            }
            default -> {}
        }
        spaceShip2.removeGoods(goods);
        switch(spaceShip2.getComponents()[7][5]){
            case Load component->{
                assertTrue(component.getGoods().isEmpty());
            }
            default -> {}
        }
    }

    @Test
    void getAlienCabin() {
        assertEquals(0,spaceShip2.getAlienCabin().size());
    }

    @Test
    void removeAllHumans() {
        spaceShip2.setNumHuman();
        spaceShip2.removeAllHumans();
        assertEquals(0,spaceShip2.getNumCrew());
    }

}