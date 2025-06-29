package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpaceShipTest {

    private SpaceShip spaceShip1;
    private SpaceShip spaceShip2;
    private SpaceShip spaceShip3;
    private CreateShipTest support=new CreateShipTest();

    @BeforeEach
    void setUp() {
        try {
            spaceShip3=support.createShip1(spaceShip3); // level 2 spaceship
            spaceShip2=support.createShip3(spaceShip2); // level trial flight spaceship
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        spaceShip1=new SpaceShip();
        spaceShip1.initSpaceShip(2,PlayerColors.GREEN); // empty spaceship
        Game.initializeForTest(spaceShip2,spaceShip3);
    }

    @Test
    void initSpaceShip() {
        spaceShip1.initSpaceShip(2,PlayerColors.RED);

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

        spaceShip2.initSpaceShip(0,PlayerColors.GREEN);
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
        aliencoord.add(new int[]{7,6,1});
        spaceShip3.setNumAlien(aliencoord);
        spaceShip3.setNumHuman();
        switch (spaceShip3.getComponents()[7][6]){
            case Cabin cabin->{assertNotNull(cabin.getAlien());
            assertEquals(AlienColor.BROWN, cabin.getAlien().getColor());}
            default -> {}
        }
        assertEquals(7, spaceShip3.getNumCrew());

        SpaceShip shipAlien1=new SpaceShip();
        shipAlien1.initSpaceShip(2,PlayerColors.BLUE);
        try {
            shipAlien1.addComponent(
                    new Cabin(3,3,3,3,"Cabin","",null,false),
                    7 ,8
            );
            shipAlien1.addComponent(
                    new LifeSupport(3,3,3,3,"Life Support","",AlienColor.PURPLE),
                    8,8
            );
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        ArrayList<int[]> aliencoord2=new ArrayList<>();
        aliencoord2.add(new int[]{7,8,0});
        shipAlien1.setNumAlien(aliencoord2);
        shipAlien1.setNumHuman();
        assertEquals(3,shipAlien1.getNumCrew());

        SpaceShip shipAlien2=new SpaceShip();
        shipAlien2.initSpaceShip(2,PlayerColors.BLUE);
        try {
            shipAlien2.addComponent(
                    new Cabin(3,3,3,3,"Cabin","",null,false),
                    6 ,7
            );
            shipAlien2.addComponent(
                    new LifeSupport(3,3,3,3,"Life Support","",AlienColor.PURPLE),
                    6,6
            );
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        ArrayList<int[]> aliencoord3=new ArrayList<>();
        aliencoord3.add(new int[]{6,7,0});
        shipAlien2.setNumAlien(aliencoord3);
        shipAlien2.setNumHuman();
        assertEquals(3,shipAlien2.getNumCrew());

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
        spaceShip1.removeNumHuman(coord1,1);
        assertEquals(1,spaceShip1.getNumCrew());

        spaceShip2.setNumHuman();
        ArrayList<int[]> coord2=new ArrayList<>();
        coord2.add(new int[]{7,7,2});
        coord2.add(new int[]{7,8,2});
        coord2.add(new int[]{8,7,1});
        spaceShip2.removeNumHuman(coord2,4);
        assertEquals(2,spaceShip2.getNumCrew());
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
        Component component=new Engine(2,2,2,"Engine","",false);
        spaceShip2.initSpaceShip(2,PlayerColors.BLUE);
        assertThrows(InvalidCoordinates.class, () -> spaceShip2.addComponent(component,6,5));
        assertThrows(InvalidCoordinates.class, () -> spaceShip2.addComponent(component,9,7));
        try {
            spaceShip2.addComponent(component,8,7);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        try {
            spaceShip2.removeComponent(8,7);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        assertNull(spaceShip2.getComponents()[8][7]);
        assertThrows(InvalidCoordinates.class,()->{spaceShip2.removeComponent(8,7);});
        try {
            spaceShip2.addComponent(new Cannon(3,3,3,"","",true),6,7);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        try {
            spaceShip2.removeComponent(6,7);
            spaceShip2.removeComponent(5,7);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        assertNull(spaceShip2.getComponents()[6][7]);
        assertNotNull(spaceShip2.getComponents()[5][7]);

    }



    @Test
    void getSingleCannonPower() {
        assertEquals(2,spaceShip2.getSingleCannonPower());
        float expected=((float) 1 /2)+2;
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

        try {
            spaceShip3.removeComponent(7,10);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }

        try {
            Component component=new Cannon(3,3,3,"","",true);
            component.turn();
            spaceShip3.addComponent(component,7,10);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        spaceShip3.deactivateDoubleCannons();
        ArrayList<int[]> coord1=new ArrayList<>();
        ArrayList<int[]> batt1=new ArrayList<>();
        coord1.add(new int[]{7,10});
        batt1.add(new int[]{6,9,1});
        try {
            spaceShip3.activateDoubleCannons(coord1,batt1);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }
        assertEquals(1,spaceShip3.getDoubleCannon());


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
        float expected=((float)1/2)+4;
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
        try {
            spaceShip2.activateShield(new int[]{6,6},new int[]{8,8,1});
        } catch (NotEnoughBatteries e) {
        }

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
        try {
            spaceShip2.activateShield(new int[]{6,6},new int[]{8,8,1});
        } catch (NotEnoughBatteries e) {
        }

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

        try {
            spaceShip3.removeComponent(9,4);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        Component cannon=new Cannon(0,0,3,"","",false);
        cannon.turn();
        cannon.turn();
        try {
            spaceShip3.addComponent(cannon,9,4);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        assertTrue(spaceShip3.wellDone());
        try {
            spaceShip3.removeComponent(6,5);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        try {
            spaceShip3.addComponent(new Cabin(3,1,2,0,"","",null,false),6,5);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        assertFalse(spaceShip3.wellDone());

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

        assertTrue(spaceShip3.isHit(1,9));
        assertTrue(spaceShip3.isHit(2,7));
        assertTrue(spaceShip3.isHit(3,7));
    }

    @Test
    void destroyComponent() {
        spaceShip2.destroyComponent(8,1,new Bank());
        spaceShip3.setBattery();
        spaceShip3.setNumHuman();
        spaceShip3.destroyComponent(5,0,new Bank());
        assertNull(spaceShip3.getComponents()[6][5]);

        switch(spaceShip3.getComponents()[9][8]){
            case Load load->{
                Goods good=new Goods(2);
                good.setX(9);
                good.setY(8);
                load.addGood(0,good);
                spaceShip3.addGoodsList(good);
            }
            default -> {}
        }
        spaceShip3.destroyComponent(9,1,new Bank());
        assertNull(spaceShip3.getComponents()[9][8]);

        spaceShip3.destroyComponent(6,2,new Bank());
        assertNull(spaceShip3.getComponents()[9][6]);

        spaceShip3.destroyComponent(8,3,new Bank());
        assertNull(spaceShip3.getComponents()[8][5]);
        assertNull(spaceShip3.getComponents()[9][5]);

        spaceShip3.destroyComponent(6,1,new Bank());
        assertNull(spaceShip3.getComponents()[6][9]);
        ArrayList<int[]> list=new ArrayList<>();
        list.add(new int[]{7,6,1});
        spaceShip3.setNumAlien(list);
        spaceShip3.destroyComponent(6,3,new Bank());

        spaceShip3.destroyComponent(7,1,new Bank());
        spaceShip3.destroyComponent(7,1,new Bank());


    }

    @Test
    void blockBigMeteor() {
        assertTrue(spaceShip2.blockBigMeteor(0,7));
        assertTrue(spaceShip2.blockBigMeteor(0,8));

        assertTrue(spaceShip3.blockBigMeteor(1,7));
        assertTrue(spaceShip3.blockBigMeteor(0,4));
        try {
            spaceShip3.removeComponent(9,4);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        try {
            spaceShip3.addComponent(
                    new Cannon(3,3,3,"","",true),
                    9,4
            );
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        assertFalse(spaceShip3.blockBigMeteor(3,9));
        switch(spaceShip3.getComponents()[9][4]){
            case Cannon cannon->{
                cannon.setIsActive(true);
            }
            default -> {}
        }
        assertTrue(spaceShip3.blockBigMeteor(3,9));
    }

    @Test
    void blockSmallMeteor() {
        spaceShip2.setBattery();
        assertTrue( spaceShip2.blockSmallMeteor(0,7));
        assertTrue( spaceShip2.blockSmallMeteor(0,8));
        assertTrue( spaceShip2.blockSmallMeteor(0,9));
        assertFalse( spaceShip2.blockSmallMeteor(0,6));

        assertTrue( spaceShip2.blockSmallMeteor(1,7));

        assertTrue( spaceShip2.blockSmallMeteor(2,7));
        assertTrue( spaceShip2.blockSmallMeteor(2,8));

        assertFalse( spaceShip2.blockSmallMeteor(3,7));
        assertTrue( spaceShip2.blockSmallMeteor(3,8));

        assertFalse(spaceShip3.blockSmallMeteor(3,8));
        spaceShip3.setBattery();
        try {
            spaceShip3.activateShield(new int[]{8,8},new int[]{6,9,1});
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }
        try {
            spaceShip3.removeComponent(9,8);
            spaceShip3.removeComponent(8,10);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }

        assertTrue(spaceShip3.blockSmallMeteor(2,8));
        assertTrue(spaceShip3.blockSmallMeteor(1,8));
        switch(spaceShip3.getComponents()[8][8]){
            case Shield shield->{
                shield.setDirection(Direction.NW);
                shield.setIsActive(true);
            }
            default -> {}
        }
        assertTrue(spaceShip3.blockSmallMeteor(3,8));
        try {
            spaceShip3.removeComponent(5,6);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        assertTrue(spaceShip3.blockSmallMeteor(0,6));
        spaceShip3.deActivateShields();

        assertFalse(spaceShip3.blockSmallMeteor(2,8));
        assertFalse(spaceShip3.blockSmallMeteor(1,8));
        assertFalse(spaceShip3.blockSmallMeteor(3,8));
        assertFalse(spaceShip3.blockSmallMeteor(0,6));
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
    void addGood() {
        Goods good1=new Goods(3);
        try {
            good1.setX(8);
            good1.setY(9);
            spaceShip3.addGood(good1,new int[]{8,9},0,new Bank());
            spaceShip3.addGood(good1,new int[]{8,9},2,new Bank());
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }
        switch(spaceShip3.getComponents()[8][9]){
            case Load load->{
                assertNotNull(load.getGoods().get(0));
                assertEquals(load.getGoods().get(0),good1);
                assertEquals(load.getGoods().get(0).getValue(),3);
                assertNotNull(load.getGoods().get(2));
                assertEquals(load.getGoods().get(2),good1);
                assertEquals(load.getGoods().get(0).getValue(),3);
            }
            default -> {}
        }

    }

    @Test
    void removeBatteries() {
        spaceShip3.setBattery();
        ArrayList<int[]> batt=new ArrayList<>();
        batt.add(new int[]{5,6,2});
        batt.add(new int[]{6,9,1});
        try {
            spaceShip3.removeBatteries(batt);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }
        assertEquals(2,spaceShip3.getNumBatteries());
        switch (spaceShip3.getComponents()[5][6]){
            case BatteryStorage bs->{
                assertEquals(0,bs.getQuantity());
            }
            default -> {}
        }
        switch (spaceShip3.getComponents()[6][9]){
            case BatteryStorage bs->{
                assertEquals(2,bs.getQuantity());
            }
            default -> {}
        }
    }

    @Test
    void checkLoad() {
        assertTrue(spaceShip3.checkLoad(new int[]{8,9}));
        assertFalse(spaceShip3.checkLoad(new int[]{6,8}));
    }

    @Test
    void checkDoubleCannon() {
        ArrayList<int[]> coord=new ArrayList<>();
        coord.add(new int[]{5,8});
        assertTrue(spaceShip3.checkDoubleCannon(coord));
        coord.add(new int[]{5,6});
        assertFalse(spaceShip3.checkDoubleCannon(coord));
    }

    @Test
    void checkShield() {
       assertTrue(spaceShip3.checkShield(new int[]{8,8}));
       assertFalse(spaceShip3.checkShield(new int[]{7,8}));
    }

    @Test
    void checkBatteryStorage() {
        ArrayList<int[]> coord=new ArrayList<>();
        coord.add(new int[]{6,9});
        coord.add(new int[]{5,6});
        assertTrue(spaceShip3.checkBatteryStorage(coord));
        coord.add(new int[]{7,6});
        assertFalse(spaceShip3.checkBatteryStorage(coord));
    }

    @Test
    void checkCabin() {
        spaceShip3.setNumHuman();
        ArrayList<int[]> coord=new ArrayList<>();
        coord.add(new int[]{7,7,1});
        coord.add(new int[]{7,6,1});
        try {
            assertTrue(spaceShip3.checkCabin(coord,2));
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        coord.add(new int[]{6,7,1});
    }

    @Test
    void swapGoods() {
        Goods good1=new Goods(3);
        Goods good2=new Goods(2);
        try {
            spaceShip3.addGood(good1,new int[]{7,8},0,new Bank());
            spaceShip3.addGood(good2,new int[]{8,9},1,new Bank());
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceShip3.swapGoods(new int[]{7,8},0,new int[]{8,9},1);
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }
        switch(spaceShip3.getComponents()[7][8]){
            case Load load->{
                assertEquals(load.getGoods().get(0),good2);

            }
            default -> {}
        }
        switch(spaceShip3.getComponents()[8][9]){
            case Load load->{
                assertEquals(load.getGoods().get(1),good1);

            }
            default -> {}
        }

    }

    @Test
    void getRedGoods() {
        Goods good1=new Goods(4);
        try {
            spaceShip2.addGood(good1,new int[]{7,5},0,new Bank());
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }

        assertEquals(1,spaceShip2.getRedGoods());

        Goods good2=new Goods(3);

    }

    @Test
    void getGreenGoods() {
        ArrayList<Goods> goods=new ArrayList<>();
        Goods good1=new Goods(2);
        try {
            spaceShip2.addGood(good1,new int[]{7,5},0,new Bank());
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }

        assertEquals(1,spaceShip2.getGreenGoods());
    }

    @Test
    void getBlueGoods() {
        ArrayList<Goods> goods=new ArrayList<>();
        Goods good1=new Goods(1);
        try {
            spaceShip2.addGood(good1,new int[]{7,5},0,new Bank());
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }

        assertEquals(1,spaceShip2.getBlueGoods());
    }

    @Test
    void getYellowGoods() {
        ArrayList<Goods> goods=new ArrayList<>();
        Goods good1=new Goods(3);
        try {
            spaceShip2.addGood(good1,new int[]{7,5},0,new Bank());
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }

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
                ArrayList<Goods> list=new ArrayList<>();
                list.add(good1);
                good1.setX(7);
                good1.setY(5);
                component.addGood(0,good1);
                spaceShip2.addGoodsList(good1);
                spaceShip2.removeGoods(list);
            }
            default -> {}
        }
        spaceShip2.removeGoods(goods);
        switch(spaceShip2.getComponents()[7][5]){
            case Load component->{
                assertFalse(component.getGoods().contains(good));
            }
            default -> {}
        }
    }

    @Test
    void getAlienCabin() {
        assertEquals(0,spaceShip2.getAlienCabin().size());

        ArrayList<int[]> coord=spaceShip3.getAlienCabin();
        assertEquals(1,coord.size());
        assertEquals(7, coord.get(0)[0]);
        assertEquals(6, coord.get(0)[1]);
        assertEquals(1, coord.get(0)[2]);
    }

    @Test
    void removeAllHumans() {
        spaceShip2.setNumHuman();
        spaceShip2.removeAllHumans();
        assertEquals(0,spaceShip2.getNumCrew());
    }

    @Test
    void getGoodsByColor(){
        Goods good1=new Goods(2);
        spaceShip2.addGoodsList(good1);
        Goods good2=new Goods(4);
        spaceShip2.addGoodsList(good2);

        Map<String,Integer> goodsByColor=spaceShip2.getGoodsByColor();
        assertEquals(1, goodsByColor.get("red"));
        assertEquals(1, goodsByColor.get("green"));
        assertEquals(2, goodsByColor.size());
    }

    @Test
    void removeAllBatteries(){
        spaceShip3.setBattery();
        spaceShip3.removeAllBatteries();
        assertEquals(0,spaceShip3.getNumBatteries());
    }

    @Test
    void testIsChanged_NoDifference() {
        CreateShipTest createShip = new CreateShipTest();
        SpaceShip s1 = new SpaceShip();
        SpaceShip s2 = new SpaceShip();
        try {
            s1 = createShip.createShip1(s1);
            s2 = createShip.createShip1(s2);
            s1.setBattery();
            s2.setBattery();
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }

        Goods g = new Goods(3);
        Goods g1 = new Goods(3);
        g.setX(8);
        g.setY(9);
        g1.setX(8);
        g1.setY(9);
        try {
            s1.addGood(g, new int[]{8, 9}, 0, new Bank());
            s2.addGood(g, new int[]{8, 9}, 0, new Bank());
            s1.addGood(g1, new int[]{8, 9}, 1, new Bank());
            s2.addGood(g1, new int[]{8, 9}, 1, new Bank());
            ArrayList<int[]> alienCoord = new ArrayList<>();
            alienCoord.add(new int[]{7, 6, 1});
            s1.setNumAlien(alienCoord);
            s2.setNumAlien(alienCoord);
            s1.setNumHuman();
            s2.setNumHuman();
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }

        assertFalse(s1.isChanged(s2));
    }

    @Test
    void testIsChanged_DifferentAliens() {
        CreateShipTest createShip = new CreateShipTest();
        SpaceShip s1 = new SpaceShip();
        SpaceShip s2 = new SpaceShip();
        try {
            s1 = createShip.createShip1(s1);
            s2 = createShip.createShip1(s2);
            s1.setBattery();
            s2.setBattery();
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }

        Goods g = new Goods(3);
        Goods g1 = new Goods(3);
        g.setX(8);
        g.setY(9);
        g1.setX(8);
        g1.setY(9);
        try {
            s1.addGood(g, new int[]{8, 9}, 0, new Bank());
            s2.addGood(g, new int[]{8, 9}, 0, new Bank());
            s1.addGood(g1, new int[]{8, 9}, 1, new Bank());
            s2.addGood(g1, new int[]{8, 9}, 1, new Bank());
            ArrayList<int[]> alienCoord = new ArrayList<>();
            alienCoord.add(new int[]{7, 6, 1});
            s1.setNumAlien(alienCoord);
            s1.setNumHuman();
            s2.setNumHuman();
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }

        assertTrue(s1.isChanged(s2));
    }

    @Test
    void testIsChanged_DifferentGoods() {
        CreateShipTest createShip = new CreateShipTest();
        SpaceShip s1 = new SpaceShip();
        SpaceShip s2 = new SpaceShip();
        try {
            s1 = createShip.createShip1(s1);
            s2 = createShip.createShip1(s2);
            s1.setBattery();
            s2.setBattery();
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }

        Goods g = new Goods(3);
        Goods g1 = new Goods(3);
        g.setX(8);
        g.setY(9);
        g1.setX(8);
        g1.setY(9);
        try {
            s1.addGood(g, new int[]{8, 9}, 0, new Bank());
            s2.addGood(g, new int[]{8, 9}, 0, new Bank());
            s1.addGood(g1, new int[]{8, 9}, 1, new Bank());
            ArrayList<int[]> alienCoord = new ArrayList<>();
            alienCoord.add(new int[]{7, 6, 1});
            s1.setNumAlien(alienCoord);
            s2.setNumAlien(alienCoord);
            s1.setNumHuman();
            s2.setNumHuman();
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }

        assertTrue(s1.isChanged(s2));
    }

    @Test
    void testIsChanged_DifferentBatteries() {
        CreateShipTest createShip = new CreateShipTest();
        SpaceShip s1 = new SpaceShip();
        SpaceShip s2 = new SpaceShip();
        try {
            s1 = createShip.createShip1(s1);
            s2 = createShip.createShip1(s2);
            s2.setBattery();
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }

        Goods g = new Goods(3);
        Goods g1 = new Goods(3);
        g.setX(8);
        g.setY(9);
        g1.setX(8);
        g1.setY(9);
        try {
            s1.addGood(g, new int[]{8, 9}, 0, new Bank());
            s2.addGood(g, new int[]{8, 9}, 0, new Bank());
            s1.addGood(g1, new int[]{8, 9}, 1, new Bank());
            s2.addGood(g1, new int[]{8, 9}, 1, new Bank());
            ArrayList<int[]> alienCoord = new ArrayList<>();
            alienCoord.add(new int[]{7, 6, 1});
            s1.setNumAlien(alienCoord);
            s2.setNumAlien(alienCoord);
            s1.setNumHuman();
            s2.setNumHuman();
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }

        assertTrue(s1.isChanged(s2));
    }

    @Test
    void testIsChanged_DifferentComponents() {
        CreateShipTest createShip = new CreateShipTest();
        SpaceShip s1 = new SpaceShip();
        SpaceShip s2 = new SpaceShip();
        try {
            s1 = createShip.createShip1(s1);
            s2 = createShip.createShip2(s2);
            s2.setBattery();
            s1.setBattery();
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }

        s1.setNumHuman();
        s2.setNumHuman();


        assertTrue(s1.isChanged(s2));
    }



}