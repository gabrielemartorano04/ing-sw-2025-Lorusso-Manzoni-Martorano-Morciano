package it.polimi.ingsw.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TruckerTest {

    private Trucker trucker;

    @Before
    public void setup(){
        trucker=new Trucker("frenk", PlayerColors.BLUE);
    }

    @After
    public void teardown(){

    }

    @Test
    public void setAndGetPosition(){
        trucker.setPosition(2);
        assertEquals(2, trucker.getPosition());
    }

    @Test
    public void getColor(){
        assertEquals(PlayerColors.BLUE, trucker.getColor());
    }

    @Test
    public void setAndGetBuildingState(){
        Assert.assertEquals(BuildingState.BUILDING, trucker.getBuildingState());
        trucker.setBuildingState(BuildingState.END);
        assertEquals(BuildingState.END, trucker.getBuildingState());
    }

    @Test
    public void getNickname(){
        assertEquals("frenk", trucker.getNickname());
    }

    @Test
    public void addAndGetCosmicCredit(){
        assertEquals(0,trucker.getCosmicCredit());
        trucker.addCosmicCredit(2);
        assertEquals(2,trucker.getCosmicCredit());
        trucker.addCosmicCredit(3);
        assertEquals(5,trucker.getCosmicCredit());
    }

    @Test
    public void getSpaceship(){
        assertNotNull(trucker.getSpaceShip());
    }

    @Test
    public void chooseWhatToDo(){
        trucker.chooseWhatToDo("Turn Hourglass");
        assertEquals(BuildingState.TURN_HOURGLASS, trucker.getBuildingState());

        trucker.setBuildingState(BuildingState.BUILDING);
        trucker.chooseWhatToDo("Select Component");
        assertEquals(BuildingState.SELECT_COMPONENT, trucker.getBuildingState());

        trucker.setBuildingState(BuildingState.BUILDING);
        trucker.chooseWhatToDo("Show Stack");
        assertEquals(BuildingState.SHOW_STACK, trucker.getBuildingState());

        trucker.setBuildingState(BuildingState.BUILDING);
        trucker.chooseWhatToDo("End");
        assertEquals(BuildingState.WAIT_END, trucker.getBuildingState());

        trucker.setBuildingState(BuildingState.SELECT_COMPONENT);
        trucker.chooseWhatToDo("Link");
        assertEquals(BuildingState.LINK, trucker.getBuildingState());

        trucker.setBuildingState(BuildingState.SELECT_COMPONENT);
        trucker.chooseWhatToDo("Keep");
        assertEquals(BuildingState.KEEP, trucker.getBuildingState());

        trucker.setBuildingState(BuildingState.SELECT_COMPONENT);
        trucker.chooseWhatToDo("Discard");
        assertEquals(BuildingState.BUILDING, trucker.getBuildingState());
    }

}
